package backend.autopass.security;

import backend.autopass.model.entities.User;
import backend.autopass.model.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Optional;

@Service
public class Security implements ISecurity {

    private final UserRepository userRepository;


    @Autowired
    public Security(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User generateUserSalt(User user) throws Exception {
        User tempUser = new User(user);
        tempUser.setSalt(this.generateSalt());
        assert tempUser.getSalt() != null;
        return tempUser;
    }

    @Override
    public Boolean validateUserPassword(String pwd, int userId) {
        Optional<User> optionalUser = userRepository.findById((long) userId);
        if (optionalUser.isEmpty()) {
            return false;
        }

        User user = optionalUser.get();
        String hashedInput = Arrays.toString(hashString(pwd, user.getSalt()));
        return hashedInput.equals(user.getPassword());
    }

    @Override
    public byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    @Override
    public byte[] hashString(String pwd, byte[] salt) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hash Algorithm not found");
        }

        digest.update(salt);
        return digest.digest(pwd.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
