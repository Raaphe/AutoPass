package backend.autopass.model.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Raphael Paquin
 * @version 01
 * The Invoice object for payment history.
 * 2024-04-09
 * AutoPass
 */
@Data
@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class Invoice implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal price;
    // This will help link the invoice to the product.
    private String stripePriceId;
    private double date;
    private String stripeCheckoutId;
    @ManyToOne
    private User user;
    private String productName;
    private Boolean processed;

}
