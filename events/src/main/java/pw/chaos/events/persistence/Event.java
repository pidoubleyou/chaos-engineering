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
  private LocalDateTime end;

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

  public boolean isStarted() {
    return start != null;
  }

  public Event end() {
    if (!isEnded()) {
      end = LocalDateTime.now();
    }
    return this;
  }

  public boolean isEnded() {
    return end != null;
  }
}
