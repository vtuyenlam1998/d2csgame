package com.d2csgame.server.order.model.response;

import com.d2csgame.server.product.model.response.MainProductRes;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemRes {
    private Long id;
    private MainProductRes product;
    private int quantity;
    private Double price;
}
