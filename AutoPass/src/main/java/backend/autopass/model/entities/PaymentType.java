package backend.autopass.model.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * PaymentType - 2024-03-30
 * Raph
 * Payment Type entity.
 * AutoPass
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class PaymentType implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private int id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(nullable = false)
    private String pAN;
    @Column(nullable = false)
    private Date expiryDate;
    @Column(nullable = false)
    private String cVV;
    @Builder.Default
    private boolean isDeleted = false;

}
