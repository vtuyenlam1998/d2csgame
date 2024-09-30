package com.d2csgame.model.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ResponseError", description = "Response error object")
public class ResponseError extends ResponseData {
    public ResponseError(int status, String message) {
        super(status, message);
    }
}