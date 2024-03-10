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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService implements IRefreshTokenService {

    private final UserRepository userRepository;
    private final TokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

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

    @Override
    public boolean isTokenExpired(Token token) {
        if (token == null) return true;
        boolean isExpired = Instant.now().isAfter(token.getExpiryDate());
        if (isExpired) {
            refreshTokenRepository.delete(token);
            return true;
        } else return false;
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
    public void deleteByUserId(Long userId) {
        try {
            Optional<User> user = userRepository.getUserById(Math.toIntExact(userId));
            user.flatMap(refreshTokenRepository::findByUser).ifPresent(refreshTokenRepository::delete);
        } catch (Exception e) {
            log.error("Refresh token Doesn't Exist.");
        }
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

    @Override
    public Token getTokenFromUserEmail(String email) {
        AtomicReference<Token> refreshToken = new AtomicReference<>(Token.builder().build());
        userRepository.findByEmail(email).flatMap(refreshTokenRepository::findByUser).ifPresent(refreshToken::set);
        return refreshToken.get();
    }


}
