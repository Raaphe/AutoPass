package backend.autopass.model.repositories;

import backend.autopass.model.entities.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

/**
 * PaymentTypeRepository - 2024-03-30
 * Raph, Lam
 * Provides data-access to Payment Types.
 * AutoPass
 */
@Repository
public interface PaymentTypeRepository extends JpaRepository<PaymentType, Long> {

    Optional<PaymentType> getPaymentTypeById(int id);

    Collection<PaymentType> getAllByUserId(int userId);

}
