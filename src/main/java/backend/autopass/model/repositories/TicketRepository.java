package backend.autopass.model.repositories;

import backend.autopass.model.entities.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Optional<Ticket> getTicketById(int id);

    Page<Ticket> getAllById(Pageable tickets);

    @Override
    boolean existsById(Long id);
}
