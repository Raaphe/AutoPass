package backend.autopass.service.interfaces;

import backend.autopass.model.entities.User;
import backend.autopass.payload.dto.UserDTO;

public interface IUserService {

    /**
     * Creates a user from a user data transfer object. Instantiates a user's pass and userWallet.
     *
     * @param user User's information for user creation.
     * @return An instance of the created user.
     */
    User createUser(UserDTO user) throws Exception;

    /**
     * Creates an administrator user from a user data transfer object. Instantiates a user's pass and userWallet.
     *
     * @param user User's information for admin creation.
     * @return An instance of the created user.
     */
    User createAdmin(UserDTO user) throws Exception;

    /**
     * Fetches from the database an object copy of a given User.
     *
     * @param userId The target user's ID.
     * @return A given user.
     */
    User getUserById(Long userId);

    /**
     * Whether the user exists by email.
     *
     * @param email The target user's email.
     * @return Whether the user exists by email.
     */
    Boolean userExists(String email);

    /**
     * Whether the user exists by id.
     *
     * @param id The target user's id.
     * @return Whether the user exists by id.
     */
    Boolean userExists(Long id);
}
