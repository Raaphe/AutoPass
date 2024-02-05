package backend.autopass.model.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.Date;

@Getter
@Entity
@Data
@Builder
public class UserWallet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private int id;
    @Column(nullable = false)
    @Builder.Default
    private int ticketAmount = 0;
    private Date memberShipEnds;
    @Builder.Default
    private boolean membershipActive = false;

    public UserWallet(int id, int ticketAmount, Date memberShipEnds, boolean membershipActive) {
        this.id = id;
        this.ticketAmount = ticketAmount;
        this.memberShipEnds = memberShipEnds;
        this.membershipActive = membershipActive;
    }

    public UserWallet() {
    }

    public void setId(int id) {
        this.id = id;
    }

}
