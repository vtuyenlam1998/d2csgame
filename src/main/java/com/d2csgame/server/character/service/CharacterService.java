package com.d2csgame.server.character.service;

import com.d2csgame.server.character.model.request.CreateCharacterReq;
import com.d2csgame.server.character.model.response.CharacterRes;

import java.util.List;

public interface CharacterService {
    void createAndEditCharacter(CreateCharacterReq req);

    List<CharacterRes> findAll();

}
