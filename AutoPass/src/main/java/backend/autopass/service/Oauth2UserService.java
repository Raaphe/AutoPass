package backend.autopass.service;

import backend.autopass.model.entities.Pass;
import backend.autopass.model.entities.User;
import backend.autopass.model.entities.UserWallet;
import backend.autopass.model.enums.Role;
import backend.autopass.model.repositories.PassRepository;
import backend.autopass.model.repositories.UserRepository;
import backend.autopass.model.repositories.UserWalletRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import java.security.Principal;
import java.util.Optional;

/**
 * Oauth2UserService - 2024-03-30
 * Raph
 * When a user tries to log in with Google Oauth2, this method is triggered and here is overloaded.
 * Responsible for saving Google user's information to our own database and to "link"
 * normal user's accounts ('USER') with their Google accounts ('GOOGLE_USER')
 * AutoPass
 */
@Service
@AllArgsConstructor
@Slf4j
public class Oauth2UserService extends DefaultOAuth2UserService {


    private final UserRepository userRepository;
    private final PassRepository passRepository;
    private final UserWalletRepository userWalletRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User user = super.loadUser(userRequest);


        // MULTIPLE SCENARIOS
        //  - 1. User with gmail already exists, and has role USER -> turn into GOOGLE_USER
        //  - 2. User with gmail does NOT already exist -> Create GOOGLE_USER
        //  - 3. User with gmail already exists, and has role GOOGLE_USER -> Do nothing
        //  - NOTE: After all of these steps, we will always have to make user's new refresh token.

        if (Optional.ofNullable(user.getAttribute("email")).isPresent()) {

            String email = (String)Optional.ofNullable(user.getAttribute("email")).get();
            Optional<User> existingUser = userRepository.findByEmail(email);

            // User email already exists in DB.
            if (existingUser.isPresent()) {
                User concreteUser = existingUser.get();

                // User is deleted.
                if (!concreteUser.isEnabled() || concreteUser.isDeleted()) {
                    return null;
                }

                // 1 - Transition from normal user to a Google user account.
                // In the case where the account is ADMIN, we cannot overwrite the ADMIN role with
                // the GOOGLE_USER role. Instead, I'm putting an access token inside the user when a Google account is linked.
                // This is now the new condition for checking if an account is a Google account.
                if (concreteUser.getGoogleAccessToken() == null && !concreteUser.getRole().equals(Role.SCANNER_USER)) {

                    if (!concreteUser.getRole().equals(Role.ADMIN)) {
                        concreteUser.setRole(Role.GOOGLE_USER);
                    }

                    concreteUser.setGoogleAccessToken(concreteUser.getGoogleAccessToken());

                    if (!concreteUser.getIsProfileImageChanged()) {
                        concreteUser.setProfileImageUrl(user.getAttribute("picture"));
                        concreteUser.setGoogleAccessToken(userRequest.getAccessToken().getTokenValue());
                    }

                    userRepository.save(concreteUser);

                    this.createGoogleUser();
                    return user;
                }

                // 3 - Is google account that already exists in a database.

                // Assigns user refresh token
                this.createGoogleUser();
                return user;
            }


            // 2 - Create new google user in database.

            Pass pass = passRepository.save(new Pass());
            UserWallet userWallet = UserWallet
                    .builder()
                    .membershipActive(false)
                    .ticketAmount(0)
                    .memberShipEnds(System.currentTimeMillis())
                    .build();
            userWallet = userWalletRepository.save(userWallet);

            String lastname = user.getAttribute("family_name");
            if (lastname == null) {
                lastname = "";
            }

            User newGoogleUser = User
                    .builder()
                    .email(email)
                    .googleAccessToken(userRequest.getAccessToken().getTokenValue())
                    .role(Role.GOOGLE_USER)
                    .isDeleted(false)
                    .profileImageUrl(user.getAttribute("picture"))
                    .wallet(userWallet)
                    .pass(pass)
                    .password("")
                    .firstName(user.getAttribute("given_name"))
                    .lastName(lastname)
                    .build();

            userRepository.save(newGoogleUser);
            this.createGoogleUser();
        }

        return user;
    }

    public User getUserByPrincipal(Principal principal) {
        Optional<User> user = userRepository.findByEmail(principal.getName());
        return user.orElse(null);
    }

    public void createGoogleUser() {
        log.info("==== Saving GOOGLE_USER ====");
    }

}
