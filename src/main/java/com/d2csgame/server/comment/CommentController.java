//package com.d2csgame.server.comment;
//
//import com.d2csgame.entity.Comment;
//import com.d2csgame.entity.Notification;
//import com.d2csgame.server.comment.model.response.CommentDTO;
//import com.d2csgame.server.comment.service.CommentService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.time.LocalDateTime;
//
//@RestController
//@RequestMapping("/api/comments")
//public class CommentController {
//    @Autowired
//    private CommentService commentService;
//
//    @MessageMapping("/comment")
//    @SendTo("/topic/comments")
//    public Comment sendComment(Comment comment) {
//        return comment;
//    }
//
//    @MessageMapping("/notify")
//    @SendTo("/topic/notifications")
//    public Notification sendNotification(Notification notification) {
//        notification.setCreatedAt(LocalDateTime.now());
//        return notification;
//    }
//
//    @PostMapping
//    public ResponseEntity<Comment> addComment(@RequestBody CommentDTO commentDTO) {
//        Comment comment = commentService.saveComment(commentDTO);
//        return ResponseEntity.ok(comment);
//    }
//}
