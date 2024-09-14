package com.d2csgame.server.character.service.impl;

import com.d2csgame.entity.Character;
import com.d2csgame.server.character.CharacterRepository;
import com.d2csgame.server.character.model.request.CreateCharacterReq;
import com.d2csgame.server.character.service.CharacterService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CharacterServiceImpl implements CharacterService {
    private final CharacterRepository characterRepository;
    private final ModelMapper modelMapper;
    @Override
    public void createAndEditCharacter(CreateCharacterReq req) {
        Character character = modelMapper.map(req, Character.class);
        characterRepository.save(character);
    }

}
