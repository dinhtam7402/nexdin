package com.nexdin.clothingstore.services;

import com.nexdin.clothingstore.domain.Payments;

public interface IPaymentService {
    Payments getByID(String paymentID);
    Payments create(Payments payments);
    Payments confirmPayment(String paymentID, String status);
}
