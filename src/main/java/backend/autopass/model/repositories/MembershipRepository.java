package backend.autopass.model.repositories;

import backend.autopass.model.entities.Membership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long> {

    Optional<Membership> getMembershipById(int id);

    @Override
    boolean existsById(Long id);

}
