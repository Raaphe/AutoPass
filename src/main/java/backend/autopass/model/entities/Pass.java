package backend.autopass.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Entity
@Data
public class Pass implements Serializable {
    @Id
    @GeneratedValue
    private int id;

    @Lob
    private byte[] pass;


    public void setId(int id) {
        this.id = id;
    }

}
