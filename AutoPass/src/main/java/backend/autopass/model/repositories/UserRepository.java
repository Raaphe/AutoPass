package backend.autopass.model.repositories;

import backend.autopass.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> getUserById(int id);

    Optional<User> findByEmail(String email);
    @Override
    boolean existsById(Long id);

    boolean existsByEmail(String email);

    @Query("SELECT u.profileImageUrl FROM User u where u.id = :id")
    String findProfileImageUrlById(int id);

    @Query("SELECT u FROM User u where u.role = 'SCANNER_USER' and u.isDeleted = false")
    Collection<User> getAllScanners();

    @Query("SELECT u FROM User u WHERE u.role = 'SCANNER_USER' AND u.isDeleted = false AND u.email LIKE CONCAT('%', :busNumber, '@%')")
    User getBusByBusNumber(@Param("busNumber") int busNumber);

}
