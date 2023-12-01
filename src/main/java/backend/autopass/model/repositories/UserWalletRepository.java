package backend.autopass.model.repositories;

import backend.autopass.model.entities.UserWallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserWalletRepository extends JpaRepository<UserWallet, Long> {

    Optional<UserWallet> findById(Long id);

    @Override
    Page<UserWallet> findAll(Pageable userWallets);

    @Override
    boolean existsById(Long id);

    boolean updateUserWalletByIdIs(int id);

}
