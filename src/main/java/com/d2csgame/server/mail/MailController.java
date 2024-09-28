package com.d2csgame.server.mail;

import com.d2csgame.model.request.DataMailDTO;
import com.d2csgame.model.response.ResponseData;
import com.d2csgame.model.response.ResponseError;
import com.d2csgame.server.mail.service.MailService;
import com.google.gson.Gson;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/mail")
@RequiredArgsConstructor
public class MailController {
    private final MailService mailService;
    private final KafkaTemplate<String,String> kafkaTemplate;

    @PostMapping("/send")
    public ResponseData<?> sendEmail(@Valid DataMailDTO req) throws MessagingException, UnsupportedEncodingException {
        return new ResponseData<>(HttpStatus.ACCEPTED.value(), "send email successful", mailService.sendEmail(req));
    }

    @PostMapping("/sendConfirmationEmail")
    public ResponseData<?> sendConfirmationEmail(@RequestParam String email,@RequestParam String userId,@RequestParam String secretCode) throws MessagingException, UnsupportedEncodingException {

//        mailService.sendConfirmationEmail(email, userId, secretCode);
        Gson gson = new Gson();
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("email", email);
        messageMap.put("userId", userId);
        messageMap.put("secretCode", secretCode);

        String jsonMessage = gson.toJson(messageMap);
        kafkaTemplate.send("confirm-account-topic", jsonMessage);

        return new ResponseData<>(HttpStatus.ACCEPTED.value(),"send email successful");
    }
}
