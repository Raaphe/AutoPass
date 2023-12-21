package backend.autopass.model.data;

import backend.autopass.model.dto.UserDTO;
import backend.autopass.model.entities.Membership;
import backend.autopass.model.entities.Ticket;
import backend.autopass.model.repositories.MembershipRepository;
import backend.autopass.model.repositories.TicketRepository;
import backend.autopass.model.repositories.UserRepository;
import backend.autopass.service.impl.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataLoader {

    private final TicketRepository ticketRepository;
    private final MembershipRepository membershipRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public DataLoader(TicketRepository ticketRepository, MembershipRepository membershipRepository, UserRepository userRepository, UserService userService) {
        this.ticketRepository = ticketRepository;
        this.membershipRepository = membershipRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @PostConstruct
    public void loadData() throws Exception {

        if (!userService.userExists("raphaelpaquin19@gmail.com")) {
            userService.createAdmin(UserDTO.builder()
                    .pwd("2251462")
                    .username("Raphe")
                    .email("raphaelpaquin19@gmail.com")
                    .build());
        }


        if (!userService.userExists("aliteralpotato@gmail.com")) {
            userService.createAdmin(UserDTO.builder()
                    .pwd("2251462")
                    .username("Lam")
                    .email("aliteralpotato@gmail.com")
                    .build());
        }

        if (membershipRepository.count() == 0) {

            Membership membership1 = new Membership();
            membership1.setMembershipDurationDays(1);
            membership1.setPrice(11f);

            Membership membership2 = new Membership();
            membership2.setMembershipDurationDays(3);
            membership2.setPrice(21.25f);

            Membership membership3 = new Membership();
            membership3.setMembershipDurationDays(7);
            membership3.setPrice(30f);

            Membership membership4 = new Membership();
            membership4.setMembershipDurationDays(30);
            membership4.setPrice(97f);

            Membership membership5 = new Membership();
            membership5.setMembershipDurationDays(140);
            membership5.setPrice(226f);

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

            userService.createUser(UserDTO.builder()
                    .email("william@gmail.com")
                    .username("NWilliRex")
                    .pwd("abc-123")
                    .build());
        }

    }

}
