package backend.autopass.service;

import backend.autopass.model.repositories.PassRepository;
import backend.autopass.service.interfaces.IPassService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PassService implements IPassService {

    private final PassRepository passRepository;

}
