package pw.chaos.tracking.domain;

public class TrackingNotStartedException extends RuntimeException {
  public TrackingNotStartedException(Long eventId, Long registrationId) {
    super(String.format("Event: %d, Registration: %d", eventId, registrationId));
  }
}
