package backend.autopass.config;

import backend.autopass.security.handlers.CustomAccessDeniedHandler;
import backend.autopass.security.handlers.Http401UnauthorizedEntryPoint;
import backend.autopass.security.handlers.Oauth2CustomAuthSuccessHandler;
import backend.autopass.security.jwt.filter.JwtAuthenticationFilter;
import backend.autopass.service.JwtService;
import backend.autopass.service.RefreshTokenService;
import backend.autopass.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    public static final String[] WHITE_LIST_URL = {
            "/auth/login",
            "/auth/signup",
            "/auth/refresh-token",
            "/auth/logout",
            "/auth/isLogged",
            "/auth/app-ip",
            "/auth/forgot-password",
            "/auth/check-refresh-token",
            "/auth/update-password",
            "/auth/get-user-role",
            "/dashboard",
            "/swagger-ui.html",
            "/v3/api-docs",
            "/v3/api-docs.yaml",
            "/swagger-ui.html/**",
            "/h2-console",
            "/h2-console/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/favicon.ico",
            "/swagger-resources",
            "/error",
            "/webjars/**",
            "/oauth2/authorization/google",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", "/swagger-resources/**", "/webjars/**"

    };

    // Needed for Success Handler Dependencies
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;
    private final JwtService jwtService;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;
    private final Http401UnauthorizedEntryPoint unauthorizedEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final ApplicationContext applicationContext;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .headers((headers) ->
                    headers.defaultsDisabled()
                    .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                )
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(unauthorizedEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(WHITE_LIST_URL).permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/user/info").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/user/delete-user").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/user/update-user-info").authenticated()
                        .requestMatchers(HttpMethod.GET, "/user/get-user-pfp").authenticated()
                        .requestMatchers(HttpMethod.POST, "/user/upload-profile-image").authenticated()
                        .requestMatchers(HttpMethod.GET, "/google/user").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/payment/charge").authenticated()
                        .requestMatchers(HttpMethod.GET, "/wallet/wallet-info").authenticated()
                        .requestMatchers(HttpMethod.GET, "/admin/scanner/scanners").authenticated()
                        .requestMatchers(HttpMethod.POST, "/admin/scanner/create-scanner").authenticated()
                        .requestMatchers(HttpMethod.POST, "/admin/scanner/delete-bus").authenticated()
                        .requestMatchers(HttpMethod.GET, "/admin/scanner/scanner-info").authenticated()
                        .requestMatchers(HttpMethod.GET, "/products/get-all-products").authenticated()
                        .requestMatchers(HttpMethod.GET, "/google-wallet-api/add-google-wallet-pass").authenticated()
                )
                .oauth2Login(oauth2Login -> oauth2Login
                        .loginPage("/oauth2/authorization/google")
                        .successHandler(successHandler())
                )

                .logout(logout -> logout
                        .logoutSuccessUrl("/auth/logout")
                        .logoutSuccessHandler(oidcLogoutSuccessHandler())
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return new Oauth2CustomAuthSuccessHandler(refreshTokenService, userService, jwtService);
    }

    private LogoutSuccessHandler oidcLogoutSuccessHandler() {
        OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler =
                new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository());
        oidcLogoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}");
        return oidcLogoutSuccessHandler;
    }

    private ClientRegistrationRepository clientRegistrationRepository() {
        return this.applicationContext.getBean(ClientRegistrationRepository.class);
    }

}
