package com.d2csgame.server.character.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CreateCharacterReq {
    private Long id;
    @NotBlank(message = "This field cannot be blank")
    private String name;
}
