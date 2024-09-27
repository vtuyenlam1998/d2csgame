//package com.d2csgame.server.comment;
//
//import com.d2csgame.entity.Comment;
//import com.d2csgame.entity.Notification;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.time.LocalDateTime;
//
//@RestController
//public class CommentController {
//    @MessageMapping("/comment")
//    @SendTo("/topic/comments")
//    public Comment sendComment(Comment comment) {
//        comment.setTimestamp(LocalDateTime.now());
//        return comment;
//    }
//
//    @MessageMapping("/notify")
//    @SendTo("/topic/notifications")
//    public Notification sendNotification(Notification notification) {
//        notification.setCreatedAt(LocalDateTime.now());
//        return notification;
//    }
//}
