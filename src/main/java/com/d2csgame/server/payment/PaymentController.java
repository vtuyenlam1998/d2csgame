package com.d2csgame.server.payment;

import com.d2csgame.model.request.VNPAYReq;
import com.d2csgame.model.response.ResponseData;
import com.d2csgame.model.response.ResponseError;
import com.d2csgame.server.payment.model.OrderDetail;
import com.d2csgame.server.payment.model.ReviewDetail;
import com.d2csgame.server.payment.service.PaypalService;
import com.d2csgame.server.payment.service.VnpayService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.PayerInfo;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.ShippingAddress;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {
    private final VnpayService vnpayService;
    private final PaypalService paypalService;

    @PostMapping("/paypal/create")
    public RedirectView createPayment(
            @RequestBody OrderDetail orderDetail,
            @RequestParam("currency") String currency
    ) {
        try {
            Payment payment = paypalService.createPayment(
                    currency,
                    "authorize",
                    orderDetail
            );

            for (Links links : payment.getLinks()) {
                if (links.getRel().equalsIgnoreCase("approval_url")) {
                    return new RedirectView(links.getHref());
                }
            }
        } catch (PayPalRESTException e) {
            log.error("Error occurred:: ", e);
        }
        return new RedirectView("http://localhost:3000/payment/error");
    }

    @GetMapping("/paypal/review_payment")
    public ResponseData<ReviewDetail> paymentSuccess(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerId
    ) {
        try {
            Payment payment = paypalService.getPaymentDetails(paymentId);

            PayerInfo payerInfo = payment.getPayer().getPayerInfo();

            Transaction transaction = payment.getTransactions().get(0);
            ShippingAddress shippingAddress = transaction.getItemList().getShippingAddress();
            ReviewDetail reviewDetail = new ReviewDetail(payerInfo, transaction, shippingAddress);
            return new ResponseData<>(HttpStatus.OK.value(), "Review Payment Details", reviewDetail);
        } catch (PayPalRESTException e) {
            log.error("Error occurred:: ", e);
        }
        return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Cant find payment details");
    }

    @PostMapping("/paypal/execute_payment")
    public ResponseData<?> executePayment(@RequestParam("paymentId") String paymentId,
                                          @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                PayerInfo payerInfo = payment.getPayer().getPayerInfo();
                Transaction transaction = payment.getTransactions().get(0);
                ReviewDetail reviewDetail = new ReviewDetail(payerInfo, transaction, null);
                return new ResponseData<>(HttpStatus.OK.value(), "Payment Details", reviewDetail);
            }
        } catch (PayPalRESTException e) {
            log.error("Error occurred:: ", e);
        }
        return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Could not execute payment");
    }

    @PostMapping("/vn-pay")
    public ResponseData<?> createPayment(HttpServletRequest request, @RequestBody VNPAYReq req) {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Payment successfully", vnpayService.createPaymentUrl(request, req));
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Bad request");
        }
    }

    @GetMapping("/vn-pay-callback")
    public ResponseData<?> payCallbackHandler(HttpServletRequest request) {
        String status = request.getParameter("vnp_ResponseCode");
        if (status.equals("00")) {
            return new ResponseData<>(HttpStatus.OK.value(), "Success");
        } else {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Failed");
        }
    }
}
