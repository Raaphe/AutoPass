package backend.autopass.service.interfaces;

import backend.autopass.model.entities.User;
import backend.autopass.payload.dto.ScannerRegistrationDTO;
import backend.autopass.payload.viewmodels.PassValidationResponseViewModel;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;

/**
 * IScannerService - 2024-03-30
 * Raph
 * Service interface for Scanner Service.
 * AutoPass
 */
public interface IScannerService {


    /**
     * Gets all the scanner accounts that are not deleted.
     * @return A Collection of user Entities.
     */
    Collection<User> getAllScanner();

    /**
     * Registers a new unique scanner/bus.
     * @param dto The registration dto with form values.
     * @return The newly created bus/scanner.
     */
    User registerScanner(ScannerRegistrationDTO dto);

    /**
     * Gets a bus number from a scanner email.
     * Bus emails are auto generated and are populated inside a User Entity.
     * To avoid adding another field to User's that will be nullable and often not hold any data,
     * the bus number will live inside the scanner email since the emails are built like so:
     * `autopass_scanner_COUNTER_BUSNUMBER@gmail.com` or `autopass_scanner_BUSNUMBER@gmail.com`
     * @param email The scanner's email
     * @return The desired bus number.
     */
    int getBusNumberFromScannerEmail(String email);

    /**
     * Deleted bus by bus number.
     * @param busNumber Bus Number.
     * @return Whether bus was deleted.
     */
    Boolean deletedBus(Integer busNumber);

    /**
     * Gets a scanner account from bus Number.
     * @param busNumber The target bus number.
     * @return The bus account or an empty scanner object.
     */
    User getBusFromBusNumber(int busNumber);

    /**
     * Validates the Google wallet pass barcode value.
     * Some of the code that was used for generating the totp was taken straight from the library's <a href="https://github.com/jchambers/java-otp">Github</a>.
     * @param rotatingBarcodeValue The barcode value made like so : <USER_EMAIL>-<TIME_BARCODE_WAS_GENERATED>-<TOTP>
     * @return PassValidationResponseViewModel Whether the pass is valid.
     */
    PassValidationResponseViewModel validatePass(String rotatingBarcodeValue) throws NoSuchAlgorithmException, InvalidKeyException;
}
