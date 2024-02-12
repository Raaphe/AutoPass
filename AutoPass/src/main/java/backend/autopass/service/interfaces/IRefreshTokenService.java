package backend.autopass.service.interfaces;

import backend.autopass.model.entities.User;
import backend.autopass.payload.dto.RefreshTokenDTO;
import backend.autopass.payload.viewmodels.RefreshTokenResponse;
import backend.autopass.security.jwt.refreshToken.Token;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface IRefreshTokenService {
    Token createRefreshToken(Long userId);

    Token verifyExpiration(Token token);

    Optional<Token> findTokenByToken(String token);

    RefreshTokenResponse generateNewToken(RefreshTokenDTO request);

    ResponseCookie generateRefreshTokenCookie(String token);

    String getRefreshTokenFromCookies(HttpServletRequest request);

    void deleteByUserId(Long userId);

    ResponseCookie getCleanRefreshTokenCookie();

    /**
     * Checks if the given token is expired and deletes it from the repository if so.
     *
     * @param token the token to check for expiration; must not be null
     * @return true if the token is expired, false otherwise
     */
    boolean isTokenExpired(Token token);

    Optional<User> findUserByToken(String token);

    /**
     * Gets a user's refresh token or null.
     *
     * @param userDetails The user's UserDetails Object.
     * @return The user's refresh token or null.
     */
    Token getTokenFromUserDetails(UserDetails userDetails);

}
