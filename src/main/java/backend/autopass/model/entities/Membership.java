package backend.autopass.model.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Data
@Entity
public class Membership implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private int id;
    @Column(nullable = false)
    private int membershipDurationDays;
    @Column(nullable = false)
    private float price;
    @Column(nullable = false)
    private boolean isDeleted = false;

    public Membership(int id, int membershipDurationDays, float price, boolean isDeleted) {
        this.id = id;
        this.membershipDurationDays = membershipDurationDays;
        this.price = price;
        this.isDeleted = isDeleted;
    }

    public Membership() {
    }

    public void setId(int id) {
        this.id = id;
    }

}
