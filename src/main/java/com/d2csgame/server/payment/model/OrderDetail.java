package com.d2csgame.server.payment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class OrderDetail {
    private String productName;
    private Double subTotal;
    private Double tax;
    private Double shipping;
    private Double total;
}
