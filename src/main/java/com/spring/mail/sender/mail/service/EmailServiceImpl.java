package com.spring.mail.sender.mail.service;

import com.spring.mail.sender.mail.domain.EmailDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class EmailServiceImpl implements IEmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final JavaMailSender mailSender;

    @Value("${contact.mail.to}")
    private String contactMailTo;

    @Value("${spring.mail.host}")
    private String mailHost;

    @Value("${spring.mail.port}")
    private int mailPort;

    @Value("${spring.mail.username}")
    private String mailUsername;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
        logger.info("=== DEBUG: EmailServiceImpl initialized ===");
        logger.info("Mail Host: {}", mailHost);
        logger.info("Mail Port: {}", mailPort);
        logger.info("Mail Username: {}", mailUsername != null ? "SET (" + mailUsername.length() + " chars)" : "NULL");
    }

    @Override
    public void sendContactEmail(EmailDto emailDto) {
        logger.info("=== DEBUG: Starting sendContactEmail ===");
        logger.info("Contact Mail To: {}", contactMailTo != null ? "SET (" + contactMailTo.length() + " chars)" : "NULL");
        logger.info("Email from: {}", emailDto.email());
        logger.info("Email subject: {}", emailDto.subject());
        
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());

            String fromAddress = contactMailTo;
            logger.info("Setting from address: {}", fromAddress);
            
            helper.setFrom(fromAddress);
            helper.setTo(contactMailTo);
            helper.setReplyTo(emailDto.email());
            helper.setSubject("Portfolio: " + emailDto.subject());
            helper.setText(buildHtmlTemplate(emailDto), true);

            logger.info("About to send email...");
            mailSender.send(mimeMessage);
            logger.info("=== DEBUG: Email sent successfully! ===");
        } catch (MessagingException e) {
            logger.error("MessagingException ERROR: {}", e.getMessage(), e);
            throw new RuntimeException("Error sending contact email: " + e.getMessage());
        } catch (Exception e) {
            logger.error("General ERROR in sendContactEmail: {}", e.getMessage(), e);
            throw new RuntimeException("Error sending contact email: " + e.getMessage());
        }
    }

    private String buildHtmlTemplate(EmailDto emailDto) {
        return """
                <!DOCTYPE html>
                <html lang="es">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                </head>
                <body style="margin: 0; padding: 0; background-color: #0a0a0a; font-family: 'Inter', 'Segoe UI', Roboto, sans-serif;">
                    <table role="presentation" width="100%%" cellpadding="0" cellspacing="0" style="background-color: #0a0a0a; padding: 40px 20px;">
                        <tr>
                            <td align="center">
                                <table role="presentation" width="600" cellpadding="0" cellspacing="0" style="max-width: 600px; width: 100%%;">
                                    <!-- Gradient top bar -->
                                    <tr>
                                        <td style="height: 3px; background: linear-gradient(to right, #00f5ff, #8b5cf6); border-radius: 12px 12px 0 0;"></td>
                                    </tr>
                                    <!-- Main card -->
                                    <tr>
                                        <td style="background-color: #1a1a1a; border: 1px solid #27272a; border-top: none; border-radius: 0 0 12px 12px; padding: 40px 36px;">
                
                                            <!-- Title -->
                                            <h1 style="margin: 0 0 8px 0; font-size: 22px; font-weight: 700; color: #ffffff;">
                                                ✉️ Nuevo mensaje de contacto
                                            </h1>
                                            <p style="margin: 0 0 32px 0; font-size: 14px; color: #71717a;">
                                                Alguien te ha contactado desde tu portfolio
                                            </p>
                
                                            <!-- Name field -->
                                            <table role="presentation" width="100%%" cellpadding="0" cellspacing="0" style="margin-bottom: 20px;">
                                                <tr>
                                                    <td style="padding: 16px; background-color: #242424; border-radius: 8px;">
                                                        <p style="margin: 0 0 4px 0; font-size: 11px; font-weight: 600; text-transform: uppercase; letter-spacing: 1px; color: #00f5ff;">
                                                            Nombre
                                                        </p>
                                                        <p style="margin: 0; font-size: 15px; color: #e4e4e7;">
                                                            %s
                                                        </p>
                                                    </td>
                                                </tr>
                                            </table>
                
                                            <!-- Email field -->
                                            <table role="presentation" width="100%%" cellpadding="0" cellspacing="0" style="margin-bottom: 20px;">
                                                <tr>
                                                    <td style="padding: 16px; background-color: #242424; border-radius: 8px;">
                                                        <p style="margin: 0 0 4px 0; font-size: 11px; font-weight: 600; text-transform: uppercase; letter-spacing: 1px; color: #00f5ff;">
                                                            Email
                                                        </p>
                                                        <a href="mailto:%s" style="font-size: 15px; color: #8b5cf6; text-decoration: none;">
                                                            %s
                                                        </a>
                                                    </td>
                                                </tr>
                                            </table>
                
                                            <!-- Subject field -->
                                            <table role="presentation" width="100%%" cellpadding="0" cellspacing="0" style="margin-bottom: 20px;">
                                                <tr>
                                                    <td style="padding: 16px; background-color: #242424; border-radius: 8px;">
                                                        <p style="margin: 0 0 4px 0; font-size: 11px; font-weight: 600; text-transform: uppercase; letter-spacing: 1px; color: #00f5ff;">
                                                            Asunto
                                                        </p>
                                                        <p style="margin: 0; font-size: 15px; color: #e4e4e7;">
                                                            %s
                                                        </p>
                                                    </td>
                                                </tr>
                                            </table>
                
                                            <!-- Message field -->
                                            <table role="presentation" width="100%%" cellpadding="0" cellspacing="0" style="margin-bottom: 24px;">
                                                <tr>
                                                    <td style="padding: 20px; background-color: #242424; border-left: 3px solid #00f5ff; border-radius: 0 8px 8px 0;">
                                                        <p style="margin: 0 0 8px 0; font-size: 11px; font-weight: 600; text-transform: uppercase; letter-spacing: 1px; color: #00f5ff;">
                                                            Mensaje
                                                        </p>
                                                        <p style="margin: 0; font-size: 15px; line-height: 1.6; color: #e4e4e7; white-space: pre-wrap;">
                                                            %s
                                                        </p>
                                                    </td>
                                                </tr>
                                            </table>
                
                                            <!-- Divider -->
                                            <hr style="border: none; border-top: 1px solid #27272a; margin: 24px 0;">
                
                                            <!-- Footer -->
                                            <p style="margin: 0; font-size: 12px; color: #71717a; text-align: center;">
                                                Este mensaje fue enviado desde el formulario de contacto de tu portfolio.
                                            </p>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </body>
                </html>
                """.formatted(
                escapeHtml(emailDto.name()),
                escapeHtml(emailDto.email()),
                escapeHtml(emailDto.email()),
                escapeHtml(emailDto.subject()),
                escapeHtml(emailDto.message())
        );
    }

    private String escapeHtml(String input) {
        if (input == null) return "";
        return input
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
