package com.d2csgame.server.image.service.impl;

import com.d2csgame.config.Translator;
import com.d2csgame.entity.Image;
import com.d2csgame.entity.enumration.EActionType;
import com.d2csgame.exception.ResourceNotFoundException;
import com.d2csgame.server.image.ImageRepository;
import com.d2csgame.server.image.model.response.ImageRes;
import com.d2csgame.server.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;
    private final ModelMapper modelMapper;

    @Transactional
    @Override
    public void changePrimaryImage(Long imageId, Long productId) {
        Image image = imageRepository.findById(imageId).orElseThrow(() -> new ResourceNotFoundException("Image not found"));
        Image primaryImage = imageRepository.findByActionIdAndIsPrimary(productId, EActionType.PRODUCT).orElseThrow(() -> new ResourceNotFoundException("Primary image not found"));
        imageRepository.updatePrimary(primaryImage.getId(), false);
        imageRepository.updatePrimary(image.getId(), true);
    }

    @Override
    public ImageRes getPrimaryImage(Long productId) {
        Image image = imageRepository.findByActionIdAndIsPrimary(productId, EActionType.PRODUCT).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocale("image.not.found")));
        return modelMapper.map(image, ImageRes.class);
    }
}
