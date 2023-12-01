package backend.autopass.model.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.util.Date;

@Getter
@Entity
@Data
public class UserWallet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private int id;
    @Column(nullable = false)
    private int ticketAmount = 0;
    private Date memberShipEnds;
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
