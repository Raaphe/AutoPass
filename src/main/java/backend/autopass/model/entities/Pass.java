package backend.autopass.model.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pass implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private int id;

    @Lob
    private byte[] pass;

    public void setId(int id) {
        this.id = id;
    }

}
