package backend.autopass.model.repositories;

import backend.autopass.model.entities.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * @author Raphael Paquin
 * @version 01
 * Provides data-access to Invoices.
 * 2024-04-10
 * AutoPass
 */
@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Invoice getInvoiceByStripeCheckoutId(String stripeCheckoutId);

    Boolean existsByStripeCheckoutId(String stripeCheckoutId);

    @Query("SELECT i FROM Invoice i where i.user.id = :userId")
    Collection<Invoice> getAllByUserId(int userId);

}
