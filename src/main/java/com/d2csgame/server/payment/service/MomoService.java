package com.d2csgame.server.payment.service;

import com.d2csgame.utils.IdUtils;
import com.google.gson.Gson;
import com.mservice.allinone.models.CaptureMoMoResponse;
import com.mservice.allinone.models.PayATMResponse;
import com.mservice.allinone.models.QueryStatusTransactionResponse;
import com.mservice.allinone.processor.allinone.*;
import com.mservice.pay.models.POSPayResponse;
import com.mservice.pay.models.TransactionQueryResponse;
import com.mservice.pay.models.TransactionRefundResponse;
import com.mservice.pay.processor.notallinone.POSPay;
import com.mservice.pay.processor.notallinone.TransactionQuery;
import com.mservice.pay.processor.notallinone.TransactionRefund;
import com.mservice.shared.constants.Parameter;
import com.mservice.shared.constants.RequestType;
import com.mservice.shared.exception.MoMoException;
import com.mservice.shared.sharedmodels.Environment;
import com.mservice.shared.sharedmodels.Environment.*;
import com.mservice.shared.sharedmodels.PartnerInfo;
import com.mservice.shared.utils.Encoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class MomoService {
    public static void allInOne() throws MoMoException {
        try {
            String requestId = IdUtils.generateId();
            String orderId = IdUtils.generateId();
            long amount = 50000;

            String orderInfo = "Pay With MoMo";
            String returnURL = "https://google.com.vn";
            String notifyURL = "https://google.com.vn";
            String extraData = "";
            String bankCode = "SML";
            String customerNumber = "0963181714";

            Environment environment = Environment.selectEnv(EnvTarget.DEV, ProcessType.PAY_GATE);
            CaptureMoMoResponse captureMoMoResponse = CaptureMoMo.process(environment, orderId, requestId, Long.toString(amount), orderInfo, returnURL, notifyURL, "");
            QueryStatusTransactionResponse queryStatusTransactionResponse = QueryStatusTransaction.process(environment, orderId, requestId);

//          Refund -- Manual Testing
//            RefundMoMoResponse response = RefundMoMo.process(environment, "1562135830002", orderId, "10000", "2304963912");
//            RefundStatus.process(environment, "1562135830002", "1561972787557");

            String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkpa+qMXS6O11x7jBGo9W3yxeHEsAdyDE" +
                    "40UoXhoQf9K6attSIclTZMEGfq6gmJm2BogVJtPkjvri5/j9mBntA8qKMzzanSQaBEbr8FyByHnf226dsL" +
                    "t1RbJSMLjCd3UC1n0Yq8KKvfHhvmvVbGcWfpgfo7iQTVmL0r1eQxzgnSq31EL1yYNMuaZjpHmQuT2" +
                    "4Hmxl9W9enRtJyVTUhwKhtjOSOsR03sMnsckpFT9pn1/V9BE2Kf3rFGqc6JukXkqK6ZW9mtmGLSq3" +
                    "K+JRRq2w8PVmcbcvTr/adW4EL2yc1qk9Ec4HtiDhtSYd6/ov8xLVkKAQjLVt7Ex3/agRPfPrNwIDAQAB";
            String privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCSlr6oxdLo7XXHuMEaj1bfLF4cSwB3IMTjRSheGhB/0rpq21IhyVNkwQZ+rqCYmbYGiBUm0+SO+uLn+P2YGe0DyoozPNqdJBoERuvwXIHIed/bbp2wu3VFslIwuMJ3dQLWfRirwoq98eG+a9VsZxZ+mB+juJBNWYvSvV5DHOCdKrfUQvXJg0y5pmOkeZC5PbgebGX1b16dG0nJVNSHAqG2M5I6xHTewyexySkVP2mfX9X0ETYp/esUapzom6ReSorplb2a2YYtKrcr4lFGrbDw9WZxty9Ov9p1bgQvbJzWqT0Rzge2IOG1Jh3r+i/zEtWQoBCMtW3sTHf9qBE98+s3AgMBAAECggEAQxBiU9aFgnk5HFGDTwJrDRlASRNrOBUu3odCS6MDD2e6T67daYWw+HRy4zxDTu1r4JsbijMA6wUPEG/SnWanD8f26DAcGC5vFKvZv5Ki8bQIXVzDGhr5MRS/E3lDxuEqljSPN+1+Ch6CV9r/vmN/YBV6zC1hH3IrTRPD71Jj1KMITCDQlKcDbZqgFTY0wq2ONrzQ5lF0u1sSrdnHLny2kayIAocWqSVbfcSE/9iKN4jkc2/zBQOAFgBQVPuZOdLL+rf1PTKus75aJm/TzaCcoxF496kTw/mRJ77rOxB8mNDEhGULTopG0Bk12upA+QXzxsWJKm8pgv/iXV+0Hi27oQKBgQDCMAydxOCybtOnTkRQ66typlRJQDVgBCD4yhNchOd6jWk34GRY64MuNbyyrD8A5P/ioI4OvRs00S28Sb/G/w3ldciR0j7lm9FgbjkTDCrVVbp4P8gczgL+z5mPdCua1KQD+2C5RA2tMRJlAfczIVekoxCriuCQSO9RltsGT7LmEQKBgQDBP/bzTD+PKWmxeBOTLeNGH8IM63DeccWtowxRgeF1xohFK1ipi5RKxoKOVLxku0U3tKOe6thE2IhpaqYFcCRs2TFZidChyytEjD4LVlECfe9OvCqfVL8IvDUzw8B3850HYrGUh8y4Mmry3JJYLOKoAPBqEg9NLe9c8yI9rI3UxwKBgGVQjnSOMLHH8vPaePhDTUtfDqC9OFvlK5LCU8G0sdUWDKyTjad7ERE+BjqudZyw3fTO0e9MqPIwpQ0U6VMY5ZYvkrrKF/jSCDaoq2yNr5doyAZPOMgWkCeEBtl6wflhMkXFlNx0bjJLZQ6ALQpnPgPu9BacObft5bcK3zF2yZ8RAoGBAIgkYfuBKf3XdQh7yX6Ug1qxoOmtLHTpvhPXnCQH1ig811+za+D13mDXfL5838QvUlIuRl78n6PQ0DlD0vZdzKuKT4P+3SY+lZrTGhqukp+ozOCxG23oLDUhMnHnZD6dN3EujGBRU14o1sOFtOu9o2gsUTLIylLbG5hmCSdd2wWdAoGBAIvddYHkS1b8B8TCv1+CVObe5WCUvqpZgbHo3oztD0KxlgWvl+f6y8DUToK3KU9sp512Ivk43mn1Xv2QftBx8E4vyhWeltdiKOJOhMsk6djjoyb8AOuyPVumXTQBuue1yRrTKLAl1SaZnzdrKzpXsI8OBpnI0bjFxA2SNnU/iD0R";

            String partnerCode = "MOMOIQA420180417";
            String phoneNumber = "0963181714";
            String username = "nhat.nguyen";

//            orderId = IdUtils.generateId();
            PayATMResponse payATMResponse = PayATM.process(environment, requestId, orderId, bankCode, Long.toString(amount), orderInfo, returnURL, notifyURL, extraData);

//            RefundATM.process(environment, orderId, "1561972550332", "10000", "2304962904", bankCode);
//            RefundStatus.process(environment, "1562135830002", "1561972787557");

            String generateRSA = generateRSA(customerNumber, "247", "247", username, environment.getPartnerInfo().getPartnerCode(), amount, publicKey);
            System.out.println("Generate RSA: " + generateRSA);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //generate RSA signature from given information
    public static String generateRSA(String phoneNumber, String billId, String transId, String username, String partnerCode, long amount, String publicKey) throws Exception {
        // current version of Parameter key name is 2.0
        Map<String, Object> rawData = new HashMap<>();
        rawData.put(Parameter.CUSTOMER_NUMBER, phoneNumber);
        rawData.put(Parameter.PARTNER_REF_ID, billId);
        rawData.put(Parameter.PARTNER_TRANS_ID, transId);
        rawData.put(Parameter.USERNAME, username);
        rawData.put(Parameter.PARTNER_CODE, partnerCode);
        rawData.put(Parameter.AMOUNT, amount);

        Gson gson = new Gson();
        String jsonStr = gson.toJson(rawData);
        byte[] testByte = jsonStr.getBytes(StandardCharsets.UTF_8);
        String hashRSA = Encoder.encryptRSA(testByte, publicKey);

        return hashRSA;
    }

    public void pay() {
        try {
            PartnerInfo devInfo = new PartnerInfo("MOMOLRJZ20181206", "mTCKt9W3eU1m39TW", "KqBEecvaJf1nULnhPF5htpG3AMtDIOlD");
            //change the endpoint according to the appropriate processes

            String commit = RequestType.CONFIRM_APP_TRANSACTION;
            String rollback = RequestType.CANCEL_APP_TRANSACTION;
            long amount = 10000;
            String partnerRefId = String.valueOf(System.currentTimeMillis());
            String partnerTransId = "1561046083186";
            String requestId = String.valueOf(System.currentTimeMillis());
            String momoTransId = "147938695";
            String customerNumber = "0963181714";
            String partnerName = "1561046083186";
            String storeId = "1561046083186";
            String storeName = "1561046083186";
            String appData = "1561046083186";
            String description = "Pay POS Barcode";
            String paymentCode = "MM515023896957011876";

            String publicKey = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAlsL+G4UyFO0UQsQ4cAXuGYcn38d67PluKmeJqS2RcAqnNUFJjQieI5DSCyHVgAmPpUfDZ3CiSw+5NCfnjgChd/p4fq3bnGqSIw2JP78UROQDJYqfAc+WLvT29IgCH4O+P9+lOLUj2EWf8aqHxBwC1YPxtxK+8M+LKdVMAvZd3lXE3MBg9wTDYEcCNODXkNma/SfIKJCvmVWdKeKXd6IwW7yA0oTjdguAeqP8+O8jLjxJH57otRh63iX945vqAX2YAm9qzVoiDcWpv+UubRmbZ9l0moQwkdsDyCtCYPUtcW6kkdxuhlq8rg8RAVcinsz/843CBYHtqaUaAFQU1TO5EXiXT87zx/Oj2Bf4OC+iAJL/UQ4ASeL1vMoOfDSpSE8EnqKPyP+rM/H7oUaJrIin8KkrxmDLGQWKhNcTFO6UNPv3Hh13tEBv0GRy2vktL8+CWhrYHouXF2XwpS8uR/gH/Vl+5HT/HsTv/13gjSoGBQcdfyck9ZyHh5oBrQTds52C2vabCqWCEafRMbpj7lSrDWS2Df+XznR/hGkgewSdSZ/M0VK/DLadJ3x1Yhblv1HVw3jA3xzY1/zlNOZReLuvW6/kdRwJV/Zj5bd9eLJnz9jDPUcB0hAO+JuJYfTVuhZG9Beo1JbQ9+cFx+92ELn/yHDMod6rfrfBjikU9Gkxor0CAwEAAQ==";


            // Uncomment to use the processes you need
            // Make sure you are using the correct environment for each processes
            // Change to valid IDs and information to use AppPay, POS, Refund processes.

//        AppPayResponse appProcessResponse = AppPay.process(Environment.selectEnv("dev", Environment.ProcessType.APP_IN_APP), partnerRefId, partnerTransId, amount, partnerName,storeId, storeName, publicKey, customerNumber, appData, description, Parameter.VERSION, Parameter.APP_PAY_TYPE);
            POSPayResponse posProcessResponse = POSPay.process(Environment.selectEnv(Environment.EnvTarget.DEV, Environment.ProcessType.PAY_POS), partnerRefId, amount, "", "", publicKey, "", "MM943358184685515708", Parameter.VERSION, Parameter.APP_PAY_TYPE);

//        PayConfirmationResponse payConfirmationResponse = PayConfirmation.process(Environment.selectEnv("dev", Environment.ProcessType.PAY_CONFIRM), "35646", rollback, requestId, "2305062978", "", "");

            TransactionQueryResponse transactionQueryResponse = TransactionQuery.process(Environment.selectEnv("dev", Environment.ProcessType.PAY_QUERY_STATUS), "1562298553079", "1562299067267", publicKey, "", Parameter.VERSION);
            TransactionQuery.process(Environment.selectEnv("dev", Environment.ProcessType.PAY_QUERY_STATUS), "1562299067267", "1562299177871", publicKey, "", Parameter.VERSION);

            TransactionRefundResponse transactionRefundResponse = TransactionRefund.process(Environment.selectEnv("dev", Environment.ProcessType.PAY_QUERY_STATUS), "1562298553079", "", publicKey, "2305062760", amount, "", "1562299067267", Parameter.VERSION);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) throws MoMoException {
//        allInOne();
//    }
}
