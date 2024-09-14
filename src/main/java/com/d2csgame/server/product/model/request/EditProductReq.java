package com.d2csgame.server.product.model.request;

import com.d2csgame.entity.enumration.EProductType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
public class EditProductReq {
    private Long id;
    @NotBlank(message = "Product name cannot be blank")
    @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Product description cannot be blank")
    @Size(min = 10, max = 500, message = "Product description must be between 10 and 500 characters")
    private String description;

    @NotNull(message = "Price cannot be null")
    private Double price;

    @NotNull(message = "Tag IDs cannot be null")
    @Size(min = 1, message = "At least one tag must be selected")
    private Set<Long> tagId = new HashSet<>();

    @NotBlank(message = "Demo field cannot be blank")
    @Size(min = 5, max = 255, message = "Demo field must be between 5 and 255 characters")
    private String demo;

    @NotNull(message = "Product type cannot be null")
    private EProductType productType;

    @NotNull(message = "Character ID cannot be null")
    private Long characterId;

    @NotNull(message = "Image files cannot be null")
    @Size(min = 1, message = "At least one image must be provided")
    private MultipartFile[] newImages;

    private Set<Long> oldImagesId;
}
