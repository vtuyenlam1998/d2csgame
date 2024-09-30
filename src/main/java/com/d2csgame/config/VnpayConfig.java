package com.d2csgame.config;

import com.d2csgame.server.payment.utils.VnPayUtils;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

@Configuration
@Getter
public class VnpayConfig {
    @Value("${vnpay.vnp_TmnCode}")
    private String vnp_TmnCode;

    @Getter
    @Value("${vnpay.vnp_HashSecret}")
    private String vnpHashSecret;

    @Getter
    @Value("${vnpay.vnp_PayUrl}")
    private String vnpPayUrl;

    @Value("${vnpay.vnp_ReturnUrl}")
    private String vnp_ReturnUrl;

    @Value("${vnpay.vnp_Version}")
    private String vnp_Version;
    @Value("${vnpay.vnp_Command}")
    private String vnp_Command;
    @Value("${vnpay.vnp_OrderType}")
    private String vnp_OrderType;

    public Map<String, String> getVNPayConfig() {
        Map<String, String> vnpParamsMap = new HashMap<>();
        vnpParamsMap.put("vnp_Version", this.vnp_Version);
        vnpParamsMap.put("vnp_Command", this.vnp_Command);
        vnpParamsMap.put("vnp_TmnCode", this.vnp_TmnCode);
        vnpParamsMap.put("vnp_CurrCode", "VND");
        vnpParamsMap.put("vnp_TxnRef",  VnPayUtils.getRandomNumber(8));
        vnpParamsMap.put("vnp_OrderType", this.vnp_OrderType); //https://sandbox.vnpayment.vn/apis/docs/loai-hang-hoa/
        vnpParamsMap.put("vnp_Locale", "vn");
        vnpParamsMap.put("vnp_ReturnUrl", this.vnp_ReturnUrl);
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnpCreateDate = formatter.format(calendar.getTime());
        vnpParamsMap.put("vnp_CreateDate", vnpCreateDate);
        calendar.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(calendar.getTime());
        vnpParamsMap.put("vnp_ExpireDate", vnp_ExpireDate);
        return vnpParamsMap;
    }
}
