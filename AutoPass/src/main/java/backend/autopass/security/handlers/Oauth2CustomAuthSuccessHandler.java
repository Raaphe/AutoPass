package backend.autopass.security.handlers;

import backend.autopass.model.entities.User;
import backend.autopass.payload.viewmodels.AuthenticationResponse;
import backend.autopass.security.jwt.refreshToken.Token;
import backend.autopass.service.JwtService;
import backend.autopass.service.RefreshTokenService;
import backend.autopass.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class Oauth2CustomAuthSuccessHandler implements AuthenticationSuccessHandler {

    private final RefreshTokenService refreshTokenService;
    private final UserService userService;
    private final JwtService jwtService;

    @Value("${application.ip}")
    private String appIp;

    @Value("${frontend.server.port}")
    private String frontendAppPort;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        log.info("Handling successful google logon.");

        // The 'authentication' parameter contains the principal details
        Object principal = authentication.getPrincipal();
        AuthenticationResponse tokens = new AuthenticationResponse();

        String username;
        if (principal instanceof OAuth2User oAuth2User) {

            Map<String, Object> attributes = oAuth2User.getAttributes();
            username = (String) attributes.get("email");
            UserDetails user = userService.loadUserByUsername(username);

            tokens = this.setUserInSecurityContext(user, request);
        } else {
            log.error("==== Could not set user in authentication context. ====");
        }

        String baseUrl = "http://" + this.appIp + ":" + this.frontendAppPort + "/login";
        String redirectUrl = String.format("%s?accessToken=%s&tokenType=%s&id=%d&refreshToken=%s",
                baseUrl,
                URLEncoder.encode(tokens.getAccessToken(), StandardCharsets.UTF_8),
                URLEncoder.encode(tokens.getTokenType(), StandardCharsets.UTF_8),
                tokens.getId(),
                URLEncoder.encode(tokens.getRefreshToken(), StandardCharsets.UTF_8));

        response.sendRedirect(redirectUrl);
    }

    public AuthenticationResponse setUserInSecurityContext(UserDetails userDetails, HttpServletRequest request) {
        User user = userService.getUserByEmail(userDetails.getUsername());
        if (user.getEmail() != null) {
            // Creating tokens.
            Token refreshToken = refreshTokenService.createRefreshToken((long) user.getId());
            String accessToken = jwtService.generateToken(userDetails);

            // Setting in context.
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);

            // Returning an object with tokens that will be saved in client.
            log.error("==== Setting Google user : " + userDetails.getUsername() + " in context. ====");
            return AuthenticationResponse
                    .builder()
                    .accessToken(accessToken)
                    .tokenType("Bearer")
                    .id((long) user.getId())
                    .refreshToken(refreshToken.getToken())
                    .build();
        }
        log.error("==== Could not set user in authentication context. ====");
        return null;
    }


}
