package com.d2csgame.server.notification;

import com.d2csgame.entity.Notification;
import com.d2csgame.server.notification.model.request.NotificationReq;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class NotificationController {
    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationRepository notificationRepository;
    private final ModelMapper modelMapper;

    @MessageMapping("/notification/send")
    public void sendNotification(NotificationReq notificationReq) { // Bỏ @RequestBody
        Notification notification = modelMapper.map(notificationReq, Notification.class);
        notification.setCreatedAt(LocalDateTime.now());
        notificationRepository.save(notification);

        // Kiểm tra nếu không có recipient (gửi cho tất cả)
        if (notification.getRecipient() == null || notification.getRecipient().isEmpty()) {
            messagingTemplate.convertAndSend("/topic/notifications", notification);
        } else {
            // Gửi cho 1 người nhận cụ thể
            messagingTemplate.convertAndSendToUser(notification.getRecipient(), "/queue/notifications", notification);
        }
    }

    @GetMapping("/notifications/{recipient}")
    public List<Notification> getNotifications(@PathVariable String recipient) {
        if ("all".equals(recipient)) {
            // Nếu người nhận là "all" thì tìm các notification không có recipient
            return notificationRepository.findByRecipientIsNull();
        } else {
            // Tìm các notification của recipient cụ thể
            return notificationRepository.findByRecipient(recipient);
        }
    }
}
