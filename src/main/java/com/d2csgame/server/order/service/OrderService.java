package com.d2csgame.server.order.service;

import com.d2csgame.model.response.PageResponse;
import com.itextpdf.text.DocumentException;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

public interface OrderService {
    PageResponse<?> getOrderHistory(Pageable pageable);

    void checkoutCart() throws DocumentException, IOException, MessagingException;

}
