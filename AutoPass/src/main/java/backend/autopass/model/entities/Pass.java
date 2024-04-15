package backend.autopass.model.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

/**
 * Pass - 2024-03-30
 * Raph
 * Pass entity.
 * AutoPass
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pass implements Serializable {
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private int id;
    /**
     * If we ever choose to have a unique hex key for each Google wallet pass, it could be saved here.
     */
    private String googlePassHexKey;
}
