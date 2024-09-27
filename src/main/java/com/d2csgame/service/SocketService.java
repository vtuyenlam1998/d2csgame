package com.d2csgame.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocketService {
    private final SimpMessagingTemplate messagingTemplate;

    public void pushWebSocketToClientCreateComment(Object data) {
        log.debug("[Socket - Create comment] Socket to client with body : {}", data);
        String destination = "/topic/comment/create";
        this.pushWebSocketToClient(destination, data);
    }

    public void pushWebSocketToClient(String destination, Object data) {
        log.debug("[Socket] Push data to client by {} with body: {}", destination, data);
        messagingTemplate.convertAndSend(destination, data);
    }

    public void pushWSToClientNotificationIcon(Long userId, Object data){
        log.debug("[Socket - Notification icon] Socket to client with body : {}", data);
        String destination = "/topic/notification/global/" + userId;
        this.pushWebSocketToClient(destination, data);
    }
}
