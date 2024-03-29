package backend.autopass.service;

import backend.autopass.model.entities.Pass;
import backend.autopass.model.entities.User;
import backend.autopass.model.repositories.UserRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.api.client.googleapis.batch.BatchRequest;
import com.google.api.client.googleapis.batch.json.JsonBatchCallback;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.*;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.walletobjects.*;
import com.google.api.services.walletobjects.model.*;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import java.io.*;
import java.security.interfaces.RSAPrivateKey;
import java.util.*;

/**
 * @author Raphael Paquin
 * @version 01
 * Google Wallet Service.
 * Much of this code comes from the sample Google wallet repository from Google on <a href="https://github.com/google-wallet/rest-samples">Github</a>.
 * Also, important to know that much of this code will not be used as we already have a class created.
 * In this instance, we will use our generic class `<IssuerID>.1`.
 * Instead of Class suffix we will have 1 as previously mentioned, and as for object suffix we will use user's email.
 * 2024-03-23
 * AutoPass
 */
@Slf4j
@Service
public class GoogleWalletService {

    /**
     * Path to a service account key file from Google Cloud Console. Environment variable:
     * GOOGLE_APPLICATION_CREDENTIALS.
     */
    public static String keyFilePath;

    /** Service account credentials for Google Wallet APIs. */
    public static GoogleCredentials credentials;

    /** Google Wallet service client. */
    public static Walletobjects service;

    /**
     * Google cloud project's issuer ID.
     */
    @Value("${google.IssuerId}")
    public String issuerId;


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
        // -if the readme instructions were not properly followed.
        Resource resource = loader.getResource("classpath:autopass-414515-f21ce763f523.json");
        keyFilePath =
                System.getenv().getOrDefault("GOOGLE_APPLICATION_CREDENTIALS", resource.getFile().getAbsolutePath());

