package backend.autopass.service;

import backend.autopass.model.entities.User;
import backend.autopass.model.entities.UserWallet;
import backend.autopass.model.repositories.UserRepository;
import backend.autopass.payload.viewmodels.GoogleWalletPassURLViewModel;
import backend.autopass.service.interfaces.IGoogleWalletService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.walletobjects.Walletobjects;
import com.google.api.services.walletobjects.WalletobjectsScopes;
import com.google.api.services.walletobjects.model.*;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.yaml.snakeyaml.error.YAMLException;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.interfaces.RSAPrivateKey;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * GoogleWalletService - 2024-03-30
 * Raph
 * Much of this code comes from the sample Google wallet repository from Google on <a href="https://github.com/google-wallet/rest-samples">Github</a>.
 * Also, important to know that much of this code will not be used as we already have a class created.
 * In this instance, we will use our generic class `<IssuerID>.1`.
 * Instead of Class suffix we will have 1 as previously mentioned, and as for object suffix we will use user's email.
 * Note that the object suffix will be the user's email with the '@' replaced with a '.',
 * since the Google Wallet API won't accept '@' in object resource ID.
 * AutoPass
 */
@Slf4j
@Service
public class GoogleWalletService implements IGoogleWalletService {

    /**
     * Path to a service account key file from Google Cloud Console. Environment variable:
     * GOOGLE_APPLICATION_CREDENTIALS.
     */
    public static String keyFilePath;

    /** Service account credentials for Google Wallet APIs. */
    public static GoogleCredentials credentials;

    /** Google Wallet service client. */
    public static Walletobjects service;

    public final String classSuffix = "autopass_class";

    @Autowired
    private WebClient webClient;


    /**
     * Google cloud project's issuer ID.
     */
    @Value("${google.IssuerId}")
    public String issuerId;

    @Value("${rotating-barcode-hmac-key}")
    public String key;

    @Value("${application.ip}")
    public String appIp;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;



    public GoogleWalletService(ResourceLoader loader) throws Exception {

        // Gets file resource to then get its file path.
        // The project will always be in different directories for different developers, this just gets the full path of
        // the service account key always as an added measure
        // if the readme instructions were not properly followed.
        Resource resource = loader.getResource("classpath:autopass-414515-f21ce763f523.json");
        keyFilePath =
                System.getenv().getOrDefault("GOOGLE_APPLICATION_CREDENTIALS", resource.getFile().getAbsolutePath());

        auth();
    }

    /**
     * Create an authenticated HTTP client using a service account file.
     *
     */
    public void auth() throws Exception {
        credentials =
                GoogleCredentials.fromStream(new FileInputStream(keyFilePath))
                        .createScoped(List.of(WalletobjectsScopes.WALLET_OBJECT_ISSUER));
        credentials.refresh();

        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        // Initialize Google Wallet API service
        service =
                new Walletobjects.Builder(
                        httpTransport,
                        GsonFactory.getDefaultInstance(),
                        new HttpCredentialsAdapter(credentials))
                        .setApplicationName("AutoPass")
                        .build();
    }

    public GoogleWalletPassURLViewModel createObject(int userId)
            throws IOException {

        User user = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated()){
            return null;
        }
        Object principal = authentication.getPrincipal();
        if(principal instanceof UserDetails){
            user = userService.getUserByEmail(((UserDetails)principal).getUsername());
        }
        String objectSuffix = user.getEmail().replace("@", ".");
        UserWallet userWallet = user.getWallet();
        assert userWallet != null;

        /// SCENARIOS
        /// 1 - Pass doesn't exist ->
        //      generate the link and send it to the front-end.
        //      Set isGoogleWalletPassAdded to true.
        //  2 - Pass exists, and active ->
        //      return Link to see pass
        //  3 - Pass exists, is expired ->
        //      return "EXPIRED": frontend will have feature to renew pass.

