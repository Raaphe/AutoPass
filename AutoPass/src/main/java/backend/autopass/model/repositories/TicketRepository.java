package backend.autopass.model.repositories;

import backend.autopass.model.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * TicketRepository - 2024-03-30
 * Raph, Lam
 * Provides data-access to Tickets.
 * AutoPass
 */
@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query("select t from Ticket t where t.isDeleted = false")
    List<Ticket> getAll();
}
