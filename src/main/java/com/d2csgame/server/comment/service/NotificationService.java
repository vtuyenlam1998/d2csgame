package com.d2csgame.server.comment.service;

import com.d2csgame.entity.User;

public interface NotificationService {
    void sendNotification(User taggedUser, String s);
}
