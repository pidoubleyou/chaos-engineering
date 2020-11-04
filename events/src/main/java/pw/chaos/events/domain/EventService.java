package pw.chaos.events.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pw.chaos.events.persistence.Event;
import pw.chaos.events.persistence.EventRepository;

import java.util.Optional;

@Service
public class EventService {
  @Autowired
  private EventRepository eventRepository;

  public Event createEvent(Event event) {
    if (event.getId() == null) {
      event.setId(1L);
    }
    return eventRepository.save(event);
  }

  public Optional<Event> findEvent(Long id) {
    return eventRepository.findById(id);
  }
}
