package backend.autopass.service;

import backend.autopass.exception.TokenException;
import backend.autopass.model.entities.User;
import backend.autopass.model.enums.TokenType;
import backend.autopass.model.repositories.UserRepository;
import backend.autopass.payload.dto.RefreshTokenDTO;
import backend.autopass.payload.viewmodels.RefreshTokenResponse;
import backend.autopass.security.jwt.refreshToken.Token;
import backend.autopass.security.jwt.refreshToken.TokenRepository;
import backend.autopass.service.interfaces.IRefreshTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import java.time.Instant;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService implements IRefreshTokenService {

    private final UserRepository userRepository;
    private final TokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    @Value("${application.security.same-site-setting}")
    private String sameSiteCookieSetting;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;
    @Value("${application.security.jwt.refresh-token.cookie-name}")
    private String refreshTokenName;


    @Transactional
    @Override
    public Token createRefreshToken(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Token refreshToken = refreshTokenRepository.findByUser(user)
                .orElse(new Token());

        refreshToken.setToken(Base64.getEncoder().encodeToString(UUID.randomUUID().toString().getBytes()));
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshExpiration));
        refreshToken.setRevoked(false);
        refreshToken.setUser(user);

        return refreshTokenRepository.save(refreshToken);

    }

    @Override
    public Token verifyExpiration(Token token) {
        if (token == null) {
            log.error("Token is null");
            throw new TokenException(null, "Token is null");
        }
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenException(token.getToken(), "Refresh token was expired. Please make a new authentication request");
        }
        return token;
    }

    public Boolean isTokenExpired(Token token) {
        return token != null && token.getExpiryDate().compareTo(Instant.now()) >= 0;
    }

    @Override
    public Optional<Token> findTokenByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public Optional<User> findUserByToken(String token) {
        Token refreshToken;
        if (findTokenByToken(token).isPresent()) {
            refreshToken = findTokenByToken(token).get();
            return Optional.ofNullable(refreshToken.getUser());
        }
        return Optional.empty();
    }


    @Override
    public RefreshTokenResponse generateNewToken(RefreshTokenDTO request) {
        User user = refreshTokenRepository.findByToken(request.getRefreshToken())
                .map(this::verifyExpiration)
                .map(Token::getUser)
                .orElseThrow(() -> new TokenException(request.getRefreshToken(), "Refresh token does not exist"));

        String token = jwtService.generateToken(user);
        return RefreshTokenResponse.builder()
                .accessToken(token)
                .refreshToken(request.getRefreshToken())
                .tokenType(TokenType.BEARER.name())
                .build();
    }

    @Override
    public ResponseCookie generateRefreshTokenCookie(String token) {
        return ResponseCookie.from(refreshTokenName, token)
                .path("/")
                .maxAge(refreshExpiration / 1000) // 15 days in seconds
                .httpOnly(true)
                .secure(true)
                .sameSite(sameSiteCookieSetting)
                .build();
    }

    @Override
    public String getRefreshTokenFromCookies(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, refreshTokenName);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return "";
        }
    }

    @Override
    public void deleteByToken(String token) {
        refreshTokenRepository.findByToken(token).ifPresent(refreshTokenRepository::delete);
    }

    @Override
    public ResponseCookie getCleanRefreshTokenCookie() {
        return ResponseCookie.from(refreshTokenName, "")
                .path("/")
                .httpOnly(true)
                .sameSite(sameSiteCookieSetting)
                .maxAge(0)
                .build();
    }

    @Override
    public Token getTokenFromUserDetails(UserDetails userDetails) {

        Optional<User> user = userRepository.findByEmail(userDetails.getUsername());
        Optional<Token> token = Optional.empty();

        if (user.isPresent()) {
            token = refreshTokenRepository.findByUser(user.get());
        }

        assert Objects.requireNonNull(token).isPresent();
        return token.orElse(null);
    }


}