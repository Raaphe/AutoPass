package backend.autopass.model.data;

import backend.autopass.model.entities.Membership;
import backend.autopass.model.entities.Ticket;
import backend.autopass.model.entities.TransitLog;
import backend.autopass.model.entities.User;
import backend.autopass.model.enums.Role;
import backend.autopass.model.repositories.MembershipRepository;
import backend.autopass.model.repositories.TicketRepository;
import backend.autopass.model.repositories.TransitLogRepository;
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
import java.util.Optional;

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
    private final TransitLogRepository transitLogRepository;

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

        if (transitLogRepository.count() == 0) {
            Optional<User> user = userRepository.getUserById(1);
            assert user.isPresent();
            transitLogRepository.save(TransitLog.builder().date(System.currentTimeMillis() - 1000000).user(user.get()).authorized(true).resourceType("Ticket x 1").busName("Bus Line 1").busNumber(10).build());
            transitLogRepository.save(TransitLog.builder().date(System.currentTimeMillis() - 2000000).user(user.get()).authorized(true).resourceType("Ticket x 1").busName("Bus Line 2").busNumber(20).build());
            transitLogRepository.save(TransitLog.builder().date(System.currentTimeMillis() - 3000000).user(user.get()).authorized(false).resourceType("No Tickets").busName("Bus Line 3").busNumber(30).build());
            transitLogRepository.save(TransitLog.builder().date(System.currentTimeMillis() - 4000000).user(user.get()).authorized(true).resourceType("Invalid Pass").busName("Bus Line 4").busNumber(40).build());
            transitLogRepository.save(TransitLog.builder().date(System.currentTimeMillis() - 5000000).user(user.get()).authorized(true).resourceType("Ticket x 1").busName("Bus Line 5").busNumber(50).build());
            transitLogRepository.save(TransitLog.builder().date(System.currentTimeMillis() - 6000000).user(user.get()).authorized(false).resourceType("No Membership").busName("Bus Line 6").busNumber(60).build());
            transitLogRepository.save(TransitLog.builder().date(System.currentTimeMillis() - 7000000).user(user.get()).authorized(true).resourceType("Ticket x 1").busName("Bus Line 7").busNumber(70).build());
            transitLogRepository.save(TransitLog.builder().date(System.currentTimeMillis() - 8000000).user(user.get()).authorized(false).resourceType("Invalid Pass").busName("Bus Line 8").busNumber(80).build());
            transitLogRepository.save(TransitLog.builder().date(System.currentTimeMillis() - 9000000).user(user.get()).authorized(true).resourceType("Ticket x 1").busName("Bus Line 9").busNumber(90).build());
            transitLogRepository.save(TransitLog.builder().date(System.currentTimeMillis() - 10000000).user(user.get()).authorized(false).resourceType("No Membership").busName("Bus Line 10").busNumber(100).build());
            transitLogRepository.save(TransitLog.builder().date(System.currentTimeMillis() - 11000000).user(user.get()).authorized(true).resourceType("Ticket x 1").busName("Bus Line 11").busNumber(110).build());
            transitLogRepository.save(TransitLog.builder().date(System.currentTimeMillis() - 12000000).user(user.get()).authorized(false).resourceType("Invalid Pass").busName("Bus Line 12").busNumber(120).build());
            transitLogRepository.save(TransitLog.builder().date(System.currentTimeMillis() - 13000000).user(user.get()).authorized(true).resourceType("Ticket x 1").busName("Bus Line 13").busNumber(130).build());
            transitLogRepository.save(TransitLog.builder().date(System.currentTimeMillis() - 14000000).user(user.get()).authorized(true).resourceType("No Tickets").busName("Bus Line 14").busNumber(140).build());
            transitLogRepository.save(TransitLog.builder().date(System.currentTimeMillis() - 15000000).user(user.get()).authorized(false).resourceType("No Membership").busName("Bus Line 15").busNumber(150).build());
            transitLogRepository.save(TransitLog.builder().date(System.currentTimeMillis() - 16000000).user(user.get()).authorized(true).resourceType("Ticket x 1").busName("Bus Line 16").busNumber(160).build());
            transitLogRepository.save(TransitLog.builder().date(System.currentTimeMillis() - 17000000).user(user.get()).authorized(false).resourceType("Invalid Pass").busName("Bus Line 17").busNumber(170).build());
            transitLogRepository.save(TransitLog.builder().date(System.currentTimeMillis() - 18000000).user(user.get()).authorized(true).resourceType("Ticket x 1").busName("Bus Line 18").busNumber(180).build());
            transitLogRepository.save(TransitLog.builder().date(System.currentTimeMillis() - 19000000).user(user.get()).authorized(false).resourceType("No Membership").busName("Bus Line 19").busNumber(190).build());
            transitLogRepository.save(TransitLog.builder().date(System.currentTimeMillis() - 20000000).user(user.get()).authorized(true).resourceType("Ticket x 1").busName("Bus Line 20").busNumber(200).build());
            transitLogRepository.save(TransitLog.builder().date(System.currentTimeMillis() - 21000000).user(user.get()).authorized(false).resourceType("No Tickets").busName("Bus Line 21").busNumber(210).build());
            transitLogRepository.save(TransitLog.builder().date(System.currentTimeMillis() - 22000000).user(user.get()).authorized(true).resourceType("Ticket x 1").busName("Bus Line 22").busNumber(220).build());
            transitLogRepository.save(TransitLog.builder().date(System.currentTimeMillis() - 23000000).user(user.get()).authorized(false).resourceType("Invalid Pass").busName("Bus Line 23").busNumber(230).build());
            transitLogRepository.save(TransitLog.builder().date(System.currentTimeMillis() - 24000000).user(user.get()).authorized(true).resourceType("Ticket x 1").busName("Bus Line 24").busNumber(240).build());
            transitLogRepository.save(TransitLog.builder().date(System.currentTimeMillis() - 25000000).user(user.get()).authorized(false).resourceType("No Membership").busName("Bus Line 25").busNumber(250).build());
            transitLogRepository.save(TransitLog.builder().date(System.currentTimeMillis() - 26000000).user(user.get()).authorized(true).resourceType("Ticket x 1").busName("Bus Line 26").busNumber(260).build());
            transitLogRepository.save(TransitLog.builder().date(System.currentTimeMillis() - 27000000).user(user.get()).authorized(false).resourceType("No Tickets").busName("Bus Line 27").busNumber(270).build());
            transitLogRepository.save(TransitLog.builder().date(System.currentTimeMillis() - 28000000).user(user.get()).authorized(true).resourceType("Ticket x 1").busName("Bus Line 28").busNumber(280).build());
            transitLogRepository.save(TransitLog.builder().date(System.currentTimeMillis() - 29000000).user(user.get()).authorized(false).resourceType("Invalid Pass").busName("Bus Line 29").busNumber(290).build());
            transitLogRepository.save(TransitLog.builder().date(System.currentTimeMillis() - 30000000).user(user.get()).authorized(true).resourceType("Ticket x 1").busName("Bus Line 30").busNumber(300).build());
            transitLogRepository.save(TransitLog.builder().date(System.currentTimeMillis() - 31000000).user(user.get()).authorized(false).resourceType("No Membership").busName("Bus Line 31").busNumber(310).build());
            transitLogRepository.save(TransitLog.builder().date(System.currentTimeMillis() - 32000000).user(user.get()).authorized(true).resourceType("Ticket x 1").busName("Bus Line 32").busNumber(320).build());
            transitLogRepository.save(TransitLog.builder().date(System.currentTimeMillis() - 33000000).user(user.get()).authorized(false).resourceType("No Tickets").busName("Bus Line 33").busNumber(330).build());
            transitLogRepository.save(TransitLog.builder().date(System.currentTimeMillis() - 34000000).user(user.get()).authorized(true).resourceType("Ticket x 1").busName("Bus Line 34").busNumber(340).build());
            transitLogRepository.save(TransitLog.builder().date(System.currentTimeMillis() - 35000000).user(user.get()).authorized(false).resourceType("Invalid Pass").busName("Bus Line 35").busNumber(350).build());
            transitLogRepository.save(TransitLog.builder().date(System.currentTimeMillis() - 36000000).user(user.get()).authorized(true).resourceType("Ticket x 1").busName("Bus Line 36").busNumber(360).build());
            transitLogRepository.save(TransitLog.builder().date(System.currentTimeMillis() - 37000000).user(user.get()).authorized(false).resourceType("No Membership").busName("Bus Line 37").busNumber(370).build());
            transitLogRepository.save(TransitLog.builder().date(System.currentTimeMillis() - 38000000).user(user.get()).authorized(true).resourceType("Ticket x 1").busName("Bus Line 38").busNumber(380).build());
            transitLogRepository.save(TransitLog.builder().date(System.currentTimeMillis() - 39000000).user(user.get()).authorized(false).resourceType("No Tickets").busName("Bus Line 39").busNumber(390).build());
            transitLogRepository.save(TransitLog.builder().date(System.currentTimeMillis() - 40000000).user(user.get()).authorized(true).resourceType("Ticket x 1").busName("Bus Line 40").busNumber(400).build());
            transitLogRepository.save(TransitLog.builder().date(System.currentTimeMillis() - 41000000).user(user.get()).authorized(false).resourceType("Invalid Pass").busName("Bus Line 41").busNumber(410).build());
            transitLogRepository.save(TransitLog.builder().date(System.currentTimeMillis() - 42000000).user(user.get()).authorized(true).resourceType("Ticket x 1").busName("Bus Line 42").busNumber(420).build());
            transitLogRepository.save(TransitLog.builder().date(System.currentTimeMillis() - 43000000).user(user.get()).authorized(false).resourceType("No Membership").busName("Bus Line 43").busNumber(430).build());
            transitLogRepository.save(TransitLog.builder().date(System.currentTimeMillis() - 44000000).user(user.get()).authorized(true).resourceType("Ticket x 1").busName("Bus Line 44").busNumber(440).build());
            transitLogRepository.save(TransitLog.builder().date(System.currentTimeMillis() - 45000000).user(user.get()).authorized(false).resourceType("No Tickets").busName("Bus Line 45").busNumber(450).build());
            transitLogRepository.save(TransitLog.builder().date(System.currentTimeMillis() - 46000000).user(user.get()).authorized(true).resourceType("Ticket x 1").busName("Bus Line 46").busNumber(460).build());
            transitLogRepository.save(TransitLog.builder().date(System.currentTimeMillis() - 47000000).user(user.get()).authorized(false).resourceType("Invalid Pass").busName("Bus Line 47").busNumber(470).build());
            transitLogRepository.save(TransitLog.builder().date(System.currentTimeMillis() - 48000000).user(user.get()).authorized(true).resourceType("Ticket x 1").busName("Bus Line 48").busNumber(480).build());
            transitLogRepository.save(TransitLog.builder().date(System.currentTimeMillis() - 49000000).user(user.get()).authorized(false).resourceType("No Membership").busName("Bus Line 49").busNumber(490).build());
            transitLogRepository.save(TransitLog.builder().date(System.currentTimeMillis() - 50000000).user(user.get()).authorized(true).resourceType("Ticket x 1").busName("Bus Line 50").busNumber(500).build());


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
