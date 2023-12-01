package backend.autopass.model.repositories;

import backend.autopass.model.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long id);

    @Override
    Page<User> findAll(Pageable users);

    @Override
    boolean existsById(Long id);

    boolean updateUserByIdIs(int id);

}
