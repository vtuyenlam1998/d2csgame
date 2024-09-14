package com.d2csgame.server.image;

import com.d2csgame.model.response.ResponseData;
import com.d2csgame.model.response.ResponseError;
import com.d2csgame.server.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/image")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;
    @PatchMapping()
    public ResponseData<?> changePrimaryImage(@RequestParam("imageId") final Long imageId, @RequestParam("productId") final Long productId) {
        try {
            imageService.changePrimaryImage(imageId,productId);
            return new ResponseData<>(HttpStatus.FOUND.value(),"change primary successfully");
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}
