package pw.chaos.events.persistence;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Event {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String name;

  @OneToMany(fetch = FetchType.LAZY)
  private List<Registration> registrations;

  public Registration addRegistration(Registration registration) {
    registrations.add(registration);
    registration.setEvent(this);
    return registration;
  }
}
