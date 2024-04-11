package backend.autopass.service;

import backend.autopass.model.entities.Invoice;
import backend.autopass.model.entities.User;
import backend.autopass.model.repositories.InvoiceRepository;
import backend.autopass.model.repositories.UserRepository;
import backend.autopass.service.interfaces.IInvoiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

/**
 * @author Raphael Paquin
 * @version 01
 * The invoice service method implementation.
 * 2024-04-11
 * AutoPass
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class InvoiceService implements IInvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final UserRepository userRepository;

    @Override
    public Collection<Invoice> getInvoicesByUserId(int userId) {
        Optional<User> userOptional = userRepository.getUserById(userId);

        if (userOptional.isPresent()) {
                return invoiceRepository.getAllByUserId(userOptional.get().getId());
        } else {
            return new ArrayList<>();
        }
    }
}
