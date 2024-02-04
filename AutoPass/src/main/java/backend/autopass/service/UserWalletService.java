package backend.autopass.service;

import backend.autopass.model.repositories.UserWalletRepository;
import backend.autopass.service.interfaces.IUserWallet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserWalletService implements IUserWallet {

    private final UserWalletRepository userWalletRepository;

}
