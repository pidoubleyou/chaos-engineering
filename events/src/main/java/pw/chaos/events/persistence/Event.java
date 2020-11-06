package pw.chaos.events.persistence;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Event {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String name;
  private LocalDateTime start;

  @OneToMany(fetch = FetchType.LAZY)
  private List<Registration> registrations;

  public Registration addRegistration(Registration registration) {
    registrations.add(registration);
    registration.setEvent(this);
    return registration;
  }

  public Event start() {
    if (!isStarted()) {
      start = LocalDateTime.now();
    }
    return this;
  }

  private boolean isStarted() {
    return start != null;
  }
}
