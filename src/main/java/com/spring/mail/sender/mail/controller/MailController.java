package com.spring.mail.sender.mail.controller;

import com.spring.mail.sender.mail.domain.EmailDto;
import com.spring.mail.sender.mail.service.IEmailService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1")
@CrossOrigin(origins = "*")
public class MailController {

    private static final Logger logger = LoggerFactory.getLogger(MailController.class);

    private final IEmailService emailService;

    public MailController(IEmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/contact")
    public ResponseEntity<Map<String, String>> sendContactEmail(@Valid @RequestBody EmailDto emailDto) {
        logger.info("=== DEBUG: Petition received at /contact endpoint ===");
        logger.info("Received data: name={}, email={}, subject={}, message={}", 
            emailDto.name(), emailDto.email(), emailDto.subject(), 
            emailDto.message() != null ? emailDto.message().substring(0, Math.min(emailDto.message().length(), 50)) + "..." : "null");
        
        try {
            emailService.sendContactEmail(emailDto);
            logger.info("Email sent successfully!");
            return ResponseEntity.ok(Map.of("status", "sent"));
        } catch (Exception e) {
            logger.error("ERROR sending email: {}", e.getMessage(), e);
            throw e;
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(Map.of(
                "status", "error",
                "errors", errors
        ));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntime(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "status", "error",
                "message", "Error al enviar el correo"
        ));
    }
}
