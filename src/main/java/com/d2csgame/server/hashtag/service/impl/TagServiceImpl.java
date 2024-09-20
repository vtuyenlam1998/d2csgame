package com.d2csgame.server.hashtag.service.impl;

import com.d2csgame.entity.Tag;
import com.d2csgame.server.hashtag.TagRepository;
import com.d2csgame.server.hashtag.model.request.CreateTagReq;
import com.d2csgame.server.hashtag.service.TagService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    private final ModelMapper modelMapper;
    @Override
    public void createAndEditTag(CreateTagReq req) {
        Tag tag = modelMapper.map(req, Tag.class);
        tagRepository.save(tag);
    }
}
