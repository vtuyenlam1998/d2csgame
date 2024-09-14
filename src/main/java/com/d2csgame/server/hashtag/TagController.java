package com.d2csgame.server.hashtag;

import com.d2csgame.model.response.ResponseData;
import com.d2csgame.model.response.ResponseError;
import com.d2csgame.server.hashtag.model.request.CreateTagReq;
import com.d2csgame.server.hashtag.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tag")
public class TagController {
    private final TagService tagService;
    @PostMapping
    public ResponseData<?> createAndEditTag(@RequestBody final CreateTagReq tag) {
        try {
            tagService.createAndEditTag(tag);
            return new ResponseData<>(HttpStatus.CREATED.value(),"tag created successfully");
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}
