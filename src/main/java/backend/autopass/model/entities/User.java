package backend.autopass.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.Getter;

@Getter
@Entity
@Data
public class User {
    @Id
    @GeneratedValue
    private int id;
    private String username;
    private String email;
    private String password;
    private String salt;
    @OneToOne
    private UserWallet wallet;
    @OneToOne
    private Pass pass;
    private boolean isDeleted;

    public User(int id, String username, String email, String password, String salt, UserWallet wallet, Pass pass, boolean isDeleted) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.salt = salt;
        this.wallet = wallet;
        this.pass = pass;
        this.isDeleted = isDeleted;
    }

    public User() {
    }

    public void setId(int id) {
        this.id = id;
    }

}
