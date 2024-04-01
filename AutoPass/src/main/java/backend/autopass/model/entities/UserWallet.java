package backend.autopass.model.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * UserWallet - 2024-03-30
 * Raph
 * User Wallet entity.
 * Not to confuse Google Wallet implementation with this entity.
 * The Google wallet API is used in the app for virtual pass distribution whereas this entity represents the number of tickets and the membership type for a user.
 * AutoPass
 */
@Entity
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class UserWallet implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private int id;
    @Column(nullable = false)
    @Builder.Default
    private int ticketAmount = 0;
    private double memberShipEnds;
    @Builder.Default
    private boolean membershipActive = false;
    @ManyToOne
    private Membership membershipType;
}
