package com.d2csgame.server.order.model.response;

import com.d2csgame.entity.enumration.OrderStatus;
import com.d2csgame.server.user.model.response.SimpleUserRes;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
public class OrderRes {
    private Long id;
    private SimpleUserRes user;
    private Set<OrderItemRes> items = new HashSet<>();
    private OrderStatus status;
    private LocalDateTime orderDate;
    private LocalDateTime paymentDate;
    private Double totalAmount;
}
