package backend.autopass.model.repositories;

import backend.autopass.model.entities.Membership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long> {
    @Query("select m from Membership m where m.isDeleted = false")
    List<Membership> getAll();
}
