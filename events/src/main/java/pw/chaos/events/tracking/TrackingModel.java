package pw.chaos.events.tracking;

import lombok.*;
import pw.chaos.events.persistence.Event;
import pw.chaos.events.persistence.Registration;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class TrackingModel {
  private Long eventId;
  private Long[] registrations;

  public TrackingModel(Event event) {
    setEventId(event.getId());

    List<Long> tempRegistrations = new ArrayList<>();
    for (Registration registration : event.getRegistrations()) {
      tempRegistrations.add(registration.getId());
    }
    setRegistrations(tempRegistrations.toArray(Long[]::new));
  }
}
