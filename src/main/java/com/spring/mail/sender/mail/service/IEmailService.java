package com.spring.mail.sender.mail.service;

import com.spring.mail.sender.mail.domain.EmailDto;

public interface IEmailService {
    void sendContactEmail(EmailDto emailDto);
}
