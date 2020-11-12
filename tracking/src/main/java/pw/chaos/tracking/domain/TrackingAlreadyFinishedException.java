package pw.chaos.tracking.domain;

public class TrackingAlreadyFinishedException extends RuntimeException {
  public TrackingAlreadyFinishedException(Long eventId, Long registrationId) {
    super(String.format("Event: %d, Registration: %d", eventId, registrationId));
  }
}
