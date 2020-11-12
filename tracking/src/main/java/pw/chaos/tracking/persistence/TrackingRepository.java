package pw.chaos.tracking.persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface TrackingRepository extends ReactiveCrudRepository<Tracking, Long> {
  Mono<Tracking> findByEventIdAndRegistrationId(Long eventId, Long registrationId);
}
