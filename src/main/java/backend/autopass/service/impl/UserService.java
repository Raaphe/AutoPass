package backend.autopass.service.impl;

import backend.autopass.model.dto.UserDTO;
import backend.autopass.model.entities.Pass;
import backend.autopass.model.entities.Role;
import backend.autopass.model.entities.User;
import backend.autopass.model.entities.UserWallet;
import backend.autopass.model.repositories.PassRepository;
import backend.autopass.model.repositories.UserRepository;
import backend.autopass.model.repositories.UserWalletRepository;
import backend.autopass.security.Security;
import backend.autopass.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;

    private final Security security;

    private final PassRepository passRepository;

    private final UserWalletRepository walletRepository;

    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Username Not Found"));
            }
        };
    }

    @Override
    public User createUser(UserDTO userDto) throws Exception {

        if (userRepository.existsByEmail(userDto.email)) {
            throw new Exception("Email already in use");
        }

        User user = this.buildUser(userDto);
        user.setRole(Role.ROLE_USER);

        return userRepository.save(user);
    }

    @Override
    public User createAdmin(UserDTO userDto) throws Exception {

        if (userRepository.existsByEmail(userDto.email)) {
            throw new Exception("Email already in use");
        }

        User user = this.buildUser(userDto);
        user.setRole(Role.ROLE_ADMIN);

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


    private User buildUser(UserDTO userDTO) throws Exception {

        // pass + userWallet
        Pass pass = passRepository.save(new Pass());
        UserWallet wallet = walletRepository.save(new UserWallet());

        User user = User.builder()
                .email(userDTO.email)
                .name(userDTO.username)
                .pass(pass)
                .wallet(wallet)
                .build();

        // Password and salt
        user = security.generateUserSalt(user);
        user.setPassword(security.hashString(userDTO.pwd, user.getSalt()));

        return user;
    }

}
