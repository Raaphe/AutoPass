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

    void deleteByToken(String token);

    ResponseCookie getCleanRefreshTokenCookie();

    Optional<User> findUserByToken(String token);

    /**
     * Gets a user's refresh token or null.
     *
     * @param userDetails The user's UserDetails Object.
     * @return The user's refresh token or null.
     */
    Token getTokenFromUserDetails(UserDetails userDetails);

}
