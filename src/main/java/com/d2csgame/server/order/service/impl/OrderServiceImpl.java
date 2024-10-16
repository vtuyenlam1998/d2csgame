package com.d2csgame.server.order.service.impl;

import com.d2csgame.entity.Cart;
import com.d2csgame.entity.CartItem;
import com.d2csgame.entity.Image;
import com.d2csgame.entity.Order;
import com.d2csgame.entity.OrderItem;
import com.d2csgame.entity.User;
import com.d2csgame.entity.enumration.EActionType;
import com.d2csgame.entity.enumration.OrderStatus;
import com.d2csgame.exception.ResourceNotFoundException;
import com.d2csgame.model.request.DataMailDTO;
import com.d2csgame.model.response.PageResponse;
import com.d2csgame.server.cart.CartRepository;
import com.d2csgame.server.image.ImageRepository;
import com.d2csgame.server.image.model.response.ImageRes;
import com.d2csgame.server.mail.service.MailService;
import com.d2csgame.server.order.OrderRepository;
import com.d2csgame.server.order.model.response.OrderItemRes;
import com.d2csgame.server.order.model.response.OrderRes;
import com.d2csgame.server.order.service.OrderService;
import com.d2csgame.server.product.model.response.MainProductRes;
import com.d2csgame.server.user.UserRepository;
import com.d2csgame.server.user.exception.UserNotFoundException;
import com.d2csgame.utils.FileUtils;
import com.google.gson.Gson;
import com.itextpdf.text.DocumentException;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final ModelMapper modelMapper;
    private final InvoiceService invoiceService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void checkoutCart() throws DocumentException, IOException, MessagingException {
        User currentUser = userRepository.findById(1L).orElseThrow(() -> new UserNotFoundException("User not found"));
        Cart cart = cartRepository.findByUserId(currentUser.getId()).orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        Order order = new Order();
        order.setUser(currentUser);
        order.setOrderDate(LocalDateTime.now());
        order.setPaymentDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PAID);
        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPrice());

            order.getItems().add(orderItem);
        }
        order.setTotalAmount(calculateTotal(cart));
        orderRepository.save(order);
        cartRepository.delete(cart);
        String filePath = invoiceService.generateInvoice(order);
        DataMailDTO dto = new DataMailDTO();
        dto.setSubject("Order Invoice");
        dto.setContent("Dear customer,\n\nPlease find attached your order invoice.");
        dto.setRecipients(currentUser.getEmail());
        dto.setFilePath(filePath);
        Gson gson = new Gson();
        String jsonMessage = gson.toJson(dto);
        kafkaTemplate.send("send-email-topic", jsonMessage);
    }

    private Double calculateTotal(Cart cart) {
        return cart.getItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    @Override
    public PageResponse<?> getOrderHistory(Pageable pageable) {
        User currentUser = userRepository.findById(1L).orElseThrow(() -> new UserNotFoundException("User not found"));
        Page<Order> orders = orderRepository.findAllByUserId(currentUser.getId(), pageable);
        Set<OrderRes> orderRes = orders.getContent().stream().map(
                order -> {
                    OrderRes orderResponse = modelMapper.map(order, OrderRes.class);
                    Set<OrderItemRes> orderItemRes = orderResponse.getItems().stream().map(orderItem -> {
                        MainProductRes productRes = orderItem.getProduct();
                        Image image = imageRepository.findByActionIdAndIsPrimary(productRes.getId(), EActionType.PRODUCT).orElse(null);
                        if (image != null) {
                            ImageRes imageRes = modelMapper.map(image, ImageRes.class);
                            productRes.setImage(imageRes);
                        } else {
                            productRes.setImage(null);
                        }
                        orderItem.setProduct(productRes);
                        return orderItem;
                    }).collect(Collectors.toSet());
                    orderResponse.setItems(orderItemRes);
                    return orderResponse;
                }
        ).collect(Collectors.toSet());
        return PageResponse.builder()
                .total(orders.getTotalElements())
                .items(orderRes)
                .hasPrevious(orders.hasPrevious())
                .page(orders.getNumber())
                .hasNext(orders.hasNext())
                .size(orders.getSize())
                .build();
    }
}
