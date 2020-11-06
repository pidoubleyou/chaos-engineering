package pw.chaos.events.persistence;

import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Data
@Entity
public class Registration {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "eventId", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Event event;
}
