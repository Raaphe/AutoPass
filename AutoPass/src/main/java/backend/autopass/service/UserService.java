package backend.autopass.service;

import backend.autopass.model.entities.Pass;
import backend.autopass.model.entities.User;
import backend.autopass.model.entities.UserWallet;
import backend.autopass.model.enums.Role;
import backend.autopass.model.repositories.PassRepository;
import backend.autopass.model.repositories.UserRepository;
import backend.autopass.model.repositories.UserWalletRepository;
import backend.autopass.payload.dto.SignUpDTO;
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
    public User createUser(SignUpDTO signUpDTO) throws Exception {

        if (userRepository.existsByEmail(signUpDTO.getEmail())) {
            throw new Exception("Email already in use");
        }

        User user = this.buildUser(signUpDTO);
        user.setRole(Role.USER);

        return userRepository.save(user);
    }

    @Override
    public User createAdmin(SignUpDTO signUpDTO) throws Exception {

        if (userRepository.existsByEmail(signUpDTO.getEmail())) {
            throw new Exception("Email already in use");
        }

        User user = this.buildUser(signUpDTO);
        user.setRole(Role.ADMIN);

        return userRepository.save(user);
    }


    public void markUserAsDeleted(Long userId) throws Exception {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new Exception("User not found");
        }
        User user = userOptional.get();
        if (!user.isDeleted()) {
            user.setDeleted(true);
            userRepository.save(user);
        } else {
            throw new Exception("User is already marked as deleted");
        }
    }

    public void updateUser(Long id, String newFirstName, String newLastname, String newEmail) throws Exception{
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isEmpty()){
            throw new Exception("User not found.");
        }else{
            User user = userOptional.get();
            user.setEmail(newEmail);
            user.setFirstName(newFirstName);
            user.setLastName(newLastname);
            userRepository.save(user);
        }
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


    private User buildUser(SignUpDTO signUpDTO) {

        Pass pass = passRepository.save(new Pass());
        UserWallet wallet = walletRepository.save(UserWallet.builder().membershipActive(false).ticketAmount(0).build());

        User user = User.builder()
                .email(signUpDTO.getEmail())
                .firstName(signUpDTO.getFirstname())
                .lastName(signUpDTO.getLastname())
                .password(passwordEncoder.encode(signUpDTO.getPassword()))
                .wallet(wallet)
                .build();

        user.setPass(pass);

        return userRepository.save(user);
    }

}
