package com.d2csgame.server.cart.service;

import com.d2csgame.server.cart.model.request.AddToCartReq;
import com.d2csgame.server.cart.model.response.CartRes;

public interface CartService {
    void addToCart(AddToCartReq req);

    CartRes viewActiveCart();

}
