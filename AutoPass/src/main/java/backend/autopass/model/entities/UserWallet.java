package backend.autopass.model.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

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
