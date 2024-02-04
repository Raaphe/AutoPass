package backend.autopass.model.repositories;

import backend.autopass.model.entities.UserWallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserWalletRepository extends JpaRepository<UserWallet, Long> {

    Optional<UserWallet> getUserWalletById(int id);

    @Override
    boolean existsById(Long id);
}
