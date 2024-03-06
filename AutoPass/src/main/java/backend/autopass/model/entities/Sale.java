package backend.autopass.model.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Builder
@Data
@Entity
@Table(name = "sale")
@RequiredArgsConstructor
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, name = "sale_id")
    private long id;
    @ManyToOne
    @JoinColumn(name = "_userId")
    private User userId;
    @Column(nullable = false)
    private String serviceDetails;
    @Column(nullable = false)
    private double totalAmount;
    @Column(nullable = false)
    private long stripPaymentId;
    @Column(nullable = false)
    private String paymentStatus;
    @Column(nullable = false)
    private LocalDateTime paymentDate;
    @Column(nullable = false)
    private String cAD;
    @Column(nullable = false)
    private String userEmail;

    public Sale(long id, User userId, String serviceDetails, double totalAmount, long stripPaymentId, String paymentStatus,LocalDateTime paymentDate, String cAD, String userEmail) {
        this.id = id;
        this.userId = userId;
        this.serviceDetails = serviceDetails;
        this.totalAmount = totalAmount;
        this.stripPaymentId = stripPaymentId;
        this.paymentStatus = paymentStatus;
        this.paymentDate = paymentDate;
        this.cAD = cAD;
        this.userEmail = userEmail;
    }
}
