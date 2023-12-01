package backend.autopass.service.impl;

import backend.autopass.model.entities.User;
import backend.autopass.model.repositories.UserRepository;
import backend.autopass.model.viewmodels.UserViewModel;
import backend.autopass.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public User createUser(UserViewModel userViewModel) {
        return null;
    }
}
