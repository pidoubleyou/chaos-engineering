package pw.chaos.events.api;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import pw.chaos.events.persistence.Registration;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class RegistrationModel extends RepresentationModel<RegistrationModel> {
  private Long id;
  private String name;

  public RegistrationModel(Registration registration) {
    setId(registration.getId());
    setName(registration.getName());

    add(
        linkTo(RegistrationController.class).slash("/events/" + registration.getEvent().getId() + "/registrations/" + registration.getId())
            .withSelfRel());
  }

  public Registration fromModel() {
    Registration registration = new Registration();
    registration.setId(getId());
    registration.setName(getName());
    return registration;
  }
}
