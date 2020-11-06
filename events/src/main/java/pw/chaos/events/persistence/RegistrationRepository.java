package pw.chaos.events.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationRepository extends CrudRepository<Registration, Long> {}
