package com.d2csgame.server.auth;

import com.d2csgame.model.response.ResponseData;
import com.d2csgame.model.response.ResponseError;
import com.d2csgame.server.user.model.request.CreateUserReq;
import com.d2csgame.server.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Slf4j
@Tag(name = "Authentication API",description = "Operations related to authenticate management")
public class AuthenticateController {
    private final UserService userService;

    @Operation(summary = "Register a new user", description = "Returns User Id have created")
    @ResponseStatus(CREATED)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseData.class))),
            @ApiResponse(responseCode = "400", description = "Registration failed",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseError.class)))
    })
    @PostMapping(value = "/register")
    public ResponseData<?> register(@Valid @RequestBody CreateUserReq req) {
        log.info("Request register user: {}, {}", req.getUsername(), req.getPassword());
        try {
            long userId = userService.register(req);
            return new ResponseData<>(CREATED.value(), "register user successful",userId);
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(),e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Register user fail");

        }
    }

    @Operation(summary = "Confirm user registration", description = "Confirm user registration using a secret code")
    @ResponseStatus(ACCEPTED)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "User confirmed successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseData.class))),
            @ApiResponse(responseCode = "400", description = "User confirmation failed",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseError.class)))
    })
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
            response.sendRedirect("http://localhost:3001");
        }
    }

}
