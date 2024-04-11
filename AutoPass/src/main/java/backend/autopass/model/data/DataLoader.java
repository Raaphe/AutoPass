package backend.autopass.model.data;

import backend.autopass.model.entities.Membership;
import backend.autopass.model.entities.Ticket;
import backend.autopass.model.enums.Role;
import backend.autopass.model.repositories.MembershipRepository;
import backend.autopass.model.repositories.TicketRepository;
import backend.autopass.model.repositories.UserRepository;
import backend.autopass.payload.dto.SignUpDTO;
import backend.autopass.service.AuthenticationService;
import backend.autopass.service.ScannerService;
import backend.autopass.service.UserService;
import backend.autopass.payload.dto.ScannerRegistrationDTO;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * DataLoader - 2024-03-30
 * Raph
 * Since the database is in memory, this class loads some predefined values in the database.
 * AutoPass
 */
@Component
@AllArgsConstructor
@Slf4j
public class DataLoader {

    private final TicketRepository ticketRepository;
    private final MembershipRepository membershipRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final ScannerService scannerService;

    @PostConstruct
    public void loadData() {

        if (!userService.userExists("raphaelpaquin19@gmail.com")) {
            authenticationService.register(SignUpDTO.builder()
                    .password("2251462")
                    .firstname("raph")
                    .lastname("raph")
                    .email("raphaelpaquin19@gmail.com")
                    .role(Role.ADMIN)
                    .build());
        }

        if (!userService.userExists("aliteralpotato@gmail.com")) {
            authenticationService.register(SignUpDTO.builder()
                    .password("2251462")
                    .firstname("lam")
                    .lastname("crack")
                    .email("alitteralpotato@gmail.com")
                    .role(Role.ADMIN)
                    .build());
        }

        if (!userService.userExists("cefcurlz@gmail.com")) {
            authenticationService.register(SignUpDTO.builder()
                    .password("2133588")
                    .firstname("Cef")
                    .lastname("Taki")
                    .email("cefcurlz@gmail.com")
                    .role(Role.ADMIN)
                    .build());
        }

        scannerService.registerScanner(ScannerRegistrationDTO.builder().busNumber(18).routeName("Beaubien").pwd("abc-123").build());
        scannerService.registerScanner(ScannerRegistrationDTO.builder().busNumber(444).routeName("Marie-Victorin").pwd("abc-123").build());
        scannerService.registerScanner(ScannerRegistrationDTO.builder().busNumber(189).routeName("Notre-Dame").pwd("abc-123").build());

        Membership membership1 = Membership
                .builder()
                .price(BigDecimal.valueOf(11f))
                .membershipDurationDays(1)
                .stripePriceId("price_1P2dfqH4PBFYm1VAvyDz9BjF")
                .build();

        Membership membership2 = Membership
                .builder()
                .price(BigDecimal.valueOf(21.25f))
                .membershipDurationDays(3)
                .stripePriceId("price_1P2dhVH4PBFYm1VAbY2cjudh")
                .build();

        Membership membership3 = Membership
                .builder()
                .price(BigDecimal.valueOf(30f))
                .membershipDurationDays(7)
                .stripePriceId("price_1P2dieH4PBFYm1VAtICrFVJ0")
                .build();

        Membership membership4 = Membership
                .builder()
                .price(BigDecimal.valueOf(97f))
                .membershipDurationDays(30)
                .stripePriceId("price_1P2dkYH4PBFYm1VAF0TSjCq8")
                .build();

        Membership membership5 = Membership
                .builder()
                .price(BigDecimal.valueOf(226f))
                .membershipDurationDays(120)
                .stripePriceId("price_1P2dmbH4PBFYm1VAsppi2AtT")
                .build();
          
        if (membershipRepository.count() == 0) {
            membershipRepository.saveAll(new ArrayList<>(List.of(new Membership[]{membership1, membership2, membership3, membership4, membership5})));
        }

        if (ticketRepository.count() == 0) {

            Ticket ticket1 = Ticket
                    .builder()
                    .ticketAmount(1)
                    .price(BigDecimal.valueOf(3.75))
                    .stripePriceId("price_1P2dpNH4PBFYm1VA2RM4hAyB")
                    .build();

            Ticket ticket2 = Ticket
                    .builder()
                    .ticketAmount(2)
                    .price(BigDecimal.valueOf(7f))
                    .stripePriceId("price_1P2dpwH4PBFYm1VAQfPU5DAq")
                    .build();

            Ticket ticket3 = Ticket
                    .builder()
                    .price(BigDecimal.valueOf(32.50))
                    .stripePriceId("price_1P2drHH4PBFYm1VADzi07aET")
                    .ticketAmount(10)
                    .build();

            ticketRepository.saveAll(new ArrayList<>(List.of(new Ticket[]{ticket1, ticket2, ticket3})));
        }

        if (!userRepository.existsByEmail("william@gmail.com")) {

            userService.createUser(SignUpDTO.builder()
                    .email("william@gmail.com")
                    .firstname("will")
                    .lastname("rex")
                    .password("abc-123")
                    .build());
        }
    }
}
