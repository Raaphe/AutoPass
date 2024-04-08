package backend.autopass.service.interfaces;

import backend.autopass.model.entities.User;
import backend.autopass.payload.dto.BasicUserInfoDTO;
import backend.autopass.payload.dto.ChangeImageDTO;
import backend.autopass.payload.dto.ChangePasswordDTO;
import backend.autopass.payload.dto.SignUpDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.stream.Stream;

/**
 * IUserService - 2024-03-30
 * Raph
 * Service interface for User Service.
 * AutoPass
 */
public interface IUserService {

    /**
     * Creates a user from a user data transfer object. Instantiates a user's pass and userWallet.
     *
     * @param user User's information for user creation.
     * @return An instance of the created user.
     */
    User createUser(SignUpDTO user) throws Exception;

    /**
     * Creates an administrator user from a user data transfer object. Instantiates a user's pass and userWallet.
     *
     * @param userDto User's information for admin creation.
     * @return An instance of the created user.
     */
    User createAdmin(SignUpDTO userDto) throws Exception;

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

    /**
     * Changes a user's password.
     *
     * @param dto The user's username and new password.
     * @return Whether the password was successfully changed.
     */
    Boolean changePassword(ChangePasswordDTO dto);

    /**
     * Gets a User entity by an email or default.
     * @param email The target user's email.
     * @return A User entity.
     */
    User getUserByEmail(String email);

    /**
     * Takes in an image and saves it in s3 bucket.
     * @param dto The image to save and the user id.
     * @return The Url of the saved image.
     */
    String saveImageToUser(ChangeImageDTO dto) throws IOException;

    /**
     * Gets an image url from the database.
     * @param userId The user id to get from.
     * @return The Url of the saved image.
     */
    String getImageFromUser(int userId);

    /**
     * Saves the basic user's info.
     * @param info The user's info.
     * @return The user information.
     */
    User saveBasicUserInfo(BasicUserInfoDTO info);
}
