package backend.autopass.security;

import backend.autopass.model.entities.User;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface ISecurity {

    /**
     * Generates a salt, populates the user object's salt.
     *
     * @param user The target user to generate salt.
     * @return the user object with the salt.
     */
    User generateUserSalt(User user) throws Exception;

    /**
     * Takes a pwd to compare to in the database. Method will apply salt and hash and compare to model value. Only for users.
     *
     * @param pwd    The raw password.
     * @param userId The user's id to compare to.
     * @return whether the passwords match.
     */
    Boolean validateUserPassword(String pwd, int userId);

    /**
     * Generates a salt.
     *
     * @return a salt as a string.
     */
    byte[] generateSalt();

    /**
     * Hashes a given pwd.
     *
     * @param pwd the password to hash.
     * @return Returns the hashed pwd as a string.
     */
    byte[] hashString(String pwd, byte[] salt);

    /**
     * Generates the password encoder.
     *
     * @return the password encoder.
     */
    @Bean
    PasswordEncoder passwordEncoder();


}
