package backend.autopass.service;

import backend.autopass.model.entities.Pass;
import backend.autopass.model.entities.User;
import backend.autopass.model.entities.UserWallet;
import backend.autopass.model.enums.Role;
import backend.autopass.model.repositories.PassRepository;
import backend.autopass.model.repositories.UserRepository;
import backend.autopass.model.repositories.UserWalletRepository;
import backend.autopass.payload.dto.UserDTO;
import backend.autopass.service.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final PassRepository passRepository;
    private final UserWalletRepository walletRepository;


    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Username Not Found"));
    }

    @Override
    public User createUser(UserDTO userDto) throws Exception {

        if (userRepository.existsByEmail(userDto.email)) {
            throw new Exception("Email already in use");
        }

        User user = this.buildUser(userDto);
        user.setRole(Role.USER);

        return userRepository.save(user);
    }

    @Override
    public User createAdmin(UserDTO userDto) throws Exception {

        if (userRepository.existsByEmail(userDto.email)) {
            throw new Exception("Email already in use");
        }

        User user = this.buildUser(userDto);
        user.setRole(Role.ADMIN);

        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long userId) {
        Optional<User> user = userRepository.getUserById(Math.toIntExact(userId));
        return user.orElse(null);
    }

    @Override
    public Boolean userExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Boolean userExists(Long id) {
        return userRepository.existsById(id);
    }


    private User buildUser(UserDTO userDTO) {
        // Create pass and wallet
        Pass pass = passRepository.save(new Pass());
        UserWallet wallet = walletRepository.save(UserWallet.builder().membershipActive(false).ticketAmount(0).build());

        // Build the user with the hashed password
        return User.builder()
                .email(userDTO.email)
                .firstName(userDTO.firstName)
                .lastName(userDTO.lastName)
                .password(passwordEncoder.encode(userDTO.pwd)) // Set the hashed password
                .pass(pass)
                .wallet(wallet)
                .build();
    }

}
