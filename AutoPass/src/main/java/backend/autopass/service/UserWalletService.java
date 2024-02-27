package backend.autopass.service;

import backend.autopass.model.entities.User;
import backend.autopass.model.entities.UserWallet;
import backend.autopass.model.repositories.UserRepository;
import backend.autopass.service.interfaces.IUserWallet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserWalletService implements IUserWallet {

    private final UserRepository userRepository;

    @Override
    public UserWallet getUserWalletInfo(int userId) {
        try {
            Optional<User> user = userRepository.getUserById(userId);
            if (user.isPresent()) {
                if (user.get().isEnabled() && user.get().isAccountNonExpired() && user.get().isAccountNonLocked() && user.get().isCredentialsNonExpired() && !user.get().isDeleted()) {
                    return user.get().getWallet();
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
