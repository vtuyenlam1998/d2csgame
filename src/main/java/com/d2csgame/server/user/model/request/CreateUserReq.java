package com.d2csgame.server.user.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateUserReq {
    @NotNull(message = "id can not be null")
    private Long id;
    @NotBlank(message = "username can not be blank")
    private String username;
    @NotBlank(message = "password can not be blank")
    private String password;
    @NotBlank(message = "email can not be blank")
    private String email;
}
