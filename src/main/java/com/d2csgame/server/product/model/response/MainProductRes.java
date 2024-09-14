package com.d2csgame.server.product.model.response;

import com.d2csgame.entity.enumration.EProductType;
import com.d2csgame.server.character.model.response.CharacterRes;
import com.d2csgame.server.image.model.response.ImageRes;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MainProductRes {
    private Long id;
    private String name;
    private Double price;
    private CharacterRes character;
    private EProductType productType;
    private ImageRes image;
}
