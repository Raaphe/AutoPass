package backend.autopass.service;

import backend.autopass.model.entities.User;
import backend.autopass.model.enums.Role;
import backend.autopass.model.repositories.UserRepository;
import backend.autopass.service.interfaces.IScannerService;
import backend.autopass.payload.dto.ScannerRegistrationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScannerService implements IScannerService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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


    private void mapDtoToScanner(User scanner, ScannerRegistrationDTO dto) {
        scanner.setPassword(passwordEncoder.encode(dto.getPwd()));
        scanner.setFirstName(dto.getRouteName());
    }
}
