package com.d2csgame.server.comment.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CommentDTO {
    private Long id;
    private String content;
    private Long authorId;
    private Long postId;
    private Long parentCommentId; // Null nếu là bình luận cấp 1
    private List<Long> taggedUserIds;

    // Constructors, Getters, Setters
}
