package backend.autopass.model.data;

import backend.autopass.model.entities.Membership;
import backend.autopass.model.entities.Ticket;
import backend.autopass.model.enums.Role;
import backend.autopass.model.repositories.MembershipRepository;
import backend.autopass.model.repositories.TicketRepository;
import backend.autopass.model.repositories.UserRepository;
import backend.autopass.payload.dto.SignUpDTO;
import backend.autopass.service.AuthenticationService;
import backend.autopass.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class DataLoader {

    private final TicketRepository ticketRepository;
    private final MembershipRepository membershipRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostConstruct
    public void loadData() throws Exception {

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


            Membership membership1 = Membership
                    .builder()
                    .price(11f)
                    .membershipDurationDays(1)
                    .build();

            Membership membership2 = Membership
                    .builder()
                    .price(21.25f)
                    .membershipDurationDays(3)
                    .build();

            Membership membership3 = Membership
                    .builder()
                    .price(30f)
                    .membershipDurationDays(7)
                    .build();

            Membership membership4 = Membership
                    .builder()
                    .price(97f)
                    .membershipDurationDays(30)
                    .build();

            Membership membership5 = Membership
                    .builder()
                    .price(226f)
                    .membershipDurationDays(140)
                    .build();
          
            if (membershipRepository.count() == 0) {

                membershipRepository.saveAll(new ArrayList<>(List.of(new Membership[]{membership1, membership2, membership3, membership4, membership5})));

            }

            if (ticketRepository.count() == 0) {

                Ticket ticket1 = new Ticket();
                ticket1.setTicketAmount(1);
                ticket1.setPrice(3.75);

                Ticket ticket2 = new Ticket();
                ticket2.setTicketAmount(2);
                ticket2.setPrice(7f);

                Ticket ticket3 = new Ticket();
                ticket3.setTicketAmount(10);
                ticket3.setPrice(32.50);

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
