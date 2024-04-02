package backend.autopass.model.entities;

import backend.autopass.model.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;

/**
 * User - 2024-03-30
 * Raph
 * User entity.
 * AutoPass
 */
@Builder
@Data
@Entity
@Table(name = "_user")
@RequiredArgsConstructor
@AllArgsConstructor
public class User implements UserDetails, Serializable {
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, name = "user_id")
    private int id;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @OneToOne
    private UserWallet wallet;
    @OneToOne
    private Pass pass;
    @Builder.Default
    private boolean isDeleted = false;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String googleAccessToken;
    private String profileImageUrl;
    private Boolean isProfileImageChanged;
    private Boolean isGoogleWalletPassAdded = false;


    @PrePersist
    protected void onCreate() {
        if (isProfileImageChanged == null) {
            isProfileImageChanged = false;
        }

        if (isGoogleWalletPassAdded == null) {
            isGoogleWalletPassAdded = false;
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
