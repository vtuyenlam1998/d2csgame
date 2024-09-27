package com.d2csgame.server.notification.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationReq {
    private String message;
    private String recipient;
}
