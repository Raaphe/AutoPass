package backend.autopass.service.interfaces;

import backend.autopass.payload.viewmodels.GoogleWalletPassURLViewModel;

import java.io.IOException;

/**
 * IGoogleWalletService - 2024-03-30
 * Raph
 * Service interface for Google Wallet Service.
 * AutoPass
 */
public interface IGoogleWalletService {

    /**
     * Create an object.
     *
     * @param userId The user's pass' ID.
     * @return an object with the url representing a pass along with the current state of the pass. Can view or save a new pass.
     */
    public GoogleWalletPassURLViewModel createObject(int userId) throws IOException ;

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
    public Boolean expireObject(String objectSuffix, String userEmail) throws IOException;

    /**
     * Updates the given user's Google Wallet object with a new number of tickets.
     * @param email The given user's email.
     * @param ticketAmount The new ticket amount. This number will not be added or removed from any existing number, but instead will replace the current amount.
     * @return Whether the operation was successful.
     */
    public Boolean updatePassTickets(String email, int ticketAmount);

    /**
     * Updates the given user's Google Wallet object's Membership Status.
     * @param email The given user's email.
     * @param endTimeInMs The time in Unix Epoch ms when the membership will expire. If this is already expired, the pass will indicate, "No Active Subscriptions".
     * @return Whether the operation was successful.
     */
    public Boolean updatePassMembershipEnds(String email, double endTimeInMs);

    /**
     * Gets the link with an existing pass object jwt to see the pass from the web.
     * @param email The target user email.
     * @param passExists If Pass doesn't exist, we do not update it, we simply create the jwt to add it.
     * @return An empty string if the user doesn't have a pass or the link.
     */
    public String getJwtForPass(String email, boolean passExists);
}
