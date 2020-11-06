package pw.chaos.tracking.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pw.chaos.tracking.persistence.TrackingRepository;
import pw.chaos.tracking.persistence.Tracking;
import reactor.core.publisher.Mono;

@Service
public class TrackingService {
  @Autowired private TrackingRepository trackingRepository;

  public Mono<Void> start(Tracking[] trackings) {
    for (Tracking tracking : trackings) {
      trackingRepository.save(tracking);
    }

    return Mono.empty();
  }
}
