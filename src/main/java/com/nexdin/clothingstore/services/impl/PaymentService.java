package com.nexdin.clothingstore.services.impl;

import com.nexdin.clothingstore.domain.Payments;
import com.nexdin.clothingstore.domain.enums.EPaymentStatus;
import com.nexdin.clothingstore.repository.IPaymentRepository;
import com.nexdin.clothingstore.services.IPaymentService;
import com.nexdin.clothingstore.utils.FactoryEnum;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PaymentService implements IPaymentService {

    @Autowired
    private IPaymentRepository paymentRepository;

    @Override
    public Payments getByID(String paymentID) {
        Payments payment = paymentRepository.findById(paymentID).orElseThrow(
                () -> {
                    log.warn("[getByID] - Not found payment by ID: {}", paymentID);
                    return new EntityNotFoundException("Not found payment by ID: " + paymentID);
                });
        log.info("Found payment by ID: {}", paymentID);
        return payment;
    }

    @Override
    public Payments create(Payments payments) {
        return paymentRepository.save(payments);
    }

    @Override
    public Payments confirmPayment(String paymentID, String status) {
        Payments payment = getByID(paymentID);
        payment.setPaymentStatus(FactoryEnum.getEnumInstance(EPaymentStatus.class, status));
        log.info("[confirmPayment] - Updated payment status successfully");
        return payment;
    }
}
