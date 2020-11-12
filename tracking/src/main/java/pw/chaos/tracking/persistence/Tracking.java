package pw.chaos.tracking.persistence;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import pw.chaos.tracking.domain.TrackingAlreadyFinishedException;
import pw.chaos.tracking.domain.TrackingAlreadyStartedException;
import pw.chaos.tracking.domain.TrackingNotStartedException;

import java.time.LocalDateTime;

@Table(value = "tracking_tracking")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Tracking {
  @Id
  private Long id;

  private Long eventId;
  private Long registrationId;

  private LocalDateTime start;
  private LocalDateTime finish;

  public Tracking start(LocalDateTime startTime) {
    if (start != null) {
      throw new TrackingAlreadyStartedException(eventId, registrationId);
    }

    start = startTime;
    return this;
  }

  public Tracking finish(LocalDateTime finishTime) {
    if (finish != null) {
      throw new TrackingAlreadyFinishedException(eventId, registrationId);
    }
    if (start == null) {
      throw new TrackingNotStartedException(eventId, registrationId);
    }

    finish = finishTime;
    return this;
  }
}
