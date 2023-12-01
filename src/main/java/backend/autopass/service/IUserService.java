package backend.autopass.service;

import backend.autopass.model.dto.UserDTO;
import backend.autopass.model.entities.User;

public interface IUserService {

    User createUser(UserDTO user) throws Exception;

}
