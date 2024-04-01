package backend.autopass.service;

import backend.autopass.model.repositories.PassRepository;
import backend.autopass.service.interfaces.IPassService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * PassService - 2024-03-30
 * Raph
 * Pass Service method implementation.
 * AutoPass
 */
@Service
@RequiredArgsConstructor
public class PassService implements IPassService {

    private final PassRepository passRepository;

}
