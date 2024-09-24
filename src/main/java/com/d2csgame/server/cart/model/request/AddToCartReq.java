package com.d2csgame.server.cart.model.request;

import lombok.Getter;

@Getter
public class AddToCartReq {
    private Long productId;
    private int quantity;
}
