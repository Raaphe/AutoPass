package backend.autopass.service;

import backend.autopass.model.entities.Membership;
import backend.autopass.model.entities.Ticket;
import backend.autopass.model.entities.User;
import backend.autopass.model.entities.UserWallet;
import backend.autopass.model.repositories.MembershipRepository;
import backend.autopass.model.repositories.TicketRepository;
import backend.autopass.model.repositories.UserRepository;
import backend.autopass.model.repositories.UserWalletRepository;
import backend.autopass.payload.viewmodels.StripeSessionStatusViewModel;
import backend.autopass.service.interfaces.IStripeClient;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.LineItem;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.model.checkout.Session;
import com.stripe.net.RequestOptions;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionRetrieveParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * StripeClient - 2024-03-30
 * Raph
 * Stripe Client method implementation. (Maybe obsolete)
 * AutoPass
 */
@Service
public class StripeClient implements IStripeClient {

    @Value("${Stripe.apiKey}")
    private String key;

    @Value("${application.ip}")
    private String ip;

    @Value("${frontend.server.port}")
    private String frontendPort;

    private final GoogleWalletService googleWalletService;
    private final MembershipRepository membershipRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final UserWalletRepository userWalletRepository;

    StripeClient(GoogleWalletService googleWalletService, MembershipRepository membershipRepository, TicketRepository ticketRepository, UserRepository userRepository, UserWalletRepository userWalletRepository) {
        this.googleWalletService = googleWalletService;
        this.membershipRepository = membershipRepository;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.userWalletRepository = userWalletRepository;
        Stripe.apiKey = key;
    }

    @Override
    public String getCheckoutOptions(String priceId) throws StripeException {
        Stripe.apiKey = key;
        String domain = "http://" + ip + ":" + frontendPort;
        SessionCreateParams params =
                SessionCreateParams
                        .builder()
                        .setUiMode(SessionCreateParams.UiMode.EMBEDDED)
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setReturnUrl(domain + "/return?session_id={CHECKOUT_SESSION_ID}")
                        .addLineItem(
                                SessionCreateParams.LineItem
                                        .builder()
                                        .setQuantity(1L)
                                        .setPrice(priceId)
                                        .build()
                        )
                        .build();

        Session session = Session.create(params);
        return session.getRawJsonObject().getAsJsonPrimitive("client_secret").getAsString();
    }

    @Override
    public StripeSessionStatusViewModel getSessionStatus(String sessionId) throws StripeException {
        Session session = Session.retrieve(
                sessionId,
                SessionRetrieveParams.builder().addAllExpand(Collections.singletonList("line_items.data.price.product")).build(),
                RequestOptions.builder().build()
        );

        List<LineItem> lineItems = session.getLineItems().getData();
        String userEmail = session.getRawJsonObject().getAsJsonObject("customer_details").getAsJsonPrimitive("email").getAsString();
        Optional<User> user = userRepository.findByEmail(userEmail);


        for (LineItem lineItem : lineItems) {

            User concrecteUser;
            Price price = lineItem.getPrice();
            String priceId = price.getId();
            Membership membership = membershipRepository.getMembershipByStripePriceId(priceId);
            Ticket ticket = ticketRepository.getTicketByStripePriceId(priceId);

            // horrible logic but ay.
            if (ticket == null) {
                assert membership != null;

                if (user.isPresent()) {
                    concrecteUser = user.get();
                    UserWallet userWallet = concrecteUser.getWallet();
                    userWallet.setMembershipActive(true);
                    userWallet.setMemberShipEnds(concrecteUser.getWallet().getMemberShipEnds() + membership.getMembershipDurationDays() * 86400000);
                    userWallet.setMembershipType(membership);
                    concrecteUser.setWallet(userWallet);
                    concrecteUser = userRepository.save(concrecteUser);
                    userWalletRepository.save(userWallet);
                    googleWalletService.updatePassMembershipEnds(userEmail, concrecteUser.getWallet().getMemberShipEnds());
                }

                return StripeSessionStatusViewModel
                        .builder()
                        .status(session.getRawJsonObject().getAsJsonPrimitive("status").getAsString())
                        .customerEmail(userEmail)
                        .build();
            }


            if (user.isPresent()) {
                concrecteUser = user.get();
                UserWallet userWallet = concrecteUser.getWallet();
                userWallet.setTicketAmount(concrecteUser.getWallet().getTicketAmount() + ticket.getTicketAmount());
                concrecteUser.setWallet(userWallet);
                concrecteUser = userRepository.save(concrecteUser);
                userWalletRepository.save(userWallet);
                googleWalletService.updatePassTickets(userEmail, concrecteUser.getWallet().getTicketAmount());
            }

            return StripeSessionStatusViewModel
                    .builder()
                    .status(session.getRawJsonObject().getAsJsonPrimitive("status").getAsString())
                    .customerEmail(userEmail)
                    .build();
        }

        return null;
    }


}
