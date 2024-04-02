package backend.autopass.security.jwt.refreshToken;

import backend.autopass.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * TokenRepository - 2024-03-30
 * Raph
 * Provides data-access to refresh tokens.
 * AutoPass
 */
@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByToken(String token);

    Optional<Token> findByUser(User user);

}
