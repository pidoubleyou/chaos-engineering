package pw.chaos.tracking.api;

import lombok.*;
import pw.chaos.tracking.persistence.Tracking;

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

  public Tracking[] fromModel() {
    List<Tracking> trackings = new ArrayList<>();

    for(Long registrationId : registrations) {
      Tracking tracking =new Tracking();
      tracking.setEventId(getEventId());
      tracking.setRegistrationId(registrationId);
      trackings.add(tracking);
    }

    return trackings.toArray(Tracking[]::new);
  }
}