        GenericObject pass ;
        try {
            pass = service.genericobject().get(String.format("%s.%s", issuerId, objectSuffix)).execute();

            System.out.println(pass);
            if (pass.getState().equals("expired")) {
                throw new YAMLException("", new Throwable());
            }

            // SCENARIO 2 - PASS EXISTS AND IS ACTIVE

            System.out.printf("Object %s.%s already exists!%n", issuerId, objectSuffix);
            setGooglePassWasAddedToUser(true, userId);

            return GoogleWalletPassURLViewModel
                    .builder()
                    .isExpired(false)
                    .passUrl(this.getJwtForPass(user.getEmail(), true))
                    .build();

        } catch (GoogleJsonResponseException ex) {
            if (ex.getStatusCode() != 404) {
                // Something else went wrong...
                log.error(ex.getMessage());
                return GoogleWalletPassURLViewModel.builder().build();
            }
        } catch (YAMLException ex) {

            // SCENARIO 3 - PASS EXPIRED
            return GoogleWalletPassURLViewModel
                    .builder()
                    .isExpired(true)
                    .passUrl(getJwtForPass(user.getEmail(), false)) // Even though the pass exists, I set it to false, so it won't update it.
                    .build();

        }

        // SCENARIO 1 - PASS DOESN'T EXIST
        return GoogleWalletPassURLViewModel.builder().passUrl(getJwtForPass(user.getEmail(), false)).isExpired(false).build();
    }

    /**
     * Expire an object.
     *
     * <p>Sets the object's state to Expire.
     * If the valid time interval is already set, the pass will
     * expire automatically up to 24 hours after.
     *
     * @param objectSuffix Developer-defined unique ID for this pass object.
     * @return The pass object ID: "{issuerId}.{objectSuffix}"
     */
    public Boolean expireObject(String objectSuffix, String userEmail) throws IOException {

        // Check if the object exists
        try {
            service.genericobject().get(String.format("%s.%s", issuerId, objectSuffix)).execute();
        } catch (GoogleJsonResponseException ex) {
            if (ex.getStatusCode() == 404) {
                // Object does not exist
                System.out.printf("Object %s.%s not found!%n", issuerId, objectSuffix);
            } else {
                // Something else went wrong...
                System.out.printf("Object %s.%s Error.%n", issuerId, objectSuffix);
                log.error(ex.getMessage());
            }
            setGooglePassWasAddedToUser(false, userService.getUserByEmail(userEmail).getId());
            return false;
        }

        // Patch the object, setting the pass as expired
        GenericObject patchBody = new GenericObject().setState("EXPIRED");

        GenericObject response;
        try {
            response = service
                    .genericobject()
                    .patch(String.format("%s.%s", issuerId, objectSuffix), patchBody)
                    .execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Object expiration response");
        System.out.println(response.toPrettyString());
        return true;
    }

    @Override
    public Boolean updatePassTickets(String email, int ticketAmount) {

        String objectSuffix = email.replace("@", ".");

        GenericObject pass;
        try {
            pass = service.genericobject().get(String.format("%s.%s", issuerId, objectSuffix)).execute();
        } catch (GoogleJsonResponseException ex) {
            if (ex.getStatusCode() == 404) {
                // Object does not exist
                log.warn("Object " + issuerId + "." + objectSuffix + " NOT FOUND.%n");
            } else {
                // Something else went wrong...
                log.error("Object " + issuerId + "." + objectSuffix + " Error.%n");
                log.error("Something went wrong updating Google Wallet Pass' tickets : " + ex.getMessage());
            }
            setGooglePassWasAddedToUser(false, userService.getUserByEmail(email).getId());
            return false;
        } catch (IOException ex) {
            log.error("Object " + issuerId + "." + objectSuffix + " Error.%n");
            log.error("Something went wrong updating Google Wallet Pass' tickets : " + ex.getMessage());
            return false;
        }
        assert pass != null;


        User user = userService.getUserByEmail(email);
        UserWallet userWallet = user.getWallet();
        assert userWallet != null;


        Instant instant = Instant.ofEpochMilli((long) userWallet.getMemberShipEnds());
        String formattedDateUntilExpiry = DateTimeFormatter.ofPattern("dd-MM-yyyy").withZone(ZoneId.systemDefault()).format(instant);


        pass.setTextModulesData(
                List.of(
                        new TextModuleData()
                                .setHeader("Tickets")
                                .setBody(String.valueOf(ticketAmount))
                                .setId("tickets"),
                        new TextModuleData()
                                .setHeader("Lasts Until")
                                .setBody(formattedDateUntilExpiry)
                                .setId("membership-validity")
                )
        );

        return patchPass(objectSuffix, pass);
    }

    @Override
    public Boolean updatePassMembershipEnds(String email, double endTimeInMs) {

        String objectSuffix = email.replace("@", ".");

        GenericObject pass;
        try {
            pass = service.genericobject().get(String.format("%s.%s", issuerId, objectSuffix)).execute();
        } catch (GoogleJsonResponseException ex) {
            if (ex.getStatusCode() == 404) {
                // Object does not exist
                log.warn("Object " + issuerId + "." + objectSuffix + " NOT FOUND.%n");
            } else {
                // Something else went wrong...
                log.error("Object " + issuerId + "." + objectSuffix + " Error.%n");
                log.error("Something went wrong updating Google Wallet Pass' tickets : " + ex.getMessage());
            }
            setGooglePassWasAddedToUser(false, userService.getUserByEmail(email).getId());
            return false;
        } catch (IOException ex) {
            log.error("Object " + issuerId + "." + objectSuffix + " Error.%n");
            log.error("Something went wrong updating Google Wallet Pass' tickets : " + ex.getMessage());
            return false;
        }
        assert pass != null;

        User user = userService.getUserByEmail(email);
        UserWallet userWallet = user.getWallet();
        assert userWallet != null;

        Instant instant = Instant.ofEpochMilli((long) endTimeInMs);
        String formattedDateUntilExpiry = DateTimeFormatter.ofPattern("dd-MM-yyyy").withZone(ZoneId.systemDefault()).format(instant);

        if (endTimeInMs < System.currentTimeMillis()) {
            formattedDateUntilExpiry = "No Active Memberships";
        }

        pass.setTextModulesData(
                List.of(
                        new TextModuleData()
                                .setHeader("Tickets")
                                .setBody(String.valueOf(userWallet.getTicketAmount()))
                                .setId("tickets"),
                        new TextModuleData()
                                .setHeader("Lasts Until")
                                .setBody(formattedDateUntilExpiry)
                                .setId("membership-validity")
                )
        );

        return patchPass(objectSuffix, pass);
    }

    @Override
    public String getJwtForPass(String email, boolean passExists) {

        User user = userService.getUserByEmail(email);
        GenericObject pass = getObject(user);

        // If the pass already exists, we can update it.
        if (passExists) {
            updatePass(user.getEmail().replace("@", "."), pass);
        } else {
            setGooglePassWasAddedToUser(true, user.getId());
        }

        // Create the JWT as a HashMap object
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("iss", ((ServiceAccountCredentials) credentials).getClientEmail());
        claims.put("aud", "google");
        claims.put("origins", List.of());
        claims.put("typ", "savetowallet");

        // Create the Google Wallet payload and add to the JWT
        HashMap<String, Object> payload = new HashMap<>();
        payload.put("genericObjects", List.of(pass));
        claims.put("payload", payload);

        // The service account credentials are used to sign the JWT
        Algorithm algorithm =
                Algorithm.RSA256(
                        null, (RSAPrivateKey) ((ServiceAccountCredentials) credentials).getPrivateKey());
        String token = JWT.create().withPayload(claims).sign(algorithm);

        System.out.printf("https://pay.google.com/gp/v/save/%s%n", token);

        return String.format("https://pay.google.com/gp/v/save/%s", token);
    }

    private Boolean patchPass(String objectSuffix, GenericObject pass) {
        try {
            service
                    .genericobject()
                    .patch(String.format("%s.%s", issuerId, objectSuffix), pass)
                    .execute();
        } catch (IOException e) {
            log.error("Object " + issuerId + "." + objectSuffix + " Error.%n");
            log.error("Something went wrong patching Google Wallet Pass' tickets : " + e.getMessage());
            return false;
        }

        return true;
    }

    private void updatePass(String objectSuffix, GenericObject pass) {
        try {
            service
                    .genericobject()
                    .update(String.format("%s.%s", issuerId, objectSuffix), pass)
                    .execute();
        } catch (IOException e) {
            log.error("Object " + issuerId + "." + objectSuffix + " Error.%n");
            log.error("Something went wrong updating Google Wallet Pass' tickets : " + e.getMessage());
        }

    }

    private GenericObject getObject(User user) {

        String objectSuffix = user.getEmail().replace("@", ".");
        UserWallet userWallet = user.getWallet();
        assert userWallet != null;

        Instant instant = Instant.ofEpochMilli((long) userWallet.getMemberShipEnds());
        String formattedDateUntilExpiry = DateTimeFormatter.ofPattern("dd-MM-yyyy").withZone(ZoneId.systemDefault()).format(instant);

        if (userWallet.getMemberShipEnds() < System.currentTimeMillis()) {
            formattedDateUntilExpiry = "No Active Memberships";
        }


        // See the link below for more information on required properties
        // https://developers.google.com/wallet/generic/rest/v1/genericobject
        return
                new GenericObject()
                        .setId(String.format("%s.%s", issuerId, objectSuffix))
                        .setClassId(String.format("%s.%s", issuerId, classSuffix))
                        .setState("ACTIVE")
                        .setGenericType("GENERIC_TYPE_UNSPECIFIED")
                        .setHeroImage(
                                new Image()
                                        .setSourceUri(
                                                new ImageUri()
                                                        .setUri(
                                                                "https://photosforraph.s3.us-east-2.amazonaws.com/1NonTransparentHD.png"))
                                        .setContentDescription(
                                                new LocalizedString()
                                                        .setDefaultValue(
                                                                new TranslatedString()
                                                                        .setLanguage("en-US")
                                                                        .setValue("Hero image description"))))
                        .setTextModulesData(
                                List.of(
                                        new TextModuleData()
                                                .setHeader("Tickets")
                                                .setBody(String.valueOf(userWallet.getTicketAmount()))
                                                .setId("tickets"),
                                        new TextModuleData()
                                                .setHeader("Lasts Until")
                                                .setBody(formattedDateUntilExpiry)
                                                .setId("membership-validity")
                                )
                        )
                        .setLinksModuleData(
                                new LinksModuleData()
                                        .setUris(
                                                Arrays.asList(
                                                        new Uri()
                                                                .setUri("http://localhost:3000/about")
                                                                .setDescription("About us")
                                                                .setId("LINK_MODULE_URI_ID"),
                                                        new Uri()
                                                                .setUri("tel:4384999630")
                                                                .setDescription("Link module tel description")
                                                                .setId("LINK_MODULE_TEL_ID"))))
                        .setImageModulesData(
                                List.of(
                                        new ImageModuleData()
                                                .setMainImage(
                                                        new Image()
                                                                .setSourceUri(
                                                                        new ImageUri()
                                                                                .setUri(
                                                                                        "https://photosforraph.s3.us-east-2.amazonaws.com/banner2.jpg"))
                                                                .setContentDescription(
                                                                        new LocalizedString()
                                                                                .setDefaultValue(
                                                                                        new TranslatedString()
                                                                                                .setLanguage("en-US")
                                                                                                .setValue("Second pass banner"))))
                                                .setId("IMAGE_MODULE_ID")))
                        .setRotatingBarcode(
                                new RotatingBarcode()
                                        .setType("QR_CODE")
                                        .setValuePattern(user.getEmail() + "-{totp_timestamp_seconds}-{totp_value_0}")
                                        .setAlternateText("PASS CODE")
                                        .setTotpDetails(
                                                new RotatingBarcodeTotpDetails()
                                                        .setAlgorithm("TOTP_SHA1")
                                                        .setPeriodMillis(6000L)
                                                        .setParameters(List.of(
                                                                        new RotatingBarcodeTotpDetailsTotpParameters()
                                                                                .setKey(key)
                                                                                .setValueLength(8)
                                                                )
                                                        )

                                        )
                        )
                        .setCardTitle(
                                new LocalizedString()
                                        .setDefaultValue(
                                                new TranslatedString().setLanguage("en-US").setValue("Virtual Transit Pass")))
                        .setHeader(
                                new LocalizedString()
                                        .setDefaultValue(
                                                new TranslatedString().setLanguage("en-US").setValue(user.getFirstName())))
                        .setSubheader(
                                new LocalizedString()
                                        .setDefaultValue(
                                                new TranslatedString().setValue("Holder").setLanguage("en-US")
                                        )
                        )
                        .setHexBackgroundColor("#333")
                        .setLogo(
                                new Image()
                                        .setSourceUri(
                                                new ImageUri()
                                                        .setUri(
                                                                "https://photosforraph.s3.us-east-2.amazonaws.com/9NonTransparentHD.png"))
                                        .setContentDescription(
                                                new LocalizedString()
                                                        .setDefaultValue(
                                                                new TranslatedString()
                                                                        .setLanguage("en-US")
                                                                        .setValue("Auto Pass Card Logo"))));
    }

    private void setGooglePassWasAddedToUser(boolean wasAdded, int userId) {
        User user = userService.getUserById((long) userId);
        user.setIsGoogleWalletPassAdded(wasAdded);
        userRepository.save(user);
    }

}
