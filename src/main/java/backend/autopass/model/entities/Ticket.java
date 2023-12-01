package backend.autopass.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Entity
@Data
public class Ticket implements Serializable {
    @Id
    @GeneratedValue
    private int id;
    private int ticketAmount;
    private double price;
    private boolean isDeleted;

    public Ticket(int id, int ticketAmount, double price, boolean isDeleted) {
        this.id = id;
        this.ticketAmount = ticketAmount;
        this.price = price;
        this.isDeleted = isDeleted;
    }

    public Ticket() {
    }

    public void setId(int id) {

        this.id = id;
    }

}
