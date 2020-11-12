package pw.chaos.tracking.domain;

public class TrackingAlreadyStartedException extends RuntimeException {
  public TrackingAlreadyStartedException(Long eventId, Long registrationId) {
    super(String.format("Event: %d, Registration: %d", eventId, registrationId));
  }
}
