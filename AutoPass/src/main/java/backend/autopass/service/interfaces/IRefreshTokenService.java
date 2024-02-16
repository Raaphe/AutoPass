package backend.autopass.service.interfaces;

import backend.autopass.model.entities.User;
import backend.autopass.payload.dto.RefreshTokenDTO;
import backend.autopass.payload.viewmodels.RefreshTokenResponse;
import backend.autopass.security.jwt.refreshToken.Token;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface IRefreshTokenService {
    /**
     * Generates the refresh token based on a user's id.
     *
     * @param userId The target user's id.
     * @return The refresh token entity.
     */
    Token createRefreshToken(Long userId);

    /**
     * Verifies the refresh token's expiry date.
     *
     * @param token The token entity to verify.
     * @return The token.
     */
    Token verifyExpiration(Token token);

    /**
     * Gets the token object in the database from its string value.
     *
     * @param token The string representing the refresh token.
     * @return The optional refresh token entity.
     */
    Optional<Token> findTokenByToken(String token);

    /**
     * Generates a new access token based on a refresh token.
     *
     * @param request The object with the refresh token.
     * @return Returns all tokens if valid.
     */
    RefreshTokenResponse generateNewToken(RefreshTokenDTO request);

    /**
     * Deletes the token based on its user's ID.
     *
     * @param userId The target user's ID.
     */
    void deleteByUserId(Long userId);

    /**
     * Checks if the given token is expired and deletes it from the repository if so.
     *
     * @param token the token to check for expiration; must not be null
     * @return true if the token is expired, false otherwise
     */
    boolean isTokenExpired(Token token);

    /**
     * Finds a user by their respective Refresh Token.
     *
     * @param token The refresh token in string format.
     * @return The optional user.
     */
    Optional<User> findUserByToken(String token);

    /**
     * Gets a user's refresh token or null.
     *
     * @param userDetails The user's UserDetails Object.
     * @return The user's refresh token or null.
     */
    Token getTokenFromUserDetails(UserDetails userDetails);

}
