package com.d2csgame.server.user.service;

import com.d2csgame.server.user.model.request.CreateUserReq;

public interface UserService {
    long register(CreateUserReq req);

    void confirmUser(long userId, String secretCode);
}
