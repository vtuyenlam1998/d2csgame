package com.d2csgame.server.image.model.response;

import com.d2csgame.entity.enumration.EActionType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class ImageRes implements Serializable {
    private Long id;
    private Long actionId;
    private EActionType actionType;
    private boolean isPrimary;
    private String filePath;
}
