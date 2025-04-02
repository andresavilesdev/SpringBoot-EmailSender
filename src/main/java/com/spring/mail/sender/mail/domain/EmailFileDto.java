package com.spring.mail.sender.mail.domain;

import org.springframework.web.multipart.MultipartFile;

public record EmailFileDto(
        String[] toUser,
        String subject,
        String message,
        MultipartFile file
){}
