package backend.autopass.security.jwt.filter;

import backend.autopass.security.config.SecurityConfig;
import backend.autopass.security.jwt.refreshToken.Token;
import backend.autopass.service.JwtService;
import backend.autopass.service.RefreshTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static org.apache.logging.log4j.util.Strings.isNotEmpty;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userService;
    private final RefreshTokenService refreshTokenService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // try to get JWT in cookie or in Authorization Header
        String jwt = jwtService.getJwtFromCookies(request);
        final String authHeader = request.getHeader("Authorization");

        String requestPath = request.getRequestURI();

        // List the paths that should not require authentication
        List<String> authFreeEndpoints = List.of(SecurityConfig.WHITE_LIST_URL);

        if (authFreeEndpoints.stream().anyMatch(path -> requestPath.matches(path.replace("**", ".*")))) {
            filterChain.doFilter(request, response);
            return;
        }

        // Proceed with extracting the JWT from the Authorization header if present
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7); // Extract the token
        } else {
            jwt = jwtService.getJwtFromCookies(request);
        }

        if (jwt != null) {
            final String userEmail = jwtService.extractUserName(jwt);
            if (isNotEmpty(userEmail) && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails;
                userDetails = this.userService.loadUserByUsername(userEmail);

                // Get Refresh token
                // For refresh token logic, the filter will only let the request pass if refresh token is valid.
                // It assumes some other part of the application will handle the generation of a new access token.
                boolean isRefreshTokenValid;
                Token refreshToken = refreshTokenService.getTokenFromUserDetails(userDetails);
                isRefreshTokenValid = !refreshTokenService.isTokenExpired(refreshToken);


                if (jwtService.isTokenValid(jwt, userDetails) || isRefreshTokenValid) {
                    // Set authentication in the context
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }

        filterChain.doFilter(request, response);

    }
}
