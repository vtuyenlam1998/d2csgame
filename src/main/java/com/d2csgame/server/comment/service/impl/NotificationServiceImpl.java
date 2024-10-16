//package com.d2csgame.server.comment.service.impl;
//
//import com.d2csgame.entity.Notification;
//import com.d2csgame.entity.User;
//import com.d2csgame.server.comment.service.NotificationService;
//import com.d2csgame.server.notification.NotificationRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Service;
//
//@Service
//public class NotificationServiceImpl implements NotificationService {
//    @Autowired
//    private NotificationRepository notificationRepository;
//
//    @Autowired
//    private SimpMessagingTemplate messagingTemplate;
//
//    public void sendNotification(User recipient, String message) {
//        Notification notification = new Notification();
//        notification.setRecipient(recipient);
//        notification.setMessage(message);
//        notification.setRead(false);
//
//        Notification savedNotification = notificationRepository.save(notification);
//
//        // Gửi thông báo tới client qua WebSocket
//        messagingTemplate.convertAndSendToUser(recipient.getUsername(), "/queue/notifications", savedNotification);
//    }
//}
