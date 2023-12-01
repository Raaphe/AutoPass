package backend.autopass.model.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

@Getter
@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private int id;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String salt;
    @OneToOne
    @Column(nullable = false)
    private UserWallet wallet;
    @OneToOne
    private Pass pass;
    private boolean isDeleted = false;

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
