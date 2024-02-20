package backend.autopass.service.interfaces;

import backend.autopass.model.entities.UserWallet;

public interface IUserWallet {

    /**
     * Gets the user's wallet's information.
     *
     * @param userId The target user's ID.
     * @return The user wallet entity.
     */
    UserWallet getUserWalletInfo(int userId);
}
