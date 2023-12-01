package backend.autopass.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Entity
@Data
public class PaymentType implements Serializable {
    @Id
    @GeneratedValue
    private int id;
    @ManyToOne
    private User user;
    private String pAN;
    private Date expiryDate;
    private String cVV;
    private boolean isDeleted;

    public PaymentType(int id, User user, String pAN, Date expiryDate, String cVV, boolean isDeleted) {
        this.id = id;
        this.user = user;
        this.pAN = pAN;
        this.expiryDate = expiryDate;
        this.cVV = cVV;
        this.isDeleted = isDeleted;
    }

    public PaymentType() {
    }

    public void setId(int id) {
        this.id = id;
    }

}
