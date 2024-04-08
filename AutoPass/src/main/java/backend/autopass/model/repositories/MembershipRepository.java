package backend.autopass.model.repositories;

import backend.autopass.model.entities.Membership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * MembershipRepository - 2024-03-30
 * Raph
 * Provides data-access to Membership Repository.
 * AutoPass
 */
@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long> {
    @Query("select m from Membership m where m.isDeleted = false")
    List<Membership> getAll();

    Membership getMembershipByStripePriceId(String priceId);
}
