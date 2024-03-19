package backend.autopass.service;

import backend.autopass.model.entities.User;
import backend.autopass.model.enums.Role;
import backend.autopass.model.enums.TokenType;
import backend.autopass.model.repositories.UserRepository;
import backend.autopass.payload.dto.SignInDTO;
import backend.autopass.payload.dto.SignUpDTO;
import backend.autopass.payload.viewmodels.AuthenticationResponse;
import backend.autopass.service.interfaces.IAuthenticationService;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Date;
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
    private final JavaMailSender emailSender;

    @Value("${frontend.server.port}")
    private String frontendPort;

    @Override
    public AuthenticationResponse register(SignUpDTO request) {

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

        if (user.getRole() == Role.GOOGLE_USER || user.getGoogleAccessToken() != null) {
            throw new IllegalArgumentException("Cannot authenticate here with a google linked account.");
        }

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
    public Role getRoleFromAccessToken(String accessToken) {

        String email = jwtService.extractUserName(accessToken);
        User user = userService.getUserByEmail(email);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        if (user == null || !jwtService.isTokenValid(accessToken, userDetails)) {
            return null;
        }

        return user.getRole();

    }

    @Override
    public Boolean forgotPassword(String email) {

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message);

        try {
            Optional<User> user = userRepository.findByEmail(email);
            if (user.isEmpty()) {
                return false;
            }

            // Creating a password reset link
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            String accessToken = jwtService.generateToken(userDetails);
            String url = "http://localhost:" + this.frontendPort + "/change-password?token=" + accessToken;

            // Constructing Email
            messageHelper.setFrom("raphaelpaquin19@gmail.com");
            messageHelper.setTo(email);
            message.setText("<!DOCTYPE html> <html> <head> <title>Password Reset</title> <link href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css\" rel=\"stylesheet\"> </head> <body> <div class=\"container\" style=\"margin-top: 20px;\"> <h2>Password Reset Request \uD83D\uDD12</h2> <hr/> <p>To reset your password, please click the button below:</p> <a href='"+ url + "' class=\"btn btn-primary\">Reset Password</a> </div> </body> </html>", "UTF-8", "html");
            message.setSubject("Reset Your AutoPass password 🚏");
            message.setSentDate(Date.from(Instant.now()));
            emailSender.send(message);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
