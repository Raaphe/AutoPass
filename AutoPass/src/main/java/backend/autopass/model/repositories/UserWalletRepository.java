package backend.autopass.model.repositories;

import backend.autopass.model.entities.UserWallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * UserWalletRepository - 2024-03-30
 * Raph
 * Provides data-access to User Wallets.
 * AutoPass
 */
@Repository
public interface UserWalletRepository extends JpaRepository<UserWallet, Long> {
}
