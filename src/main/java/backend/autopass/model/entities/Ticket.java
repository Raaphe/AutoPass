package backend.autopass.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Entity
@Data
@Builder
@AllArgsConstructor
public class Ticket implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private int id;
    @Column(nullable = false)
    private int ticketAmount;
    @Column(nullable = false)
    private double price;

    @Builder.Default
    private boolean isDeleted = false;

    public Ticket() {
    }

    public void setId(int id) {
        this.id = id;
    }
}
