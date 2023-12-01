package backend.autopass.model.repositories;

import backend.autopass.model.entities.PaymentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PaymentTypeRepository extends JpaRepository<PaymentType, Long> {

    Optional<PaymentType> findById(Long id);

    @Override
    Page<PaymentType> findAll(Pageable paymentTypes);

    @Override
    boolean existsById(Long id);

    boolean updatePaymentTypeByIdIs(int id);

}
