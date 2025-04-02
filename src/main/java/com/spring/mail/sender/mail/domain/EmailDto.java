package com.spring.mail.sender.mail.domain;

public record EmailDto(
        String[] toUser,
        String subject,
        String message
                      ) {
}
