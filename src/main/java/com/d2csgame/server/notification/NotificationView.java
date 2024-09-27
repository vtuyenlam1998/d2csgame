package com.d2csgame.server.notification;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/notification")
public class NotificationView {
    @GetMapping()
    public String notification() {
        return "notification";
    }
}
