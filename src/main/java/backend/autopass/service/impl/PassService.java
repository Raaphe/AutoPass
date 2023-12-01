package backend.autopass.service.impl;

import backend.autopass.model.repositories.PassRepository;
import backend.autopass.service.IPassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PassService implements IPassService {

    private final PassRepository passRepository;

    @Autowired
    public PassService(PassRepository passRepository) {
        this.passRepository = passRepository;
    }
}
