package backend.autopass.model.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Membership - 2024-03-30
 * Raph
 * Membership entity.
 * AutoPass
 */
@Data
@Entity
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Membership implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private int id;
    @Column(nullable = false)
    private int membershipDurationDays;
    @Column(nullable = false)
    private BigDecimal price;
    @Column(nullable = false)
    @Builder.Default
    private boolean isDeleted = false;
    private String stripePriceId;
}
