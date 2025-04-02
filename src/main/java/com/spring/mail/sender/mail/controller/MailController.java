package com.spring.mail.sender.mail.controller;

import com.spring.mail.sender.mail.domain.EmailDto;
import com.spring.mail.sender.mail.domain.EmailFileDto;
import com.spring.mail.sender.mail.service.IEmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1")
public class MailController {

    private final IEmailService emailService;

    public MailController(IEmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/sendMessage")
    public ResponseEntity<?> receiveRequestEmail(@RequestBody EmailDto emailDto) {

        System.out.printf("Message received: %s\n", emailDto.toString());

        emailService.sendEmail(emailDto.toUser(),emailDto.subject(),emailDto.message());

        Map<String,String> response = new HashMap<>();
        response.put("Status","Success");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/sendMessageFile")
    public ResponseEntity<?> receiveRequestEmailWithFile(@ModelAttribute EmailFileDto emailfILEDto) {

        try{
            String fileName = emailfILEDto.file().getOriginalFilename();
            Path path = Paths.get("src/main/resource/files", fileName);
            Files.createDirectories(path.getParent());
            Files.copy(emailfILEDto.file().getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);

            File file = path.toFile();

            emailService.sendEmailWithFile(emailfILEDto.toUser(),emailfILEDto.subject(),emailfILEDto.message(),file);

            Map<String,String> response = new HashMap<>();
            response.put("Status","Success");
            response.put("File",emailfILEDto.file().getName());

            return ResponseEntity.ok(response);
        } catch (IOException e) {
            throw new RuntimeException("Error while sending file: "+ e.getMessage());
        }
    }
}
