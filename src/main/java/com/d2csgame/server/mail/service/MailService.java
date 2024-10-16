package com.d2csgame.server.mail.service;

import com.d2csgame.model.request.DataMailDTO;
import com.d2csgame.utils.FileUtils;
import com.google.gson.Gson;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class MailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    @Value("${spring.mail.from}")
    private String emailFrom;

    @KafkaListener(topics = "send-email-topic",groupId = "send-email-group")
    public String sendEmail(String message) throws MessagingException, IOException {
        log.info("Sending email...");
        Gson gson = new Gson();
        DataMailDTO dto = gson.fromJson(message, DataMailDTO.class);
        // Tải file từ đường dẫn đã nhận
        File filePath = new File(dto.getFilePath());
        if (!filePath.exists()) {
            throw new FileNotFoundException("File not found: " + dto.getFilePath());
        }
        MultipartFile[] multipartFile = FileUtils.convertFileToMultipartFile(filePath);
        dto.setFiles(multipartFile);

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
        helper.setFrom(emailFrom, "D2CSGAME");
        if (dto.getRecipients().contains(",")) {
            helper.setTo(InternetAddress.parse(dto.getRecipients()));
        } else {
            helper.setTo(dto.getRecipients());
        }
        if (dto.getFiles() != null) {
            for (MultipartFile file : dto.getFiles()) {
                helper.addAttachment(Objects.requireNonNull(file.getOriginalFilename()), file);
            }
        }
        helper.setSubject(dto.getSubject());
        helper.setText(dto.getContent(), true);
        mailSender.send(mimeMessage);
        log.info("Email has been send to recipients info: {}", dto.getRecipients());
        return "Sent email successfully";
    }

    public String sendEmail(DataMailDTO dto) throws MessagingException, UnsupportedEncodingException {
        log.info("Sending email...");

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
        helper.setFrom(emailFrom, "D2CSGAME");
        if (dto.getRecipients().contains(",")) {
            helper.setTo(InternetAddress.parse(dto.getRecipients()));
        } else {
            helper.setTo(dto.getRecipients());
        }
        if (dto.getFiles() != null) {
            for (MultipartFile file : dto.getFiles()) {
                helper.addAttachment(Objects.requireNonNull(file.getOriginalFilename()), file);
            }
        }
        helper.setSubject(dto.getSubject());
        helper.setText(dto.getContent(), true);
        mailSender.send(mimeMessage);
        log.info("Email has been send to recipients info: {}", dto.getRecipients());
        return "Sent email successfully";
    }

    @KafkaListener(topics = "confirm-account-topic",groupId = "confirm-account-group")
    public void sendConfirmationEmailWithKafka(String message) throws MessagingException, UnsupportedEncodingException {
        log.info("Sending link confirm to user...");
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        Map<String, String> messageMap = gson.fromJson(message, type);

        String email = messageMap.get("email");
        String userId = messageMap.get("userId");
        String secretCode = messageMap.get("secretCode");

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        Context context = new Context();
        String linkConfirm = String.format("http://localhost:8080/api/v1/auth/confirm/%s/?secretCode=%s", userId, secretCode);

        Map<String, Object> properties = new HashMap<>();
        properties.put("linkConfirm", linkConfirm);
        context.setVariables(properties);

        helper.setFrom(emailFrom, "D2CSGAME");
        helper.setTo(email);
        helper.setSubject("Please confirm your account");
        String html = templateEngine.process("confirm-email.html", context);
        helper.setText(html, true);
        mailSender.send(mimeMessage);
        log.info("Link confirm sent to recipients email: {}, linkConfirm: {}", email, linkConfirm);

    }
}
