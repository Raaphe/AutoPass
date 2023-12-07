package backend.autopass.service.impl;

import backend.autopass.model.dto.UserDTO;
import backend.autopass.model.entities.Pass;
import backend.autopass.model.entities.User;
import backend.autopass.model.entities.UserWallet;
import backend.autopass.model.repositories.PassRepository;
import backend.autopass.model.repositories.UserRepository;
import backend.autopass.model.repositories.UserWalletRepository;
import backend.autopass.security.Security;
import backend.autopass.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Security security;

    @Autowired
    private PassRepository passRepository;

    @Autowired
    private UserWalletRepository walletRepository;

    private static void mapDtoToUser(UserDTO dto, User user) {
        user.setEmail(dto.email);
        user.setUsername(dto.username);
    }

    @Override
    public User createUser(UserDTO userDto) throws Exception {

        if (userRepository.existsByEmail(userDto.email)) {
            return null;
        }

        User user = new User();
        mapDtoToUser(userDto, user);

        // Password and salt
        user = security.generateUserSalt(user);
        user.setPassword(security.hashString(userDto.pwd, user.getSalt()));

        // Saving pass + UserWallet
        Pass pass = passRepository.save(new Pass());

        UserWallet wallet = walletRepository.save(new UserWallet());
        user.setPass(pass);
        user.setWallet(wallet);

        // Create User
        return userRepository.save(user);
    }

}
