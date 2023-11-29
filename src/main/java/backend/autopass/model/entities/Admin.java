package backend.autopass.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Entity
@Data
public class Admin implements Serializable {
    @Id
    @GeneratedValue
    private int id;
    private String username;
    private String email;
    private String password;
    private String salt;

    public Admin(int id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public Admin() {
    }

    public void setId(int id) {
        this.id = id;
    }

}
