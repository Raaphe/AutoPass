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
    private byte[] password;
    private byte[] salt;
    @OneToOne
    @PrimaryKeyJoinColumn
    private UserWallet wallet;
    @OneToOne
    private Pass pass;
    private boolean isDeleted = false;

    public User(int id, String username, String email, byte[] password, byte[] salt, UserWallet wallet, Pass pass, boolean isDeleted) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.salt = salt;
        this.wallet = wallet;
        this.pass = pass;
        this.isDeleted = isDeleted;
    }

    public User(User user) {
        this.id = user.id;
        this.username = user.username;
        this.email = user.email;
        this.password = user.password;
        this.salt = user.salt;
        this.wallet = user.wallet;
        this.pass = user.pass;
        this.isDeleted = user.isDeleted;
    }

    public User() {
    }

    public void setId(int id) {
        this.id = id;
    }

}
