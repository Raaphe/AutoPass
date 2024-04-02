package backend.autopass.service;

import backend.autopass.model.entities.Membership;
import backend.autopass.model.entities.Ticket;
import backend.autopass.model.repositories.MembershipRepository;
import backend.autopass.model.repositories.TicketRepository;
import backend.autopass.payload.viewmodels.ProductsViewModel;
import backend.autopass.service.interfaces.IProductsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : Lam Nguyen
 * @version : 01
 * Products Service
 * Service class to get all products description for the page of products.
 * 30/03/24
 * AutoPass
 */
@Service
@RequiredArgsConstructor
public class ProductsService implements IProductsService {

    private final MembershipRepository membershipRepository;
    private final TicketRepository ticketRepository;

    @Override
    public ProductsViewModel getAllProducts() {

        List<Membership> memberships = membershipRepository.getAll();
        List<Ticket> tickets = ticketRepository.getAll();

        return ProductsViewModel
                .builder()
                .membershipList(memberships)
                .ticketsList(tickets)
                .build();
    }
}
