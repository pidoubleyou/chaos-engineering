package pw.chaos.events.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pw.chaos.events.persistence.Registration;
import pw.chaos.events.persistence.RegistrationRepository;

import java.util.Optional;

@Service
public class RegistrationService {

  @Autowired private EventService eventService;
  @Autowired private RegistrationRepository registrationRepository;

  public Optional<Registration> register(Long eventId, Registration registration) {
    return eventService
        .findEvent(eventId)
        .filter(event -> !event.isStarted())
        .map(
            event -> {
              event.addRegistration(registration);
              registrationRepository.save(registration);
              return registration;
            });
  }

  public Optional<Registration> findRegistration(Long eventId, Long registrationId) {
    return eventService
        .findEvent(eventId)
        .flatMap(event -> registrationRepository.findById(registrationId));
  }
}
