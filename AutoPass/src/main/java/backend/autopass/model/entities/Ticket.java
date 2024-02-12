package backend.autopass.model.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Entity
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
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

}
