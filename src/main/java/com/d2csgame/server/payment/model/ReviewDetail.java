package com.d2csgame.server.payment.model;

import com.paypal.api.payments.PayerInfo;
import com.paypal.api.payments.ShippingAddress;
import com.paypal.api.payments.Transaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReviewDetail {
    private PayerInfo payerInfo;
    private Transaction transaction;
    private ShippingAddress shippingAddress;
}
