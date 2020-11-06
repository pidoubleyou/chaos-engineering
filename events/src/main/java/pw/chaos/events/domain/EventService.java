package pw.chaos.events.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pw.chaos.events.persistence.Event;
import pw.chaos.events.persistence.EventRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public class EventService {
  @Autowired private EventRepository eventRepository;

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
            .map(eventRepository::save);
  }

  public Optional<Event> endEvent(Long id) {
    return findEvent(id)
            .map(Event::end)
            .map(eventRepository::save);
  }
}
