package pw.chaos.events.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pw.chaos.events.persistence.Event;
import pw.chaos.events.persistence.EventRepository;
import pw.chaos.events.tracking.TrackingClient;

import java.util.Collection;
import java.util.Optional;

@Service
@Slf4j
public class EventService {
  @Autowired private EventRepository eventRepository;
  @Autowired private TrackingClient trackingClient;

  public Event createEvent(Event event) {
    return eventRepository.save(event);
  }

  public Optional<Event> findEvent(Long id) {
    return eventRepository.findById(id);
  }

  public Collection<Event> getAll() {
    return (Collection<Event>) eventRepository.findAll();
  }

  public Optional<Event> startEvent(Long id) {
    return findEvent(id)
            .map(Event::start)
            .map(eventRepository::save)
            .map(this::notifyOnStarted);
  }

  public Optional<Event> endEvent(Long id) {
    return findEvent(id)
            .map(Event::end)
            .map(eventRepository::save)
            .map(this::notifyOnEnded);
  }


  private Event notifyOnStarted(Event event) {
    // TODO notification should be asynchronuous
    // Workaround: synchronous call with catch all
    try {
      trackingClient.onStarted(event);
    } catch(Exception e) {
      log.error("Notify on started failed: ", e);
    }
    return event;
  }

  private Event notifyOnEnded(Event event) {
    // TODO notification should be asynchronuous
    // Workaround: synchronous call with catch all
    try {
      trackingClient.onEnded(event);
    } catch(Exception e) {
      log.error("Notify on ended failed: ", e);
    }
    return event;
  }
}
