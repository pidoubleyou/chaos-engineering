package pw.chaos.tracking.persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackingRepository extends ReactiveCrudRepository<Tracking, Long> {
}
