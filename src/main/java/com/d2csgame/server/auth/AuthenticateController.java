package com.d2csgame.server.auth;

import com.d2csgame.model.response.ResponseData;
import com.d2csgame.model.response.ResponseError;
import com.d2csgame.server.user.model.request.CreateUserReq;
import com.d2csgame.server.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthenticateController {
    private final UserService userService;

    @PostMapping(value = "/register")
    public ResponseData<?> register(@Valid @RequestBody CreateUserReq req) {
        log.info("Request register user: {}, {}", req.getUsername(), req.getPassword());
        try {
            long userId = userService.register(req);
            return new ResponseData<>(HttpStatus.OK.value(), "register user successful",userId);
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(),e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Register user fail");

        }
    }

    @GetMapping(value = "/confirm/{userId}/")
    public ResponseData<?> confirmUser(@Min(1) @PathVariable long userId, @RequestParam String secretCode, HttpServletResponse response) throws IOException {
        log.info("Confirm user: userId={}, secretCode={}", userId, secretCode);
        try {
            userService.confirmUser(userId,secretCode);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "user confirm successful",userId);
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(),e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "user confirmation failure");
        } finally {
            response.sendRedirect("http://localhost:3000");
        }
    }

}
