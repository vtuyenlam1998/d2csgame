package com.d2csgame.server.product.model.response;

import com.d2csgame.entity.enumration.EProductType;
import com.d2csgame.server.character.model.response.CharacterRes;
import com.d2csgame.server.hashtag.model.response.TagRes;
import com.d2csgame.server.image.model.response.ImageRes;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class DetailProductRes {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Set<TagRes> tags = new HashSet<>();
    private String demo;
    private EProductType productType;
    private CharacterRes character;
    private List<ImageRes> images = new ArrayList<>();
    private int quantity;
}
