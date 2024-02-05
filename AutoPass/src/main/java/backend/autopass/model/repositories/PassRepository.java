package backend.autopass.model.repositories;

import backend.autopass.model.entities.Pass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PassRepository extends JpaRepository<Pass, Long> {

    Optional<Pass> getPassById(int id);

    @Override
    boolean existsById(Long id);

}
