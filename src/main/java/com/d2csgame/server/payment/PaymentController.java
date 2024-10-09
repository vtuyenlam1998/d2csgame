package com.d2csgame.server.payment;

import com.d2csgame.model.request.VNPAYReq;
import com.d2csgame.model.response.ResponseData;
import com.d2csgame.model.response.ResponseError;
import com.d2csgame.server.payment.service.PaypalService;
import com.d2csgame.server.payment.service.VnpayService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.servlet.http.HttpServletRequest;
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
    public RedirectView createPayment(@RequestParam("method") String method,
                                      @RequestParam("amount") String amount,
                                      @RequestParam("currency") String currency,
                                      @RequestParam("description") String description
    ) {
        try {
            String cancelUrl = "http://localhost:8080/payment/cancel";
            String successUrl = "http://localhost:8080/payment/success";
            Payment payment = paypalService.createPayment(
                    Double.valueOf(amount),
                    currency,
                    method,
                    "sale",
                    description,
                    cancelUrl,
                    successUrl
            );

            for (Links links : payment.getLinks()) {
                if (links.getRel().equals("approval_url")) {
                    return new RedirectView(links.getHref());
                }
            }
        } catch (PayPalRESTException e) {
            log.error("Error occurred:: ", e);
        }
        return new RedirectView("/payment/error");
    }

    @PostMapping("/vn-pay")
    public ResponseData<?> createPayment(HttpServletRequest request, @RequestBody VNPAYReq req) {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Payment successfully",vnpayService.createPaymentUrl(request, req));
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
