package com.d2csgame.server.character.service;

import com.d2csgame.server.character.model.request.CreateCharacterReq;

public interface CharacterService {
    void createAndEditCharacter(CreateCharacterReq req);
}
