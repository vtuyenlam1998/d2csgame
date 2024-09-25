package com.d2csgame.server.user.service.impl;

import com.d2csgame.server.mail.service.MailService;
import com.d2csgame.server.user.model.request.CreateUserReq;
import com.d2csgame.server.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    @Override
    public long register(CreateUserReq req) {
        return 0;
    }

    @Override
    public void confirmUser(long userId, String secretCode) {
        log.info("confirmUser");
    }
}
