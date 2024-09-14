package com.d2csgame.model.response;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Builder
public class PageResponse<T> implements Serializable {
    private int page;
    private int size;
    private long total;
    private T items;
    private boolean hasNext;
    private boolean hasPrevious;
}