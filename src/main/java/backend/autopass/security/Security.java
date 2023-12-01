package backend.autopass.security;

import backend.autopass.model.entities.Admin;
import backend.autopass.model.entities.User;
import backend.autopass.model.repositories.AdminRepository;
import backend.autopass.model.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

@Service
public class Security implements ISecurity {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    @Autowired
    public Security(UserRepository userRepository, AdminRepository adminRepository) {
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
    }


    @Override
    public User generateUserSalt(User user) throws Exception {
        User tempUser = new User(user);
        tempUser.setSalt(this.generateSalt());
        assert tempUser.getSalt() != null;
        return tempUser;
    }

    @Override
    public Admin generateUserSalt(Admin admin) throws Exception {
        Admin tempAdmin = new Admin(admin);
        tempAdmin.setSalt(this.generateSalt());
        assert tempAdmin.getSalt() != null;
        return tempAdmin;
    }

    @Override
    public Boolean validateAdminPassword(String pwd, int adminId) {
        Admin admin;
        if (adminRepository.getAdminById(adminId).isEmpty()) {
            return false;
        }

        admin = adminRepository.getAdminById(adminId).get();
        byte[] hashedInput = hashString(pwd, admin.getSalt());
        return Arrays.equals(hashedInput, admin.getPassword());
    }


    @Override
    public Boolean validateUserPassword(String pwd, int userId) {
        User user;
        if (userRepository.getUserById(userId).isEmpty()) {
            return false;
        }

        user = userRepository.getUserById(userId).get();
        byte[] hashedInput = hashString(pwd, user.getSalt());
        return Arrays.equals(hashedInput, user.getPassword());
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
}
