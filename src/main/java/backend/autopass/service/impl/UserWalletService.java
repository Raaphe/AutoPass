package backend.autopass.service.impl;

import backend.autopass.model.repositories.UserWalletRepository;
import backend.autopass.service.IUserWallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserWalletService implements IUserWallet {

    private final UserWalletRepository userWalletRepository;

    @Autowired
    public UserWalletService(UserWalletRepository userWalletRepository) {
        this.userWalletRepository = userWalletRepository;
    }
}
