package backend.autopass.model.repositories;

import backend.autopass.model.entities.Pass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * PassRepository - 2024-03-30
 * Raph
 * Provides data-access to Passes.
 * AutoPass
 */
@Repository
public interface PassRepository extends JpaRepository<Pass, Long> {
}
