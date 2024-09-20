package com.d2csgame.server.hashtag.service;

import com.d2csgame.server.hashtag.model.request.CreateTagReq;

public interface TagService {
    void createAndEditTag(CreateTagReq tag);
}
