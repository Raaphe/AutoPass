package backend.autopass.model.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Ticket - 2024-03-30
 * Raph
 * Ticket entity.
 * AutoPass
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Ticket implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private int id;
    @Column(nullable = false)
    private int ticketAmount;
    @Column(nullable = false)
    private BigDecimal price;
    @Builder.Default
    private boolean isDeleted = false;
    private String stripePriceId;
}
