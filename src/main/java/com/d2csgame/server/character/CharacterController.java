package com.d2csgame.server.character;

import com.d2csgame.model.response.ResponseData;
import com.d2csgame.model.response.ResponseError;
import com.d2csgame.server.character.model.request.CreateCharacterReq;
import com.d2csgame.server.character.service.CharacterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/character")
@RequiredArgsConstructor
public class CharacterController {
    private final CharacterService characterService;
    @PostMapping()
    public ResponseData<?> createAndEditCharacter(@Valid @RequestBody final CreateCharacterReq req) {
        try {
            characterService.createAndEditCharacter(req);
            return new ResponseData<>(HttpStatus.CREATED.value(),"character created successfully");
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
    @GetMapping()
    public ResponseData<?> getCharacters() {
        try {
            return new ResponseData<>(HttpStatus.OK.value(),"characters list",characterService.findAll());
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}
