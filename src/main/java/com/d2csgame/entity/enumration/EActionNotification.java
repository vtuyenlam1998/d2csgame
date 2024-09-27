package com.d2csgame.entity.enumration;

import lombok.Getter;

@Getter
public enum EActionNotification {
    POST_LIKED("{actor} liked your article: {message}"),
    COMPONENT_LIKED("{actor} liked your component: {message}"),
    NEW_AND_REPLY_COMMENT_POST("{actor} commented on your article: {message}"),
    NEW_AND_REPLY_COMMENT_COMPONENT("{actor} commented on your component: {message}"),
    POST_SHARE("{actor} has shared your article: {postTitle}"),
    COMPONENT_SHARE("{actor} has shared your component: {componentTitle}"),

    PASSWORD_CHANGE("Your password was successfully changed on {time}. If you did not perform this action, please contact support immediately."),

    CREATE_POST("A new article titled {title} has been created successfully."),
    UPDATE_POST("The article titled {title} has been updated"),
    DELETE_POST("The article titled {title} has been deleted.");

    private String message;

    EActionNotification(String message) {
        this.message = message;
    }
}
