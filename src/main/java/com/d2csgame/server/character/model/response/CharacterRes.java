package com.d2csgame.server.character.model.response;

import com.d2csgame.entity.enumration.ECharacterAttr;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CharacterRes {
    private Long id;
    private String name;
    private ECharacterAttr attribute;
}
