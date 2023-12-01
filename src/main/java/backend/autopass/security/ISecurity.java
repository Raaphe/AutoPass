package backend.autopass.security;

import backend.autopass.model.entities.Admin;
import backend.autopass.model.entities.User;

public interface ISecurity {

    /**
     * Generates a salt, populates the user object's salt.
     *
     * @param user The target user to generate salt.
     * @return the user object with the salt.
     */
    public User generateUserSalt(User user) throws Exception;

    /**
     * Generates a salt, populates the admin object's salt.
     *
     * @param admin The target admin to generate salt.
     * @return the user object with the salt.
     */
    public Admin generateUserSalt(Admin admin) throws Exception;

    /**
     * Takes a pwd to compare to in the database. Method will apply salt and hash and compare to model value. Only for admins.
     *
     * @param pwd     The raw password.
     * @param adminId The admin id to compare to.
     * @return whether the passwords match.
     */
    public Boolean validateAdminPassword(String pwd, int adminId);

    /**
     * Takes a pwd to compare to in the database. Method will apply salt and hash and compare to model value. Only for users.
     *
     * @param pwd    The raw password.
     * @param userId The user's id to compare to.
     * @return whether the passwords match.
     */
    public Boolean validateUserPassword(String pwd, int userId);

    /**
     * Generates a salt.
     *
     * @return a salt as a string.
     */
    public byte[] generateSalt();

    /**
     * Hashes a given pwd.
     *
     * @param pwd the password to hash.
     * @return Returns the hashed pwd as a string.
     */
    public byte[] hashString(String pwd, byte[] salt);


}
