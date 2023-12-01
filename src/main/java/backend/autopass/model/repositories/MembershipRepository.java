package backend.autopass.model.repositories;

import backend.autopass.model.entities.Membership;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long> {

    Optional<Membership> findById(Long id);

    @Override
    Page<Membership> findAll(Pageable memberships);

    @Override
    boolean existsById(Long id);

    boolean updateMembershipByIdIs(int id);

}
