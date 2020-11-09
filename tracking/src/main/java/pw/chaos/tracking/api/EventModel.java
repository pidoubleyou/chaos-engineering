package pw.chaos.tracking.api;

import lombok.*;
import pw.chaos.tracking.persistence.Event;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class EventModel {
  private Long id;
  private String start;
  private String end;

  public EventModel(Event event) {
    id = event.getId();
    start = event.getStart().toString();
    if (event.getEnd()!= null) {
      end = event.getEnd().toString();
    }
  }
}
