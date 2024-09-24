package com.d2csgame.server.cart.service.impl;

import com.d2csgame.entity.Cart;
import com.d2csgame.entity.CartItem;
import com.d2csgame.entity.Image;
import com.d2csgame.entity.OrderItem;
import com.d2csgame.entity.Product;
import com.d2csgame.entity.User;
import com.d2csgame.entity.enumration.EActionType;
import com.d2csgame.exception.ResourceNotFoundException;
import com.d2csgame.server.cart.CartRepository;
import com.d2csgame.server.cart.model.request.AddToCartReq;
import com.d2csgame.server.cart.model.response.CartItemRes;
import com.d2csgame.server.cart.model.response.CartRes;
import com.d2csgame.server.cart.service.CartService;
import com.d2csgame.server.character.model.response.CharacterRes;
import com.d2csgame.server.image.ImageRepository;
import com.d2csgame.server.image.model.response.ImageRes;
import com.d2csgame.server.product.ProductRepository;
import com.d2csgame.server.product.model.response.MainProductRes;
import com.d2csgame.server.user.UserRepository;
import com.d2csgame.server.user.exception.UserNotFoundException;
import com.d2csgame.server.user.model.response.SimpleUserRes;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;
    private final ModelMapper modelMapper;

    @Override
    public void addToCart(AddToCartReq req) {
        User currentUser = userRepository.findById(1L).orElseThrow(() -> new UserNotFoundException("User not found")); // Lấy người dùng hiện tại (có thể từ SecurityContext)

        Cart cart = cartRepository.findByUserId(currentUser.getId()).orElseGet(() -> createNewActiveCartForUser(currentUser));

        Product product = productRepository.findById(req.getProductId()).orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Optional<CartItem> existingItem = cart.getItems().stream().filter(cartItem -> cartItem.getProduct().getId().equals(req.getProductId())).findFirst();
        if (existingItem.isPresent()) {
            CartItem cartItem = existingItem.get();
            cartItem.setQuantity(req.getQuantity());
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setQuantity(req.getQuantity());
            cartItem.setProduct(product);
            cartItem.setCart(cart);
            cartItem.setPrice(product.getPrice());
            cart.getItems().add(cartItem);
        }
        cartRepository.save(cart);
    }

    @Override
    public CartRes viewActiveCart() {
        User currentUser = userRepository.findById(1L).orElseThrow(() -> new UserNotFoundException("User not found"));
        Cart cart = cartRepository.findByUserId(currentUser.getId()).orElseGet(() -> createNewActiveCartForUser(currentUser));
        CartRes cartRes = modelMapper.map(cart, CartRes.class);
        cartRes.setItems(cartRes.getItems().stream().map(item -> {
            MainProductRes productRes = item.getProduct();

            Image image = imageRepository.findByActionIdAndIsPrimary(productRes.getId(), EActionType.PRODUCT).orElse(null);
            if (image != null) {
                ImageRes imageRes = modelMapper.map(image, ImageRes.class);
                productRes.setImage(imageRes);
            } else {
                productRes.setImage(null);
            }
            item.setProduct(productRes);
            return item;
        }).collect(Collectors.toSet()));
        return cartRes;
    }

    private Cart createNewActiveCartForUser(User currentUser) {
        Cart cart = new Cart();
        cart.setUser(currentUser);
        return cart;
    }

}
