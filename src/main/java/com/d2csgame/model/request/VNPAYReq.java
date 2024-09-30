package com.d2csgame.model.request;

import com.d2csgame.entity.enumration.EBankCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VNPAYReq {
    private String orderInfo; //Thông tin mô tả nội dung thanh toán quy định dữ liệu gửi sang VNPAY (Tiếng Việt không dấu và không bao gồm các ký tự đặc biệt)
    private Long amount;
    private String bankCode;

}
