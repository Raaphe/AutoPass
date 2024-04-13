package backend.autopass.model.repositories;

import backend.autopass.model.entities.TransitLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * @author Raphael Paquin
 * @version 01
 * The data-access for transit logs.
 * 2024-04-12
 * AutoPass
 */
@Repository
public interface TransitLogRepository extends JpaRepository<TransitLog, Long> {

    @Query("SELECT tl from TransitLog tl where tl.user.id = :userId")
    Collection<TransitLog> getAllByUserId(int userId);
}
