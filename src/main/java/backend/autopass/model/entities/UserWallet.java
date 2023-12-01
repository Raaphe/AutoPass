package backend.autopass.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;

import java.util.Date;

@Getter
@Entity
@Data
public class UserWallet {
    @Id
    @GeneratedValue
    private int id;
    private int ticketAmount;
    private Date memberShipEnds;
    private boolean membershipActive;

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
