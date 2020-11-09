package pw.chaos.tracking.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pw.chaos.tracking.persistence.Event;
import pw.chaos.tracking.persistence.EventRepository;
import pw.chaos.tracking.persistence.Tracking;
import pw.chaos.tracking.persistence.TrackingRepository;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class TrackingService {
  @Autowired private EventRepository eventRepository;
  @Autowired private TrackingRepository trackingRepository;

  public Mono<Event> start(Tracking[] trackings) {
    Event event = new Event();
    event.setId(trackings[0].getEventId());
    event.setStart(LocalDateTime.now());
    Mono<Event> eventMono = eventRepository.save(event);

    for (Tracking tracking : trackings) {
      trackingRepository.save(tracking);
    }

    return eventMono;
  }

  public Mono<Event> end(Long eventId) {
    return eventRepository
        .findById(eventId)
        .map(
            event -> {
              event.setEnd(LocalDateTime.now());
              return event;
            })
        .flatMap(eventRepository::save);
  }
}
