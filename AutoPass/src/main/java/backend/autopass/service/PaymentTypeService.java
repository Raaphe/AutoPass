package backend.autopass.service;

import backend.autopass.model.entities.PaymentType;
import backend.autopass.model.entities.User;
import backend.autopass.model.repositories.PaymentTypeRepository;
import backend.autopass.model.repositories.UserRepository;
import backend.autopass.payload.dto.SavePaymentTypeDTO;
import backend.autopass.service.interfaces.IPaymentTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * PaymentTypeService - 2024-03-30
 * Raph
 * Payment Type Service method implementation.
 * AutoPass
 */
@Service
@RequiredArgsConstructor
public class PaymentTypeService implements IPaymentTypeService {

    private final PaymentTypeRepository paymentTypeRepository;
    private final UserRepository userRepository;

    @Override
    public List<PaymentType> getAllUserPaymentTypes(int userId) {
        try {
            List<PaymentType> paymentTypes = (List<PaymentType>) paymentTypeRepository.getAllByUserId(userId);
            paymentTypes.removeIf(PaymentType::isDeleted);
            return paymentTypes;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PaymentType getPaymentType(int paymentTypeId) {
        try {
            Optional<PaymentType> paymentType = paymentTypeRepository.getPaymentTypeById(paymentTypeId);
            if (paymentType.isPresent()) {
                if (!paymentType.get().isDeleted()) {
                    return paymentType.get();
                }
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean savePaymentType(SavePaymentTypeDTO paymentTypeDTO) {

        try {
            PaymentType paymentType;
            Optional<User> user = userRepository.getUserById(paymentTypeDTO.getUserId());
            if (user.isEmpty()) {
                throw new Exception("User cannot be null");
            }
            if (paymentTypeDTO.getPaymentTypeId() == -1) {
                paymentType = PaymentType
                        .builder()
                        .pAN(paymentTypeDTO.getPAN())
                        .user(user.get())
                        .isDeleted(false)
                        .cVV(paymentTypeDTO.getCVV())
                        .build();

                paymentTypeRepository.save(paymentType);
                return true;
            }

            paymentType = PaymentType
                    .builder()
                    .id(paymentTypeDTO.getPaymentTypeId())
                    .pAN(paymentTypeDTO.getPAN())
                    .user(user.get())
                    .isDeleted(false)
                    .cVV(paymentTypeDTO.getCVV())
                    .build();

            paymentTypeRepository.save(paymentType);
            return true;
        } catch (Exception e) {
            return false;
        }


    }

    @Override
    public boolean deletePaymentType(int paymentMethodId) {
        try {
            Optional<PaymentType> paymentType = paymentTypeRepository.getPaymentTypeById(paymentMethodId);
            if (paymentType.isEmpty()) {
                return false;
            } else {
                PaymentType concretePaymentType = paymentType.get();
                concretePaymentType.setDeleted(true);
                paymentTypeRepository.save(concretePaymentType);
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }
}
