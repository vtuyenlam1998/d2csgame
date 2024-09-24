package com.d2csgame.server.order;

import com.d2csgame.entity.Cart;
import com.d2csgame.model.response.PageResponse;
import com.d2csgame.model.response.ResponseData;
import com.d2csgame.model.response.ResponseError;
import com.d2csgame.server.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    @GetMapping("/order-history")
    public ResponseData<?> getOrderHistory(@PageableDefault Pageable pageable) {
        try {
            PageResponse<?> carts = orderService.getOrderHistory(pageable);
            return new ResponseData<>(HttpStatus.OK.value(), "Fetch order history successfully", carts);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());

        }
    }

    @PostMapping("/checkout")
    public ResponseData<?> checkoutCart() {
        try {
            orderService.checkoutCart();
            return new ResponseData<>(HttpStatus.OK.value(), "Check out cart successfully");
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());

        }
    }
}
