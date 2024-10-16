//package com.d2csgame.server.comment.service.impl;
//
//import com.d2csgame.entity.Comment;
//import com.d2csgame.entity.User;
//import com.d2csgame.server.comment.CommentRepository;
//import com.d2csgame.server.comment.model.response.CommentDTO;
//import com.d2csgame.server.comment.service.CommentService;
//import com.d2csgame.server.comment.service.NotificationService;
//import com.d2csgame.server.user.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.HashSet;
//import java.util.List;
//
//@Service
//public class CommentServiceImpl implements CommentService {
//    @Autowired
//    private CommentRepository commentRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private NotificationService notificationService;
//
//    public Comment saveComment(CommentDTO commentDTO) {
//        Comment comment = new Comment();
//        comment.setContent(commentDTO.getContent());
//
//        User author = userRepository.findById(commentDTO.getAuthorId()).orElseThrow();
//        comment.setAuthor(author);
//
//        if (commentDTO.getParentCommentId() != null) {
//            Comment parentComment = commentRepository.findById(commentDTO.getParentCommentId()).orElseThrow();
//            comment.setParentComment(parentComment);
//        }
//
//        List<User> taggedUsers = userRepository.findAllById(commentDTO.getTaggedUserIds());
//        comment.setTaggedUsers(new HashSet<>(taggedUsers));
//
//        Comment savedComment = commentRepository.save(comment);
//
//        // Tạo thông báo cho các taggedUsers
//        for (User taggedUser : taggedUsers) {
//            notificationService.sendNotification(taggedUser, "You were tagged in a comment.");
//        }
//
//        return savedComment;
//    }
//}
