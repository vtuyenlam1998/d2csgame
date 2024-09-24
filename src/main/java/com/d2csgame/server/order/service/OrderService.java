package com.d2csgame.server.order.service;

import com.d2csgame.model.response.PageResponse;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    PageResponse<?> getOrderHistory(Pageable pageable);

    void checkoutCart();

}
