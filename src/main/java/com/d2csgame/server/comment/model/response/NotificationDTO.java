package com.d2csgame.server.comment.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationDTO {
    private Long id;
    private String message;
    private boolean isRead;
    private Long recipientId;

}
