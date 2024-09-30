package com.d2csgame.server.payment.service;

import com.d2csgame.config.VnpayConfig;
import com.d2csgame.model.request.VNPAYReq;
import com.d2csgame.server.payment.utils.VnPayUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class VnpayService {

    private final VnpayConfig vnpayConfig;

    public String createPaymentUrl(HttpServletRequest request, VNPAYReq req) throws Exception {
        String vnp_IpAddr = VnPayUtils.getIpAddress(request); // Địa chỉ IP
        String vnp_BankCode = req.getBankCode(); // Mã ngân hàng (nếu có)

        Map<String, String> vnpParamsMap = vnpayConfig.getVNPayConfig();
        vnpParamsMap.put("vnp_Amount", String.valueOf(req.getAmount() * 100));
        if (vnp_BankCode != null && !vnp_BankCode.isEmpty()) {
            vnpParamsMap.put("vnp_BankCode", vnp_BankCode);
        }
        vnpParamsMap.put("vnp_IpAddr", vnp_IpAddr);
        vnpParamsMap.put("vnp_OrderInfo", req.getOrderInfo()); //thông tin đơn hàng

        //build query url
        String queryUrl = VnPayUtils.getPaymentURL(vnpParamsMap, true);
        String hashData = VnPayUtils.getPaymentURL(vnpParamsMap, false);

        String vnpSecureHash = VnPayUtils.hmacSHA512(vnpayConfig.getVnpHashSecret(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        String paymentUrl = vnpayConfig.getVnpPayUrl() + "?" + queryUrl;
        return paymentUrl;
    }

}
