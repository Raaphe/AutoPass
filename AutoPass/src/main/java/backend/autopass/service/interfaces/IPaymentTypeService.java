package backend.autopass.service.interfaces;

import backend.autopass.model.entities.PaymentType;
import backend.autopass.payload.dto.SavePaymentTypeDTO;

import java.util.List;

public interface IPaymentTypeService {

    /**
     * Gets all of a user's payment types.
     *
     * @param userId The target user's ID.
     * @return The list of demanded payment types.
     */
    List<PaymentType> getAllUserPaymentTypes(int userId);

    /**
     * Gets the payment type by its ID.
     *
     * @param paymentTypeId The target payment type's ID.
     * @return The payment type entity.
     */
    PaymentType getPaymentType(int paymentTypeId);

    /**
     * Saves or creates a payment type based on its DTO.
     *
     * @param paymentTypeDTO The payment type dto.
     * @return Whether the object was successfully saved.
     */
    boolean savePaymentType(SavePaymentTypeDTO paymentTypeDTO);

    /**
     * Makes the payment type invisible.
     *
     * @param paymentMethodId The target payment type's ID.
     * @return Whether the payment type was deleted or not.
     */
    boolean deletePaymentType(int paymentMethodId);

}
