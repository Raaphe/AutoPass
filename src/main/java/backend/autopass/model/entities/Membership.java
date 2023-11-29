package backend.autopass.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Data
@Entity
public class Membership implements Serializable {
    @Id
    @GeneratedValue
    private int id;
    private int membershipDurationDays;
    private float price;
    private boolean isDeleted;

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
