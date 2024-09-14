package com.d2csgame.api;

import com.d2csgame.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/search")
public class SearchApi {
    @Autowired
    private SearchService searchService;
    @GetMapping(value = "/global")
    public ResponseEntity<Map<String, Object>> searchGlobal(@Param("keyword") String keyword) {
        return ResponseEntity.ok(searchService.searchGlobal(keyword));
    }
}
