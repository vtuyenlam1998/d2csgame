package com.d2csgame.server.payment.service;

import com.d2csgame.server.payment.model.OrderDetail;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Details;
import com.paypal.api.payments.Item;
import com.paypal.api.payments.ItemList;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.PayerInfo;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class PaypalService {
    private final APIContext apiContext;

    private Payer getPayerInformation() {
        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        PayerInfo payerInfo = new PayerInfo();
        payerInfo.setFirstName("Vong Tuyen")
                .setLastName("Lam ")
                .setEmail("vtuyenlam1998@gmail.com");
        payer.setPayerInfo(payerInfo);
        return payer;
    }

    private List<Transaction> getTransactionInformation(OrderDetail orderDetail, String currency) {
        Details details = new Details();
        details.setShipping(String.format(Locale.forLanguageTag(currency), "%.2f", orderDetail.getShipping()));
        details.setSubtotal(String.format(Locale.forLanguageTag(currency), "%.2f", orderDetail.getSubTotal()));
        details.setTax(String.format(Locale.forLanguageTag(currency), "%.2f", orderDetail.getTax()));

        Amount amount = new Amount();
        amount.setCurrency(currency);
        amount.setTotal(String.format(Locale.forLanguageTag(currency), "%.2f", orderDetail.getTotal()));
        amount.setDetails(details);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription(orderDetail.getProductName());

        ItemList itemList = new ItemList();
        List<Item> items = new ArrayList<Item>();

        Item item = new Item();
        item.setCurrency(currency)
                .setName(orderDetail.getProductName())
                .setPrice(String.format(Locale.forLanguageTag(currency), "%.2f", orderDetail.getSubTotal()))
                .setTax(String.format(Locale.forLanguageTag(currency), "%.2f", orderDetail.getTax()))
                .setQuantity("1");

        items.add(item);
        itemList.setItems(items);
        transaction.setItemList(itemList);
        List<Transaction> listTransaction = new ArrayList<Transaction>();
        listTransaction.add(transaction);

        return listTransaction;
    }

    public Payment createPayment(
            String currency,
            String intent,
            OrderDetail orderDetail
    ) throws PayPalRESTException {
        Payer payer = getPayerInformation();

        RedirectUrls redirectUrls = getRedirectUrls();

        List<Transaction> listTransaction = getTransactionInformation(orderDetail, currency);

        Payment payment = new Payment();
        payment.setTransactions(listTransaction)
                .setRedirectUrls(redirectUrls)
                .setPayer(payer)
                .setIntent(intent);
        System.out.println(payment);
        return payment.create(apiContext);
    }

    private static RedirectUrls getRedirectUrls() {
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl("http://localhost:3000/payment/cancel");
        redirectUrls.setReturnUrl("http://localhost:3000/payment/review_payment");
        return redirectUrls;
    }

    public Payment executePayment(
            String paymentId,
            String payerId
    ) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        return payment.execute(apiContext, paymentExecution);
    }

    public Payment getPaymentDetails(String paymentId) throws PayPalRESTException {
        return Payment.get(apiContext, paymentId);
    }
}
