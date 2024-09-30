package com.d2csgame.server.cart;

import com.d2csgame.model.response.ResponseData;
import com.d2csgame.model.response.ResponseError;
import com.d2csgame.server.cart.model.request.AddToCartReq;
import com.d2csgame.server.cart.model.response.CartRes;
import com.d2csgame.server.cart.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
@Tag(name = "Cart", description = "APIs related to cart operations")
public class CartController {
    private final CartService cartService;

    @ResponseStatus(CREATED)
    @Operation(summary = "Add product to cart",
            description = "This API adds a product to the user's active cart.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product added to cart successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseData.class),
                            examples = @ExampleObject(
                                    name = "Success Response",
                                    value = "{\"status\": 201, \"message\": \"Product added to cart successfully\"}"
                            ))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseError.class),
                            examples = @ExampleObject(
                                    name = "Error Response",
                                    value = "{\"status\": 400, \"error\": \"Invalid product ID\"}"
                            )))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "AddToCart Request", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AddToCartReq.class),
                    examples = @ExampleObject(
                            name = "AddToCart Example",
                            value = "{\"productId\": 1, \"quantity\": 2}"
                    ))
    )
    @PostMapping("/addToCart")
    public ResponseData<?> addToCart(@RequestBody AddToCartReq req) {
        try {
            cartService.addToCart(req);
            return new ResponseData<>(HttpStatus.CREATED.value(), "Product added to cart successfully");
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());

        }
    }

    @ResponseStatus(OK)
    @Operation(summary = "View active cart",
            description = "This API returns the active cart for the user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart fetched successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseData.class),
                            examples = @ExampleObject(
                                    name = "Success Response",
                                    value = "{\"status\": 200, \"message\": \"Show current cart fetch successfully\", \"data\": {\"items\": []}}"
                            ))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseError.class),
                            examples = @ExampleObject(
                                    name = "Error Response",
                                    value = "{\"status\": 400, \"error\": \"Cart not found\"}"
                            )))
    })
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
