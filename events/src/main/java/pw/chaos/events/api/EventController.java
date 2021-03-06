package pw.chaos.events.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pw.chaos.events.domain.EventService;
import pw.chaos.events.persistence.Event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/events")
public class EventController {
  @Autowired private EventService eventService;

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<EventModel> create(@RequestBody EventModel eventModel) {

    Event event = eventModel.fromModel();
    event = eventService.createEvent(event);

    Link link = (new EventModel(event)).getRequiredLink(LinkRelation.of("self"));

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(link.toUri());
    return new ResponseEntity<>(headers, HttpStatus.CREATED);
  }

  @GetMapping
  public Collection<EventModel> getAll() {
    return eventService.getAll().stream()
        .map(EventModel::new)
        .collect(Collectors.toCollection(ArrayList::new));
  }

  @GetMapping("/{id}")
  public ResponseEntity<EventModel> findById(@PathVariable Long id) {
    Optional<Event> event = eventService.findEvent(id);
    return event
        .map(EventModel::new)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping("/{id}/start")
  public ResponseEntity<EventModel> start(@PathVariable Long id) {
    Optional<Event> event = eventService.startEvent(id);
    return event
            .map(EventModel::new)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping("/{id}/end")
  public ResponseEntity<EventModel> end(@PathVariable Long id) {
    Optional<Event> event = eventService.endEvent(id);
    return event
            .map(EventModel::new)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
  }
}
