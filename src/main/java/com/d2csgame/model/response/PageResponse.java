package com.d2csgame.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> implements Serializable {
    private int page;
    private int size;
    private long total;
    private T items;
    private boolean hasNext;
    private boolean hasPrevious;
}