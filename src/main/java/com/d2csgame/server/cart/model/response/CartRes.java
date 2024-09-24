package com.d2csgame.server.cart.model.response;

import com.d2csgame.server.user.model.response.SimpleUserRes;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
public class CartRes {
    private Long id;
    private SimpleUserRes user;
    private Set<CartItemRes> items = new HashSet<>();
}
