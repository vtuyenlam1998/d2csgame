package com.d2csgame.server.image.service;

import com.d2csgame.server.image.model.response.ImageRes;

public interface ImageService {
    void changePrimaryImage(Long imageId,Long productId);

    ImageRes getPrimaryImage(Long productId);
}
