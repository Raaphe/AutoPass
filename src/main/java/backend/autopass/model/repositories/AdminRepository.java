package backend.autopass.model.repositories;

import backend.autopass.model.entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> getAdminById(int id);

    @Override
    boolean existsById(Long id);

}
