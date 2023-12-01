package backend.autopass.service;

import backend.autopass.model.entities.User;
import backend.autopass.model.viewmodels.UserViewModel;

public interface IUserService {

    public User createUser(UserViewModel userViewModel);

}
