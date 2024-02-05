package backend.autopass.service.interfaces;

import backend.autopass.payload.dto.SignInDTO;
import backend.autopass.payload.dto.SignUpDTO;
import backend.autopass.payload.viewmodels.AuthenticationResponse;

public interface IAuthenticationService {
    AuthenticationResponse register(SignUpDTO request) throws Exception;

    AuthenticationResponse authenticate(SignInDTO request);
}
