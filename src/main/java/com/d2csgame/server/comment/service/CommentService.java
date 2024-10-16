package com.d2csgame.server.comment.service;

import com.d2csgame.entity.Comment;
import com.d2csgame.server.comment.model.response.CommentDTO;

public interface CommentService {
    Comment saveComment(CommentDTO commentDTO);
}
