package backend.autopass.model.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Entity
@Data
public class Admin implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private int id;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private byte[] password;
    @Column(nullable = false)
    private byte[] salt;

    public Admin(int id, String username, String email, byte[] password, byte[] salt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.salt = salt;
    }

    public Admin(Admin admin) {
        this.id = admin.id;
        this.username = admin.username;
        this.email = admin.email;
        this.password = admin.password;
        this.salt = admin.salt;
    }


    public Admin() {
    }

    public void setId(int id) {
        this.id = id;
    }

}