        auth();
    }
    // [END setup]

    // [START auth]
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
    // [END auth]


    // [START createObject]
    /**
     * Create an object.
     * TODO: Not customized for our use-case
     *
     * @return The pass object ID: "{issuerId}.{objectSuffix}"
     */
    public String createObject(int userId)
            throws IOException {



        User user = userService.getUserById((long) userId);
        Pass pass = user.getPass();
        assert pass != null;

        String objectSuffix = user.getEmail();
        String classSuffix = "codelab_class";

        GenericClass genericClass;
        try {
            genericClass = service.genericclass().get(String.format("%s.%s", issuerId, classSuffix)).execute();
        } catch (GoogleJsonResponseException ex) {
            if (ex.getStatusCode() != 404) {
                // Something else went wrong...
                log.error(String.format("%s.%s", issuerId, classSuffix));
                return String.format("%s.%s", issuerId, classSuffix);
            }
            return null;
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }



        // Check if the object exists
        try {
            service.genericobject().get(issuerId + "." + objectSuffix).execute();

            System.out.printf("Object %s.%s already exists!%n", issuerId, objectSuffix);
            return String.format("%s.%s", issuerId, objectSuffix);
        } catch (GoogleJsonResponseException ex) {
            if (ex.getStatusCode() != 400) {
                // Something else went wrong...
                log.error(ex.getMessage());
                return String.format("%s.%s", issuerId, objectSuffix);
            }
        }


        GenericObject codelabObject =
                new GenericObject()
                        .setId(String.format("%s.%s", issuerId, user.getEmail()))
                        .setClassId(String.format("%s.%s", issuerId, classSuffix))
                        .setHexBackgroundColor("#333")
                        .setState("ACTIVE")
                        .setCardTitle(
                                new LocalizedString(

                                ).setDefaultValue(new TranslatedString().setValue("Google I/O \'22").setLanguage("en"))
                        )
                        .setSubheader(new LocalizedString().setDefaultValue(new TranslatedString().setLanguage("en").setValue("Attendee")))
                        .setHeader(new LocalizedString().setDefaultValue(new TranslatedString().setValue("Alex McJacobs").setLanguage("en")))
                        .setBarcode(new Barcode().setType("QR_CODE").setValue(issuerId + "." + user.getEmail()))
                        .setHeroImage(
                                new Image()
                                        .setSourceUri(
                                                new ImageUri()
                                                        .setUri(
                                                                "https://storage.googleapis.com/wallet-lab-tools-codelab-artifacts-public/google-io-hero-demo-only.jpg"))
                        )
                        .setTextModulesData(
                                List.of(
                                        new TextModuleData()
                                                .setHeader("Your Auto-Pass")
                                                .setBody(user.getFirstName() + ", " + user.getLastName())
                                                .setId("1"),
                                        new TextModuleData()
                                                .setHeader("Ticket Amount")
                                                .setBody("Count: " + user.getWallet().getTicketAmount())
                                                .setId("2"),
                                        new TextModuleData()
                                                .setHeader("Ticket Amount")
                                                .setBody("Count: " + user.getWallet().getTicketAmount())
                                                .setId("3")
                                )
                        );


        GenericObject newObject =
                new GenericObject()
                        .setId(String.format("%s.%s", issuerId, user.getEmail()))
                        .setClassId(String.format("%s.%s", issuerId, classSuffix))
                        .setState("ACTIVE")
                        .setHeroImage(
                                new Image()
                                        .setSourceUri(
                                                new ImageUri()
                                                        .setUri(
                                                                "https://farm4.staticflickr.com/3723/11177041115_6e6a3b6f49_o.jpg"))
                                        .setContentDescription(
                                                new LocalizedString()
                                                        .setDefaultValue(
                                                                new TranslatedString()
                                                                        .setLanguage("en-US")
                                                                        .setValue("AutoPass"))))
                        .setTextModulesData(
                                List.of(
                                        new TextModuleData()
                                                .setHeader("Your Auto-Pass")
                                                .setBody(user.getFirstName() + ", " + user.getLastName())
                                                .setId("1"),
                                        new TextModuleData()
                                                .setHeader("Ticket Amount")
                                                .setBody("Count: " + user.getWallet().getTicketAmount())
                                                .setId("2"),
                                        new TextModuleData()
                                                .setHeader("Ticket Amount")
                                                .setBody("Count: " + user.getWallet().getTicketAmount())
                                                .setId("3")
                                ))
                        .setLinksModuleData(
                                new LinksModuleData()
                                        .setUris(
                                                List.of(
                                                        new Uri()
                                                                .setUri("http://localhost:3000/about")
                                                                .setDescription("AutoPass information")
                                                                .setId("1")
                                                )))
                        .setImageModulesData(
                                List.of(
                                        new ImageModuleData()
                                                .setMainImage(
                                                        new Image()
                                                                .setSourceUri(
                                                                        new ImageUri()
                                                                                .setUri(
                                                                                        "https://photosforraph.s3.us-east-2.amazonaws.com/Passbanner.png"))
                                                                .setContentDescription(
                                                                        new LocalizedString()
                                                                                .setDefaultValue(
                                                                                        new TranslatedString()
                                                                                                .setLanguage("en-US")
                                                                                                .setValue("Pass Banner"))))
                                                .setId("1")))
                        .setBarcode(new Barcode().setType("QR_CODE").setValue(user.getGoogleAccessToken()))
                        .setCardTitle(
                                new LocalizedString()
                                        .setDefaultValue(
                                                new TranslatedString().setLanguage("en-US").setValue("AUTO_PASS")))
                        .setHeader(
                                new LocalizedString()
                                        .setDefaultValue(
                                                new TranslatedString().setLanguage("en-US").setValue("Virtual Transit Pass")))
                        .setHexBackgroundColor("#333")
                        .setLogo(
                                new Image()
                                        .setSourceUri(
                                                new ImageUri()
                                                        .setUri(
                                                                "https://photosforraph.s3.us-east-2.amazonaws.com/7.png"))
                                        .setContentDescription(
                                                new LocalizedString()
                                                        .setDefaultValue(
                                                                new TranslatedString()
                                                                        .setLanguage("en-US")
                                                                        .setValue("AutoPass Logo"))));

        GenericObject response;
        try {
            response = service.genericobject().insert(codelabObject).execute();
        } catch (Exception e) {

            System.out.println(e.getMessage());
            throw new EOFException();
        }

        System.out.println("Object insert response");
        System.out.println(response.toPrettyString());

        return response.getId();
    }
    // [END createObject]

    // [START jwtNew]
    /**
     * Generate a signed JWT that creates a new pass class and object.
     * TODO: Not customized for our use-case
     *
     * <p>When the user opens the "Add to Google Wallet" URL and saves the pass to their wallet, the
     * pass class and object defined in the JWT are created. This allows you to create multiple pass
     * classes and objects in one API call when the user saves the pass to their wallet.
     *
     * @return An "Add to Google Wallet" link.
     */
    public String createJWTNewObjects(int userId) throws IOException {

//        createObject(userId);
        // The Google cloud project's main class' suffix
        // https://pay.google.com/business/console/passes/BCR2DN4T7W7OBOCT/issuer/3388000000022325174/generic/edit/3388000000022325174.1
        String classSuffix = "codelab_class";

        // See the link below for more information on required properties
        // https://developers.google.com/wallet/generic/rest/v1/genericclass
        // See the link below for more information on required properties
        // https://developers.google.com/wallet/generic/rest/v1/genericobject

        User user = userService.getUserById((long) userId);
        Pass pass = user.getPass();
        assert pass != null;

        // See link below for more information on required properties
        // https://developers.google.com/wallet/generic/rest/v1/genericclass

        TEST(issuerId, "1", user.getEmail());

        GenericClass genericClass;
        try {
            genericClass = service.genericclass().get(String.format("%s.%s", issuerId, classSuffix)).execute();
        } catch (GoogleJsonResponseException ex) {
            if (ex.getStatusCode() != 404) {
                // Something else went wrong...
                log.error(String.format("%s.%s", issuerId, classSuffix));
                return String.format("%s.%s", issuerId, classSuffix);
            }
            return null;
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }



        GenericObject newObject =
                new GenericObject()
                        .setId(String.format("%s.%s", issuerId, user.getEmail()))
                        .setClassId(String.format("%s.%s", issuerId, classSuffix))
                        .setState("ACTIVE")
                        .setHeroImage(
                                new Image()
                                        .setSourceUri(
                                                new ImageUri()
                                                        .setUri(
                                                                "https://farm4.staticflickr.com/3723/11177041115_6e6a3b6f49_o.jpg"))
                                        .setContentDescription(
                                                new LocalizedString()
                                                        .setDefaultValue(
                                                                new TranslatedString()
                                                                        .setLanguage("en-US")
                                                                        .setValue("AutoPass"))))
                        .setTextModulesData(
                                List.of(
                                        new TextModuleData()
                                                .setHeader("Your Auto-Pass")
                                                .setBody(user.getFirstName() + ", " + user.getLastName())
                                                .setId("1"),
                                        new TextModuleData()
                                                .setHeader("Ticket Amount")
                                                .setBody("Count: " + user.getWallet().getTicketAmount())
                                                .setId("2"),
                                        new TextModuleData()
                                                .setHeader("Ticket Amount")
                                                .setBody("Count: " + user.getWallet().getTicketAmount())
                                                .setId("3")
                                ))
                        .setLinksModuleData(
                                new LinksModuleData()
                                        .setUris(
                                                List.of(
                                                        new Uri()
                                                                .setUri("http://localhost:3000/about")
                                                                .setDescription("AutoPass information")
                                                                .setId("1")
                                                )))
                        .setImageModulesData(
                                List.of(
                                        new ImageModuleData()
                                                .setMainImage(
                                                        new Image()
                                                                .setSourceUri(
                                                                        new ImageUri()
                                                                                .setUri(
                                                                                        "https://photosforraph.s3.us-east-2.amazonaws.com/Passbanner.png"))
                                                                .setContentDescription(
                                                                        new LocalizedString()
                                                                                .setDefaultValue(
                                                                                        new TranslatedString()
                                                                                                .setLanguage("en-US")
                                                                                                .setValue("Pass Banner"))))
                                                .setId("1")))
                        .setBarcode(new Barcode().setType("QR_CODE").setValue(user.getGoogleAccessToken()))
                        .setCardTitle(
                                new LocalizedString()
                                        .setDefaultValue(
                                                new TranslatedString().setLanguage("en-US").setValue("AUTO_PASS")))
                        .setHeader(
                                new LocalizedString()
                                        .setDefaultValue(
                                                new TranslatedString().setLanguage("en-US").setValue("Virtual Transit Pass")))
                        .setHexBackgroundColor("#333")
                        .setLogo(
                                new Image()
                                        .setSourceUri(
                                                new ImageUri()
                                                        .setUri(
                                                                "https://photosforraph.s3.us-east-2.amazonaws.com/7.png"))
                                        .setContentDescription(
                                                new LocalizedString()
                                                        .setDefaultValue(
                                                                new TranslatedString()
                                                                        .setLanguage("en-US")
                                                                        .setValue("AutoPass Logo"))));

        GenericObject codelabObject =
                new GenericObject()
                        .setId(String.format("%s.%s", issuerId, user.getEmail()))
                        .setClassId(String.format("%s.%s", issuerId, classSuffix))
                        .setHexBackgroundColor("#333")
                        .setState("ACTIVE")
                        .setCardTitle(
                                new LocalizedString(

                                ).setDefaultValue(new TranslatedString().setValue("Google I/O \'22").setLanguage("en"))
                        )
                        .setSubheader(new LocalizedString().setDefaultValue(new TranslatedString().setLanguage("en").setValue("Attendee")))
                        .setHeader(new LocalizedString().setDefaultValue(new TranslatedString().setValue("Alex McJacobs").setLanguage("en")))
                        .setBarcode(new Barcode().setType("QR_CODE").setValue(issuerId + "." + user.getEmail()))
                        .setHeroImage(
                                new Image()
                                        .setSourceUri(
                                                new ImageUri()
                                                        .setUri(
                                                                "https://storage.googleapis.com/wallet-lab-tools-codelab-artifacts-public/google-io-hero-demo-only.jpg"))
                        )
                        .setTextModulesData(
                                List.of(
                                        new TextModuleData()
                                                .setHeader("Your Auto-Pass")
                                                .setBody(user.getFirstName() + ", " + user.getLastName())
                                                .setId("1"),
                                        new TextModuleData()
                                                .setHeader("Ticket Amount")
                                                .setBody("Count: " + user.getWallet().getTicketAmount())
                                                .setId("2"),
                                        new TextModuleData()
                                                .setHeader("Ticket Amount")
                                                .setBody("Count: " + user.getWallet().getTicketAmount())
                                                .setId("3")
                                )
                        );


        // Create the JWT as a HashMap object
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("iss", ((ServiceAccountCredentials) credentials).getClientEmail());
        claims.put("aud", "google");
        claims.put("origins", List.of("localhost", appIp));
        claims.put("typ", "savetowallet");

        // Create the Google Wallet payload and add to the JWT
        HashMap<String, Object> payload = new HashMap<String, Object>();
        payload.put("genericClasses", List.of(genericClass));
        payload.put("genericObjects", List.of(newObject));
        claims.put("payload", payload);

        System.out.println(claims);
        System.out.println(payload);

        // The service account credentials are used to sign the JWT
        Algorithm algorithm =
                Algorithm.RSA256(
                        null, (RSAPrivateKey) ((ServiceAccountCredentials) credentials).getPrivateKey());
        String token = JWT.create().withPayload(claims).sign(algorithm);

        System.out.println("Add to Google Wallet link");
        System.out.printf("https://pay.google.com/gp/v/save/%s%n", token);

        return String.format("https://pay.google.com/gp/v/save/%s", token);
    }
    // [END jwtNew]

    // [START jwtExisting]
    /**
     * Generate a signed JWT that references an existing pass object.
     * TODO: Not customized for our use-case
     *
     * <p>When the user opens the "Add to Google Wallet" URL and saves the pass to their wallet, the
     * pass objects defined in the JWT are added to the user's Google Wallet app. This allows the user
     * to save multiple pass objects in one API call.
     *
     * <p>The objects to add must follow the below format:
     *
     * <p>{ 'id': 'ISSUER_ID.OBJECT_SUFFIX', 'classId': 'ISSUER_ID.CLASS_SUFFIX' }
     *
     * @return An "Add to Google Wallet" link.
     */
    public String createJWTExistingObjects(int userId) {
        // Multiple pass types can be added at the same time
        // At least one type must be specified in the JWT claims
        // Note: Make sure to replace the placeholder class and object suffixes
        HashMap<String, Object> objectsToAdd = new HashMap<>();

        // Event tickets
        objectsToAdd.put(
                "eventTicketObjects",
                List.of(
                        new EventTicketObject()
                                .setId(String.format("%s.%s", issuerId, "EVENT_OBJECT_SUFFIX"))
                                .setClassId(String.format("%s.%s", issuerId, "EVENT_CLASS_SUFFIX"))));

        // Boarding passes
        objectsToAdd.put(
                "flightObjects",
                List.of(
                        new FlightObject()
                                .setId(String.format("%s.%s", issuerId, "FLIGHT_OBJECT_SUFFIX"))
                                .setClassId(String.format("%s.%s", issuerId, "FLIGHT_CLASS_SUFFIX"))));

        // Generic passes
        objectsToAdd.put(
                "genericObjects",
                List.of(
                        new GenericObject()
                                .setId(String.format("%s.%s", issuerId, "GENERIC_OBJECT_SUFFIX"))
                                .setClassId(String.format("%s.%s", issuerId, "GENERIC_CLASS_SUFFIX"))));

        // Gift cards
        objectsToAdd.put(
                "giftCardObjects",
                List.of(
                        new GiftCardObject()
                                .setId(String.format("%s.%s", issuerId, "GIFT_CARD_OBJECT_SUFFIX"))
                                .setClassId(String.format("%s.%s", issuerId, "GIFT_CARD_CLASS_SUFFIX"))));

        // Loyalty cards
        objectsToAdd.put(
                "loyaltyObjects",
                List.of(
                        new LoyaltyObject()
                                .setId(String.format("%s.%s", issuerId, "LOYALTY_OBJECT_SUFFIX"))
                                .setClassId(String.format("%s.%s", issuerId, "LOYALTY_CLASS_SUFFIX"))));

        // Offers
        objectsToAdd.put(
                "offerObjects",
                List.of(
                        new OfferObject()
                                .setId(String.format("%s.%s", issuerId, "OFFER_OBJECT_SUFFIX"))
                                .setClassId(String.format("%s.%s", issuerId, "OFFER_CLASS_SUFFIX"))));

        // Transit passes
        objectsToAdd.put(
                "transitObjects",
                List.of(
                        new TransitObject()
                                .setId(String.format("%s.%s", issuerId, "TRANSIT_OBJECT_SUFFIX"))
                                .setClassId(String.format("%s.%s", issuerId, "TRANSIT_CLASS_SUFFIX"))));

        // Create the JWT as a HashMap object
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("iss", ((ServiceAccountCredentials) credentials).getClientEmail());
        claims.put("aud", "google");
        claims.put("origins", List.of("www.example.com"));
        claims.put("typ", "savetowallet");
        claims.put("payload", objectsToAdd);

        // The service account credentials are used to sign the JWT
        Algorithm algorithm =
                Algorithm.RSA256(
                        null, (RSAPrivateKey) ((ServiceAccountCredentials) credentials).getPrivateKey());
        String token = JWT.create().withPayload(claims).sign(algorithm);

        System.out.println("Add to Google Wallet link");
        System.out.printf("https://pay.google.com/gp/v/save/%s%n", token);

        return String.format("https://pay.google.com/gp/v/save/%s", token);
    }
    // [END jwtExisting]

    // [START batch]
    /**
     * Batch create Google Wallet objects from an existing class.
     * TODO: Not customized for our use-case
     *
     * @param issuerId The issuer ID being used for this request.
     * @param classSuffix Developer-defined unique ID for this pass class.
     */
    public void batchCreateObjects(String issuerId, String classSuffix) throws IOException {
        // Create the batch request client
        BatchRequest batch = service.batch(new HttpCredentialsAdapter(credentials));

        // The callback will be invoked for each request in the batch
        JsonBatchCallback<GenericObject> callback =
                new JsonBatchCallback<>() {
                    // Invoked if the request was successful
                    public void onSuccess(GenericObject response, HttpHeaders responseHeaders) {
                        System.out.println("Batch insert response");
                        System.out.println(response.toString());
                    }

                    // Invoked if the request failed
                    public void onFailure(GoogleJsonError e, HttpHeaders responseHeaders) {
                        System.out.println("Error Message: " + e.getMessage());
                    }
                };

        // Example: Generate three new pass objects
        for (int i = 0; i < 3; i++) {
            // Generate a random object suffix
            String objectSuffix = UUID.randomUUID().toString().replaceAll("[^\\w.-]", "_");

            // See link below for more information on required properties
            // https://developers.google.com/wallet/generic/rest/v1/genericobject
            GenericObject batchObject =
                    new GenericObject()
                            .setId(String.format("%s.%s", issuerId, objectSuffix))
                            .setClassId(String.format("%s.%s", issuerId, classSuffix))
                            .setState("ACTIVE")
                            .setHeroImage(
                                    new Image()
                                            .setSourceUri(
                                                    new ImageUri()
                                                            .setUri(
                                                                    "https://farm4.staticflickr.com/3723/11177041115_6e6a3b6f49_o.jpg"))
                                            .setContentDescription(
                                                    new LocalizedString()
                                                            .setDefaultValue(
                                                                    new TranslatedString()
                                                                            .setLanguage("en-US")
                                                                            .setValue("Hero image description"))))
                            .setTextModulesData(
                                    List.of(
                                            new TextModuleData()
                                                    .setHeader("Text module header")
                                                    .setBody("Text module body")
                                                    .setId("TEXT_MODULE_ID")))
                            .setLinksModuleData(
                                    new LinksModuleData()
                                            .setUris(
                                                    Arrays.asList(
                                                            new Uri()
                                                                    .setUri("http://maps.google.com/")
                                                                    .setDescription("Link module URI description")
                                                                    .setId("LINK_MODULE_URI_ID"),
                                                            new Uri()
                                                                    .setUri("tel:6505555555")
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
                                                                                            "http://farm4.staticflickr.com/3738/12440799783_3dc3c20606_b.jpg"))
                                                                    .setContentDescription(
                                                                            new LocalizedString()
                                                                                    .setDefaultValue(
                                                                                            new TranslatedString()
                                                                                                    .setLanguage("en-US")
                                                                                                    .setValue("Image module description"))))
                                                    .setId("IMAGE_MODULE_ID")))
                            .setBarcode(new Barcode().setType("QR_CODE").setValue("QR code value"))
                            .setCardTitle(
                                    new LocalizedString()
                                            .setDefaultValue(
                                                    new TranslatedString()
                                                            .setLanguage("en-US")
                                                            .setValue("Generic card title")))
                            .setHeader(
                                    new LocalizedString()
                                            .setDefaultValue(
                                                    new TranslatedString().setLanguage("en-US").setValue("Generic header")))
                            .setHexBackgroundColor("#4285f4")
                            .setLogo(
                                    new Image()
                                            .setSourceUri(
                                                    new ImageUri()
                                                            .setUri(
                                                                    "https://storage.googleapis.com/wallet-lab-tools-codelab-artifacts-public/pass_google_logo.jpg"))
                                            .setContentDescription(
                                                    new LocalizedString()
                                                            .setDefaultValue(
                                                                    new TranslatedString()
                                                                            .setLanguage("en-US")
                                                                            .setValue("Generic card logo"))));

            service.genericobject().insert(batchObject).queue(batch, callback);
        }

        // Invoke the batch API calls
        batch.execute();
    }

    // [START jwtNew]
    /**
     * Generate a signed JWT that creates a new pass class and object.
     *
     * <p>When the user opens the "Add to Google Wallet" URL and saves the pass to their wallet, the
     * pass class and object defined in the JWT are created. This allows you to create multiple pass
     * classes and objects in one API call when the user saves the pass to their wallet.
     *
     * @param issuerId The issuer ID being used for this request.
     * @param classSuffix Developer-defined unique ID for this pass class.
     * @param objectSuffix Developer-defined unique ID for the pass object.
     * @return An "Add to Google Wallet" link.
     */
    public String TEST(String issuerId, String classSuffix, String objectSuffix) {
        // See link below for more information on required properties
        // https://developers.google.com/wallet/generic/rest/v1/genericclass
        GenericClass newClass = new GenericClass().setId(String.format("%s.%s", issuerId, classSuffix));

        // See link below for more information on required properties
        // https://developers.google.com/wallet/generic/rest/v1/genericobject
        GenericObject newObject =
                new GenericObject()
                        .setId(String.format("%s.%s", issuerId, objectSuffix))
                        .setClassId(String.format("%s.%s", issuerId, classSuffix))
                        .setState("ACTIVE")
                        .setHeroImage(
                                new Image()
                                        .setSourceUri(
                                                new ImageUri()
                                                        .setUri(
                                                                "https://farm4.staticflickr.com/3723/11177041115_6e6a3b6f49_o.jpg"))
                                        .setContentDescription(
                                                new LocalizedString()
                                                        .setDefaultValue(
                                                                new TranslatedString()
                                                                        .setLanguage("en-US")
                                                                        .setValue("Hero image description"))))
                        .setTextModulesData(
                                List.of(
                                        new TextModuleData()
                                                .setHeader("Text module header")
                                                .setBody("Text module body")
                                                .setId("TEXT_MODULE_ID")))
                        .setLinksModuleData(
                                new LinksModuleData()
                                        .setUris(
                                                Arrays.asList(
                                                        new Uri()
                                                                .setUri("http://maps.google.com/")
                                                                .setDescription("Link module URI description")
                                                                .setId("LINK_MODULE_URI_ID"),
                                                        new Uri()
                                                                .setUri("tel:6505555555")
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
                                                                                        "http://farm4.staticflickr.com/3738/12440799783_3dc3c20606_b.jpg"))
                                                                .setContentDescription(
                                                                        new LocalizedString()
                                                                                .setDefaultValue(
                                                                                        new TranslatedString()
                                                                                                .setLanguage("en-US")
                                                                                                .setValue("Image module description"))))
                                                .setId("IMAGE_MODULE_ID")))
                        .setBarcode(new Barcode().setType("QR_CODE").setValue("QR code value"))
                        .setCardTitle(
                                new LocalizedString()
                                        .setDefaultValue(
                                                new TranslatedString().setLanguage("en-US").setValue("Generic card title")))
                        .setHeader(
                                new LocalizedString()
                                        .setDefaultValue(
                                                new TranslatedString().setLanguage("en-US").setValue("Generic header")))
                        .setHexBackgroundColor("#4285f4")
                        .setLogo(
                                new Image()
                                        .setSourceUri(
                                                new ImageUri()
                                                        .setUri(
                                                                "https://storage.googleapis.com/wallet-lab-tools-codelab-artifacts-public/pass_google_logo.jpg"))
                                        .setContentDescription(
                                                new LocalizedString()
                                                        .setDefaultValue(
                                                                new TranslatedString()
                                                                        .setLanguage("en-US")
                                                                        .setValue("Generic card logo"))));

        // Create the JWT as a HashMap object
        HashMap<String, Object> claims = new HashMap<String, Object>();
        claims.put("iss", ((ServiceAccountCredentials) credentials).getClientEmail());
        claims.put("aud", "google");
        claims.put("origins", List.of("www.example.com"));
        claims.put("typ", "savetowallet");

        // Create the Google Wallet payload and add to the JWT
        HashMap<String, Object> payload = new HashMap<String, Object>();
        payload.put("genericClasses", List.of(newClass));
        payload.put("genericObjects", List.of(newObject));
        claims.put("payload", payload);

        // The service account credentials are used to sign the JWT
        Algorithm algorithm =
                Algorithm.RSA256(
                        null, (RSAPrivateKey) ((ServiceAccountCredentials) credentials).getPrivateKey());
        String token = JWT.create().withPayload(claims).sign(algorithm);

        System.out.println("Add to Google Wallet link");
        System.out.printf("https://pay.google.com/gp/v/save/%s%n", token);

        return String.format("https://pay.google.com/gp/v/save/%s", token);
    }
    // [END jwtNew]

    public boolean doesPassExist(int userId) throws Exception {

        User user = userService.getUserById((long) userId);
        Pass pass = user.getPass();

        // Check if the object exists
        try {
            service.genericobject().get(String.format("%s.%s", issuerId, user.getEmail())).execute();
        } catch (GoogleJsonResponseException ex) {
            if (ex.getStatusCode() == 400) {
                // Object does not exist
                user.setIsGoogleWalletPassAdded(false);
                userRepository.save(user);
                return false;
            } else {
                // Something else went wrong...
                log.error(ex.getMessage());
                throw new Exception("Something went wrong GoogleWalletService::doesPassExist()");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        user.setIsGoogleWalletPassAdded(true);
        userRepository.save(user);
        return true;
    }
}
