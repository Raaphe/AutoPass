package backend.autopass.model.repositories;

import backend.autopass.model.entities.Pass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PassRepository extends JpaRepository<Pass, Long> {

    Optional<Pass> findById(Long id);

    @Override
    Page<Pass> findAll(Pageable passes);

    @Override
    boolean existsById(Long id);

    boolean updatePassByIdIs(int id);

}
