package com.d2csgame.server.character.service.impl;

import com.d2csgame.entity.Character;
import com.d2csgame.server.character.CharacterRepository;
import com.d2csgame.server.character.model.request.CreateCharacterReq;
import com.d2csgame.server.character.model.response.CharacterRes;
import com.d2csgame.server.character.service.CharacterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CharacterServiceImpl implements CharacterService {
    private final CharacterRepository characterRepository;
    private final ModelMapper modelMapper;

    @Override
    public void createAndEditCharacter(CreateCharacterReq req) {
        Character character = modelMapper.map(req, Character.class);
        characterRepository.save(character);
    }

    @Override
    @Cacheable(value = "characterCache", key = "1")
    public List<CharacterRes> findAll() {
        log.info("Fetching characters from database");
        List<Character> characters = characterRepository.findAll();
        // Không được đổi thành .toList() sẽ gây ra lỗi trên redis vì có thể lần query đầu tiên .toList() đã làm thay đổi kiểu dữ liệu List<CharacterRes> thành unmodified List rồi nên nó sẽ không mapping được.
        return characters.stream().map(character -> modelMapper.map(character, CharacterRes.class)).collect(Collectors.toList());
    }

}
