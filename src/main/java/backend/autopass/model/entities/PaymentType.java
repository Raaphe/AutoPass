package backend.autopass.model.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Entity
@Data
public class PaymentType implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private int id;
    @ManyToOne
    @Column(nullable = false)
    private User user;
    @Column(nullable = false)
    private String pAN;
    @Column(nullable = false)
    private Date expiryDate;
    @Column(nullable = false)
    private String cVV;
    private boolean isDeleted = false;

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
