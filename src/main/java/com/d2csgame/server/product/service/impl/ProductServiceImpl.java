package com.d2csgame.server.product.service.impl;

import com.d2csgame.config.Translator;
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
import com.d2csgame.server.warehouse.WarehouseRepository;
import com.d2csgame.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository repository;
    private final ImageRepository imageRepository;
    private final CharacterRepository characterRepository;
    private final WarehouseRepository warehouseRepository;
    private final TagRepository tagRepository;
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;

    @Value("${file.upload-dir.product}")
    private String uploadDir;

    @Override
    @Cacheable(value = "products", key = "#pageable.pageNumber + '-' + #pageable.pageSize",
            condition = "#pageable.pageNumber == 0", unless = "#result == null")
    public PageResponse<?> findAll(Pageable pageable) {
        // Fetch from database if not cached or if it's not the first page
        Page<Product> products = repository.findAll(pageable);
        PageResponse<?> response = toPageResponse(products);

        return response;
    }

    private PageResponse<?> toPageResponse(Page<Product> products) {
        List<MainProductRes> productRes = products.stream().map(product -> {
            MainProductRes res = modelMapper.map(product, MainProductRes.class);
            Image image = imageRepository.findByActionIdAndIsPrimary(product.getId(), EActionType.PRODUCT).orElse(null);
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
        return products.stream().map(product -> {
            MainProductRes res = modelMapper.map(product, MainProductRes.class);

            Image image = imageRepository.findByActionIdAndIsPrimary(product.getId(), EActionType.PRODUCT).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocale("image.not.found")));
            if (image != null) {
                ImageRes imageRes = modelMapper.map(image, ImageRes.class);
                res.setImage(imageRes);
            } else {
                res.setImage(null);
            }
            return res;
        }).toList();
    }

    @Override
    @Cacheable(value = "productCache", key = "#id",unless = "#result == null")
    public DetailProductRes getProductById(Long id) {
        Product product = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocale("product.not.found") + " " + id));
        int quantity = warehouseRepository.getQuantityByProductId(product.getId());

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
            List<ImageRes> imageRes = images.stream().map(image -> modelMapper.map(image, ImageRes.class)).collect(Collectors.toList());
            res.setImages(imageRes);
        } else {
            res.setImages(Collections.emptyList());
        }
        res.setQuantity(quantity);
        return res;
    }

    @Transactional
    @Override
    @CacheEvict(value = "products", allEntries = true)
    public void createProduct(CreateProductReq req) throws IOException {
        Product product = modelMapper.map(req, Product.class);

        Character character = characterRepository.findById(req.getCharacterId()).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocale("character.not.found") + " " + req.getCharacterId()));
        product.setCharacter(character);

        Set<Tag> tags = new HashSet<>();
        for (Long tagId : req.getTagId()) {
            Tag tag = tagRepository.findById(tagId).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocale("tag.not.found") + " " + req.getCharacterId()));
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
    @CachePut(value = "productCache", key = "#req.id", unless="#result == null")
    public void editProduct(EditProductReq req) throws IOException {
        Product dbProduct = repository.findById(req.getId()).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocale("product.not.found") + " " + req.getCharacterId()));
        dbProduct.setName(req.getName());
        dbProduct.setDescription(req.getDescription());
        dbProduct.setPrice(req.getPrice());
        dbProduct.setDemo(req.getDemo());
        dbProduct.setProductType(req.getProductType());

        if (!dbProduct.getCharacter().getId().equals(req.getCharacterId())) {
            Character character = characterRepository.findById(req.getCharacterId()).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocale("character.not.found") + " " + req.getCharacterId()));
            dbProduct.setCharacter(character);
        }

        Set<Long> dbTagIds = dbProduct.getTags().stream().map(Tag::getId).collect(Collectors.toSet());
        if (!dbTagIds.equals(req.getTagId())) {
            Set<Tag> tags = new HashSet<>();
            for (Long tagId : req.getTagId()) {
                Tag tag = tagRepository.findById(tagId).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocale("tag.not.found") + " " + tagId));
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
    @CacheEvict(value = "productCache", beforeInvocation = false, key = "#id")
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public PageResponse<?> getProductByCategory(Long tagId, Long characterId, Pageable pageable) {
        Page<Product> products = productRepository.findByTagIdOrCharacterId(tagId, characterId, pageable);
        return toPageResponse(products);
    }

}
