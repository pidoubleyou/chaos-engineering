package pw.chaos.tracking.persistence;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table
@Getter
@Setter
@NoArgsConstructor
public class Tracking {
  @Id
 // @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private Long eventId;
  private Long registrationId;

  private LocalDateTime start;
  private LocalDateTime end;
}
