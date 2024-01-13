package backend.autopass.service.interfaces;

import backend.autopass.payload.dto.RefreshTokenDTO;
import backend.autopass.payload.viewmodels.RefreshTokenResponse;
import backend.autopass.security.jwt.refreshToken.Token;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;

import java.util.Optional;

public interface IRefreshTokenService {
    Token createRefreshToken(Long userId);

    Token verifyExpiration(Token token);

    Optional<Token> findByToken(String token);

    RefreshTokenResponse generateNewToken(RefreshTokenDTO request);

    ResponseCookie generateRefreshTokenCookie(String token);

    String getRefreshTokenFromCookies(HttpServletRequest request);

    void deleteByToken(String token);

    ResponseCookie getCleanRefreshTokenCookie();

}
