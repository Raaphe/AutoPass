package backend.autopass.service.interfaces;

import backend.autopass.model.entities.PaymentType;

public interface IPaymentTypeService {
        PaymentType createPaymentType(PaymentType paymentType);
        PaymentType getPaymentTypeById(int id);
        PaymentType updatePaymentType(int id, PaymentType paymentTypeDetails);
        void deletePaymentType(int id);
    }

