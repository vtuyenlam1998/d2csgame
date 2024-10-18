package com.d2csgame.server.payment;

import com.d2csgame.server.payment.service.PaypalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/payment")
@Slf4j
public class PaymentView {
    private final PaypalService paypalService;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/cancel")
    public String paymentCancel() {
        return "paymentCancel";
    }

    @GetMapping("/error")
    public String paymentError() {
        return "paymentError";
    }

}
