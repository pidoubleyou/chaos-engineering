package pw.chaos.tracking.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class TrackingEvent {
  private Long eventId;
  private Long registrationId;
  private LocalDateTime timestamp;
}
