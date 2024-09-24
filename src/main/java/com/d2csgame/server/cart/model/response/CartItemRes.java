package com.d2csgame.server.cart.model.response;

import com.d2csgame.server.product.model.response.MainProductRes;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CartItemRes {
    private Long id;
    private MainProductRes product;
    private int quantity;
    private Double price;
}
