package backend.autopass.service.interfaces;

import backend.autopass.model.enums.Role;
import backend.autopass.payload.dto.SignInDTO;
import backend.autopass.payload.dto.SignUpDTO;
import backend.autopass.payload.viewmodels.AuthenticationResponse;

public interface IAuthenticationService {
    /**
     * Registers a user from a sign-up request.
     *
     * @param request The user's credentials and details.
     * @return an Authentication response with access and refresh tokens as well as token type and user email.
     * @throws Exception Error occurred.
     */
    AuthenticationResponse register(SignUpDTO request) throws Exception;

    /**
     * Logs in a user depending on their credentials.
     *
     * @param request The user's credentials.
     * @return an Authentication response with access and refresh tokens as well as token type and user email.
     */
    AuthenticationResponse authenticate(SignInDTO request);

    /**
     * Gets a user's role from their access token.
     * @param accessToken The access token.
     * @return The role.
     */
    Role getRoleFromAccessToken(String accessToken);

    /**
     * Sends the user an email that will allow them to change their password.
     *
     * @param email The target user's email.
     * @return Whether the email has successfully been sent or not.
     */
    Boolean forgotPassword(String email);

    /**
     * Gets the application's currently running ip.
     * This is used in the frontend to know what the ip of the backend is.
     * Ensures that the frontend can dynamically make manual calls to the server without
     * just stating `localhost`.
     * @return The desired IP address.
     */
    String getAppIp();
}
