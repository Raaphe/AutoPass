package backend.autopass.model.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Entity
@Data
public class Ticket implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private int id;
    @Column(nullable = false)
    private int ticketAmount;
    @Column(nullable = false)
    private double price;
    private boolean isDeleted = false;

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
