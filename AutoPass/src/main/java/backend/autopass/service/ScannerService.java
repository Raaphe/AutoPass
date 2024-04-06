package backend.autopass.service;

import backend.autopass.model.entities.User;
import backend.autopass.model.entities.UserWallet;
import backend.autopass.model.enums.Role;
import backend.autopass.model.repositories.UserRepository;
import backend.autopass.model.repositories.UserWalletRepository;
import backend.autopass.payload.dto.ScannerRegistrationDTO;
import backend.autopass.payload.viewmodels.PassValidationResponseViewModel;
import backend.autopass.service.interfaces.IScannerService;
import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.security.InvalidKeyException;
import java.security.Key;
import java.time.Duration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.time.temporal.Temporal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ScannerService - 2024-03-30
 * Raph
 * Scanner service method implementation.
 * AutoPass
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ScannerService implements IScannerService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final GoogleWalletService googleWalletService;
    private final UserWalletRepository userWalletRepository;

    @Value("${rotating-barcode-hmac-key}")
    private String key;

    @Override
    public Collection<User> getAllScanner() {
        return userRepository.getAllScanners();
    }

    @Override
    public User registerScanner(ScannerRegistrationDTO dto) {

        try {

            // Checks if bus already exists
            // If so, saves new values. (UPDATE)
            User scanner = userRepository.getBusByBusNumber(dto.getBusNumber());
            if (scanner != null) {
                mapDtoToScanner(scanner, dto);
                return userRepository.save(scanner);
            }

            /// autopass_scanner_counter_busNum@gmail.com -- case if email already picked
            /// else
            /// autopass_scanner_busNum@gmail.com
            /// in any case, the bus number will always be between the last underscore and the `@`.

            String emailProvider = "@gmail.com";
            String emailPrefix = "autopass_scanner";

            String email = emailPrefix + "_" + dto.getBusNumber() + emailProvider;

            String uniqueEmail = email;
            boolean isEmailUnique = false;
            int uniqueCounter = 0;

            while (!isEmailUnique) {

                Optional<User> tempUser = userRepository.findByEmail(email);
                if (tempUser.isPresent()) {
                    uniqueEmail =

                            emailPrefix +
                            "_" +
                            uniqueCounter
                            +
                            "_"
                            +
                            dto.getBusNumber() +
                            emailProvider;

                    uniqueCounter++;
                } else {
                    isEmailUnique = true;
                }

            }

            scanner = User
                    .builder()
                    .password(passwordEncoder.encode(dto.getPwd()))
                    .email(uniqueEmail)
                    .firstName(dto.getRouteName())
                    .lastName("")
                    .role(Role.SCANNER_USER)
                    .build();

            return userRepository.save(scanner);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public int getBusNumberFromScannerEmail(String email) {
        try {
            String pattern = ".*_(\\d+)@";

            Pattern regexPattern = Pattern.compile(pattern);
            Matcher matcher = regexPattern.matcher(email);

            if (matcher.find()) {
                return Integer.parseInt(matcher.group(1));
            } else {
                return -1;
            }
        } catch (Exception e) {
            return -1;
        }
    }

    @Override
    public Boolean deletedBus(Integer busNumber) {
        try {
            User scanner = userRepository.getBusByBusNumber(busNumber);
            scanner.setDeleted(true);
            userRepository.save(scanner);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public User getBusFromBusNumber(int busNumber) {
        User scanner = userRepository.getBusByBusNumber(busNumber);

        if (scanner == null) {
            return User.builder().role(Role.SCANNER_USER).firstName("").email("").build();
        }

        else return scanner;
    }

    @Override
    public PassValidationResponseViewModel validatePass(String rotatingBarcodeValue) throws InvalidKeyException {

        // Time here is used in seconds, thus the division by 1000.
        int currentTime = (int) (System.currentTimeMillis() / 1000);
        List<String> tOTPDetails = List.of(rotatingBarcodeValue.split("-"));
        String email = tOTPDetails.get(0);
        long timeStamp = Integer.parseInt(tOTPDetails.get(1));
        String tOTPValue = tOTPDetails.get(2);

        TimeBasedOneTimePasswordGenerator generator = new TimeBasedOneTimePasswordGenerator(Duration.ofSeconds(6), 8);
        Instant timeCodeIssued = Instant.ofEpochSecond(timeStamp);
        Key key1 = new SecretKeySpec(hexStringToByteArray(key), "HmacSHA1");

        User user = userService.getUserByEmail(email);
        UserWallet userWallet = user.getWallet();

        if (!userWallet.isMembershipActive() && userWallet.getTicketAmount() <= 0) {
            return PassValidationResponseViewModel
                    .builder()
                    .isValid(false)
                    .expiresAt(currentTime - 10) // This avoids null values in a wallet object.
                    .numberOfTickets(0)
                    .responseMessage("You have no tickets or subscriptions...")
                    .build();
        }

        if (currentTime > timeStamp + 6) {
            return PassValidationResponseViewModel
                    .builder()
                    .isValid(false)
                    .expiresAt(userWallet.getMemberShipEnds()) // This avoids null values in a wallet object.
                    .numberOfTickets(userWallet.getTicketAmount())
                    .responseMessage("Your Pass is Invalid...")
                    .build();
        } else if (tOTPValue.equals(generator.generateOneTimePasswordString(key1, timeCodeIssued))) {

            String responseMessage = "Welcome Aboard 🚐" + (userWallet.isMembershipActive() ? "" : "\nYou have " + (userWallet.getTicketAmount()-1) + " tickets remaining");
            new Thread(() -> {
                updateWalletObjects(userWallet, user);
            }).start();

            return PassValidationResponseViewModel
                    .builder()
                    .responseMessage(responseMessage)
                    .isValid(true)
                    .expiresAt(userWallet.getMemberShipEnds())
                    .numberOfTickets(userWallet.getTicketAmount())
                    .build();
        } else {
            return PassValidationResponseViewModel
                    .builder()
                    .isValid(false)
                    .expiresAt(userWallet.getMemberShipEnds()) // This avoids null values in a wallet object.
                    .numberOfTickets(userWallet.getTicketAmount())
                    .responseMessage("Your Pass is Invalid...")
                    .build();
        }
    }

    private void updateWalletObjects(UserWallet userWallet, User user) {
        if (!userWallet.isMembershipActive()) {
            int newAmount = userWallet.getTicketAmount() - 1;
            userWallet.setTicketAmount(newAmount);
            googleWalletService.updatePassTickets(user.getEmail(), newAmount);
            userWalletRepository.save(userWallet);
        }
    }

    public byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }


    private void mapDtoToScanner(User scanner, ScannerRegistrationDTO dto) {
        scanner.setPassword(passwordEncoder.encode(dto.getPwd()));
        scanner.setFirstName(dto.getRouteName());
    }
}
