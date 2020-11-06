package pw.chaos.events.api;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import pw.chaos.events.persistence.Event;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class EventModel extends RepresentationModel<EventModel> {
  private Long id;
  private String name;
  private String startTime;

  public EventModel(Event entity) {
    setId(entity.getId());
    setName(entity.getName());
    if (entity.getStart() != null) {
      setStartTime(entity.getStart().toString());
    }
    add(linkTo(methodOn(EventController.class).findById(entity.getId())).withSelfRel());
  }

  public Event fromModel() {
    Event event = new Event();
    event.setName(getName());
    return event;
  }
}
