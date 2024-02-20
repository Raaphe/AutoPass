package backend.autopass.service;

import backend.autopass.model.entities.Pass;
import backend.autopass.model.entities.User;
import backend.autopass.model.entities.UserWallet;
import backend.autopass.model.enums.Role;
import backend.autopass.model.repositories.PassRepository;
import backend.autopass.model.repositories.UserRepository;
import backend.autopass.model.repositories.UserWalletRepository;
import backend.autopass.payload.dto.ChangePasswordDTO;
import backend.autopass.payload.dto.SignUpDTO;
import backend.autopass.payload.dto.UpdateUserDTO;
import backend.autopass.service.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    public User createUser(SignUpDTO signUpDTO) {

        try {
            if (userRepository.existsByEmail(signUpDTO.getEmail())) {
                throw new Exception("Email already in use");
            }

            User user = this.buildUser(signUpDTO);
            user.setRole(Role.USER);

            return userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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


    public void deleteUser(Long userId) throws Exception {
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

    public boolean updateUser(UpdateUserDTO updateDto){
        Optional<User> userOptional = userRepository.findByEmail(updateDto.getEmail());
        try {
            if (userOptional.isEmpty()) {
                return false;
            } else {
                User user = userOptional.get();
                user.setEmail(updateDto.getEmail());
                user.setFirstName(updateDto.getFirstName());
                user.setLastName(updateDto.getLastName());
                user.setPassword(updateDto.getPassword());
                userRepository.save(user);
                return true;
            }
        }catch (Exception userMess){
            return false;

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

    @Override
    public Boolean changePassword(ChangePasswordDTO dto) {
        String email = jwtService.extractUserName(dto.getToken());

        if (this.userExists(email)) {
            Optional<User> user = this.userRepository.findByEmail(email);
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            if (user.isPresent() && jwtService.isTokenValid(dto.getToken(), userDetails)) {
                User concreteUser = user.get();
                concreteUser.setPassword(passwordEncoder.encode(dto.getPassword()));
                concreteUser = userRepository.save(concreteUser);
                return passwordEncoder.matches(dto.getPassword(), concreteUser.getPassword());
            }
        }
        return false;
    }


    private User buildUser(SignUpDTO signUpDTO) {

        Pass pass = passRepository.save(new Pass());
        UserWallet wallet = walletRepository.save(UserWallet.builder().membershipActive(false).ticketAmount(0).build());

        User user = User.builder()
                .email(signUpDTO.getEmail())
                .firstName(signUpDTO.getFirstname())
                .lastName(signUpDTO.getLastname())
                .password(passwordEncoder.encode(signUpDTO.getPassword()))
                .pass(pass)
                .wallet(wallet)
                .build();

        return userRepository.save(user);
    }

}
