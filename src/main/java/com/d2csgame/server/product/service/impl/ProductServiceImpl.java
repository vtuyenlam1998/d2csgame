package com.d2csgame.server.product.service.impl;

import com.d2csgame.entity.Character;
import com.d2csgame.entity.Image;
import com.d2csgame.entity.Product;
import com.d2csgame.entity.Tag;
import com.d2csgame.entity.enumration.EActionType;
import com.d2csgame.exception.ResourceNotFoundException;
import com.d2csgame.model.response.PageResponse;
import com.d2csgame.server.character.CharacterRepository;
import com.d2csgame.server.character.model.response.CharacterRes;
import com.d2csgame.server.hashtag.TagRepository;
import com.d2csgame.server.hashtag.model.response.TagRes;
import com.d2csgame.server.image.ImageRepository;
import com.d2csgame.server.image.model.response.ImageRes;
import com.d2csgame.server.product.model.request.CreateProductReq;
import com.d2csgame.server.product.model.request.EditProductReq;
import com.d2csgame.server.product.model.response.DetailProductRes;
import com.d2csgame.server.product.model.response.MainProductRes;
import com.d2csgame.server.product.ProductRepository;
import com.d2csgame.server.product.service.ProductService;
import com.d2csgame.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository repository;
    private final ImageRepository imageRepository;
    private final CharacterRepository characterRepository;
    private final TagRepository tagRepository;
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;

    @Value("${file.upload-dir.product}")
    private String uploadDir;

    @Override
    public PageResponse<?> findAll(Pageable pageable) {
        Page<Product> products = repository.findAll(pageable);
        List<MainProductRes> productRes = products.stream()
                .map(product -> {
                    MainProductRes res = modelMapper.map(product, MainProductRes.class);

                    if (product.getCharacter() != null) {
                        CharacterRes characterRes = modelMapper.map(product.getCharacter(), CharacterRes.class);
                        res.setCharacter(characterRes);
                    } else {
                        res.setCharacter(null);
                    }

                    Image image = imageRepository.findByActionIdAndIsPrimary(product.getId(), EActionType.PRODUCT).orElseThrow(() -> new ResourceNotFoundException("Image not found"));
                    if (image != null) {
                        ImageRes imageRes = modelMapper.map(image, ImageRes.class);
                        res.setImage(imageRes);
                    } else {
                        res.setImage(null);
                    }

                    return res;
                }).toList();
        return PageResponse.builder()
                .items(productRes)
                .total(products.getTotalElements())
                .size(products.getSize())
                .page(products.getNumber())
                .hasNext(products.hasNext())
                .hasPrevious(products.hasPrevious())
                .build();
    }

    @Override
    public List<MainProductRes> findAll(String keyword) {
        List<Product> products = repository.findAll(keyword);
        List<MainProductRes> productRes = products.stream()
                .map(product -> {
                    MainProductRes res = modelMapper.map(product, MainProductRes.class);

                    Image image = imageRepository.findByActionIdAndIsPrimary(product.getId(), EActionType.PRODUCT).orElseThrow(() -> new ResourceNotFoundException("Image not found"));
                    if (image != null) {
                        ImageRes imageRes = modelMapper.map(image, ImageRes.class);
                        res.setImage(imageRes);
                    } else {
                        res.setImage(null);
                    }
                    return res;
                }).toList();
        return productRes;
    }

    @Override
    public DetailProductRes getProductById(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm " + id));

        DetailProductRes res = modelMapper.map(product, DetailProductRes.class);

        if (product.getTags() != null && !product.getTags().isEmpty()) {
            Set<TagRes> tagRes = product.getTags().stream().map(tag -> modelMapper.map(tag, TagRes.class)).collect(Collectors.toSet());
            res.setTags(tagRes);
        } else {
            res.setCharacter(null);
        }

        if (product.getCharacter() != null) {
            CharacterRes characterRes = modelMapper.map(product.getCharacter(), CharacterRes.class);
            res.setCharacter(characterRes);
        } else {
            res.setCharacter(null);
        }

        List<Image> images = imageRepository.findByActionId(id, EActionType.PRODUCT);
        if (images != null && !images.isEmpty()) {
            List<ImageRes> imageRes = images.stream()
                    .map(image -> modelMapper.map(image, ImageRes.class))
                    .collect(Collectors.toList());
            res.setImages(imageRes);
        } else {
            res.setImages(Collections.emptyList());
        }
        return res;
    }

    @Transactional
    @Override
    public void createProduct(CreateProductReq req) throws IOException {
        Product product = modelMapper.map(req, Product.class);

        Character character = characterRepository.findById(req.getCharacterId()).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân vật " + req.getCharacterId()));
        product.setCharacter(character);

        Set<Tag> tags = new HashSet<>();
        for (Long tagId : req.getTagId()) {
            Tag tag = tagRepository.findById(tagId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân vật " + req.getCharacterId()));
            tags.add(tag);
        }
        product.setTags(tags);
        productRepository.save(product);
        for (int i = 0; i < req.getImages().length; i++) {
            MultipartFile file = req.getImages()[i];
            String filePath = FileUtils.uploadImageToFileSystem(uploadDir, file);
            Image image = new Image();
            image.setActionId(product.getId());
            image.setActionType(EActionType.PRODUCT);
            image.setPrimary(i == req.getIsPrimaryIndex()); // Set primary based on index
            image.setFilePath(filePath);
            imageRepository.save(image);
        }
    }

    @Transactional
    @Override
    public void editProduct(EditProductReq req) throws IOException {
        Product dbProduct = repository.findById(req.getId()).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm " + req.getCharacterId()));
        dbProduct.setName(req.getName());
        dbProduct.setDescription(req.getDescription());
        dbProduct.setPrice(req.getPrice());
        dbProduct.setDemo(req.getDemo());
        dbProduct.setProductType(req.getProductType());

        if (!dbProduct.getCharacter().getId().equals(req.getCharacterId())) {
            Character character = characterRepository.findById(req.getCharacterId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân vật " + req.getCharacterId()));
            dbProduct.setCharacter(character);
        }

        Set<Long> dbTagIds = dbProduct.getTags().stream().map(Tag::getId).collect(Collectors.toSet());
        if (!dbTagIds.equals(req.getTagId())) {
            Set<Tag> tags = new HashSet<>();
            for (Long tagId : req.getTagId()) {
                Tag tag = tagRepository.findById(tagId)
                        .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tag với id: " + tagId));
                tags.add(tag);
            }
            dbProduct.setTags(tags);
        }

        List<Image> existingImages = imageRepository.findByActionId(dbProduct.getId(), EActionType.PRODUCT);

        // Xóa ảnh cũ nếu không còn trong yêu cầu mới
        for (Image image : existingImages) {
            if (!(req.getOldImagesId().contains(image.getId()))) {
                imageRepository.delete(image);  // Xóa ảnh cũ khỏi DB
                FileUtils.deleteFileFromSystem(image.getFilePath());  // Xóa file khỏi hệ thống
            }
        }

        productRepository.save(dbProduct);

        for (MultipartFile file : req.getNewImages()) {
            String filePath = FileUtils.uploadImageToFileSystem(uploadDir, file);
            Image newImage = new Image();
            newImage.setActionId(dbProduct.getId());
            newImage.setActionType(EActionType.PRODUCT);
            newImage.setFilePath(filePath);
            imageRepository.save(newImage);  // Save new image in DB
        }
    }

    @Override
    public void deleteProduct(Long productId) {
        log.info("Deleted");
        productRepository.deleteById(productId);
    }

}
