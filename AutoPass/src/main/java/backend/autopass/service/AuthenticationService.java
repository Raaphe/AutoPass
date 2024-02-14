package backend.autopass.service;

import backend.autopass.model.entities.User;
import backend.autopass.model.enums.Role;
import backend.autopass.model.enums.TokenType;
import backend.autopass.model.repositories.UserRepository;
import backend.autopass.payload.dto.SignInDTO;
import backend.autopass.payload.dto.SignUpDTO;
import backend.autopass.payload.viewmodels.AuthenticationResponse;
import backend.autopass.service.interfaces.IAuthenticationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationService implements IAuthenticationService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    @Override
    public AuthenticationResponse register(SignUpDTO request) throws Exception {

        try {
            User user;
          
            if (request.getRole() == Role.ADMIN) {
                System.out.println(36);
                user = userService.createAdmin(request);
                System.out.println(38);
            } else if (request.getRole() == Role.USER) {
                System.out.println(40);
                user = userService.createUser(request);
            } else {
                System.out.println(43);
                return null;
            }

            System.out.println(47);
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
}
