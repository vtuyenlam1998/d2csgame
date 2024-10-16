package com.d2csgame.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter // khi sử dụng form-data bắt buộc phải setter, json thì k cần
public class DataMailDTO {
    @NotBlank(message = "recipients must not be blank")
    private String recipients;
    @NotBlank(message = "subject must not be blank")
    private String subject;
    @NotBlank(message = "content must not be blank")
    private String content;
    private MultipartFile[] files;
    private String filePath;
}
