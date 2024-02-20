package backend.autopass.service;

import backend.autopass.model.entities.User;
import backend.autopass.model.enums.Role;
import backend.autopass.model.enums.TokenType;
import backend.autopass.model.repositories.UserRepository;
import backend.autopass.payload.dto.SignInDTO;
import backend.autopass.payload.dto.SignUpDTO;
import backend.autopass.payload.viewmodels.AuthenticationResponse;
import backend.autopass.service.interfaces.IAuthenticationService;
import com.resend.Resend;
import com.resend.services.emails.model.SendEmailRequest;
import com.resend.services.emails.model.SendEmailResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationService implements IAuthenticationService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;
    private final UserDetailsService userDetailsService;

    @Value("${resend.api.key}")
    private String resendAPIKey;

    @Value("${frontend.server.port}")
    private String frontendPort;

    @Override
    public AuthenticationResponse register(SignUpDTO request) throws Exception {

        try {
            User user;
          
            if (request.getRole() == Role.ADMIN) {
                user = userService.createAdmin(request);
            } else if (request.getRole() == Role.USER) {
                user = userService.createUser(request);
            } else {
                return null;
            }

            var jwt = jwtService.generateToken(user);
            var refreshToken = refreshTokenService.createRefreshToken((long) user.getId());

            var roles = user.getRole().getAuthorities()
                    .stream()
                    .map(SimpleGrantedAuthority::getAuthority)
                    .toList();

            return AuthenticationResponse.builder()
                    .accessToken(jwt)
                    .email(user.getEmail())
                    .id((long) user.getId())
                    .refreshToken(refreshToken.getToken())
                    .roles(roles)
                    .tokenType(TokenType.BEARER.name())
                    .build();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public AuthenticationResponse authenticate(SignInDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        var roles = user.getRole().getAuthorities()
                .stream()
                .map(SimpleGrantedAuthority::getAuthority)
                .toList();
        var jwt = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken((long) user.getId());
        return AuthenticationResponse.builder()
                .accessToken(jwt)
                .roles(roles)
                .email(user.getEmail())
                .id((long) user.getId())
                .refreshToken(refreshToken.getToken())
                .tokenType(TokenType.BEARER.name())
                .build();
    }

    @Override
    public Boolean forgotPassword(String email) {
        SendEmailResponse data;
        try {
            Optional<User> user = userRepository.findByEmail(email);
            if (user.isEmpty()) {
                return false;
            }

            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            String accessToken = jwtService.generateToken(userDetails);

            Resend resend = new Resend(resendAPIKey);
            String url = "http://localhost:" + this.frontendPort + "/change-password?token=" + accessToken;

            SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
                    .from("onboarding@resend.dev")
                    .to(email)
                    .subject("Reset your AutoPass account password 🚏")
                    .html("<p>Congrats on sending your <strong>first email</strong>!</p><a href=\"" + url + "\">Change Password</a>")
                    .build();

            data = resend.emails().send(sendEmailRequest);

        } catch (Exception e) {
            return false;
        }
        return data.getId() != null;
    }
}
