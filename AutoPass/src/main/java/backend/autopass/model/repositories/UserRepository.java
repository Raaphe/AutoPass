package backend.autopass.model.repositories;

import backend.autopass.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> getUserById(int id);

    Optional<User> findUserByEmail(String email);
    @Override
    boolean existsById(Long id);

    boolean existsByEmail(String email);

}
