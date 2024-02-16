package backend.autopass.service.interfaces;

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
}
