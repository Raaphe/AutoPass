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
import java.util.Date;
import java.util.Optional;

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
        //  - 1 . User with gmail already exists, and has role USER -> turn into GOOGLE_USER
        //  - 2 . User with gmail does NOT already exist -> Create GOOGLE_USER
        //  - 3 . User with gmail already exists, and has role GOOGLE_USER -> Do nothing
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

                // 1 - Transition from normal user to google user account.
                if (concreteUser.getRole() == Role.USER) {
                    concreteUser.setRole(Role.GOOGLE_USER);
                    concreteUser.setGoogleAccessToken(concreteUser.getGoogleAccessToken());

                    userRepository.save(concreteUser);

                    this.createGoogleUser();
                    return user;
                }

                // 3 - Is google account that already exists in database.

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
                    .memberShipEnds(new Date(System.currentTimeMillis()))
                    .build();
            userWallet = userWalletRepository.save(userWallet);

            User newGoogleUser = User
                    .builder()
                    .email(email)
                    .googleAccessToken(userRequest.getAccessToken().getTokenValue())
                    .role(Role.GOOGLE_USER)
                    .isDeleted(false)
                    .profileImageUrl(user.getAttribute("photo"))
                    .wallet(userWallet)
                    .pass(pass)
                    .password("")
                    .firstName(user.getAttribute("given_name"))
                    .lastName(user.getAttribute("family_name"))
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
