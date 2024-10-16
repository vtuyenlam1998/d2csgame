package com.d2csgame.server.warehouse;

import com.d2csgame.config.Translator;
import com.d2csgame.model.response.ResponseData;
import com.d2csgame.model.response.ResponseError;
import com.d2csgame.server.warehouse.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/warehouse")
public class WarehouseController {
    private final WarehouseService warehouseService;
    @GetMapping("/{productId}")
    public ResponseData<?> getQuantityByProductId(@PathVariable Long productId) {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), Translator.toLocale("product.quantity.found"), warehouseService.getQuantityByProductId(productId));
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

}
