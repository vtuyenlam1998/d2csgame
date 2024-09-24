package com.d2csgame.server.cart;

import com.d2csgame.model.response.ResponseData;
import com.d2csgame.model.response.ResponseError;
import com.d2csgame.server.cart.model.request.AddToCartReq;
import com.d2csgame.server.cart.model.response.CartRes;
import com.d2csgame.server.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping("/addToCart")
    public ResponseData<?> addToCart(@RequestBody AddToCartReq req) {
        try {
            cartService.addToCart(req);
            return new ResponseData<>(HttpStatus.CREATED.value(), "Product added to cart successfully");
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());

        }
    }

    @GetMapping("/viewActiveCart")
    public ResponseData<?> viewActiveCart() {
        try {
            CartRes cart = cartService.viewActiveCart();
            return new ResponseData<>(HttpStatus.OK.value(), "Show current cart fetch successfully", cart);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());

        }
    }
}
