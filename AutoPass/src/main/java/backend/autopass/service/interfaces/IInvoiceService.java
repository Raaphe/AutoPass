package backend.autopass.service.interfaces;

import backend.autopass.model.entities.Invoice;

import java.util.Collection;

/**
 * @author Raphael Paquin
 * @version 01
 * The invoice service method declarations.
 * 2024-04-11
 * AutoPass
 */
public interface IInvoiceService {

    /**
     * Gets the invoices for a given user.
     * @param userId The target user ID.
     * @return The list of invoices.
     */
    Collection<Invoice> getInvoicesByUserId(int userId);
}
