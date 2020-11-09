package pw.chaos.tracking.persistence;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table(value = "tracking_event")
@Getter
@Setter
@NoArgsConstructor
public class Event {
  @Id
  private Long id;

  private LocalDateTime start;
  private LocalDateTime end;
}
