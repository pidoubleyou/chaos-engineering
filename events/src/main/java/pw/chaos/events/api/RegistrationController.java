package pw.chaos.events.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pw.chaos.events.domain.RegistrationService;
import pw.chaos.events.persistence.Registration;

import java.util.Optional;

@RestController
public class RegistrationController {

  @Autowired private RegistrationService registrationService;

  @PostMapping(value = "/events/{eventId}/registrations", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<RegistrationModel> register(
      @PathVariable Long eventId, @RequestBody RegistrationModel registrationModel) {

    return registrationService
        .register(eventId, registrationModel.fromModel())
        .map(RegistrationModel::new)
        .map(
            registration -> {
              Link link = registration.getRequiredLink(LinkRelation.of("self"));

              HttpHeaders headers = new HttpHeaders();
              headers.setLocation(link.toUri());
              return new ResponseEntity<RegistrationModel>(headers, HttpStatus.CREATED);
            })
        .orElse(ResponseEntity.badRequest().build());
  }

  @GetMapping(value = "/events/{id}/registrations/{registrationId}")
  public ResponseEntity<RegistrationModel> findById(
      @PathVariable Long id, @PathVariable Long registrationId) {
    Optional<Registration> registration =
        registrationService.findRegistration(id, registrationId);
    return registration
        .map(RegistrationModel::new)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }
}
