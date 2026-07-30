package com.classes.Backend.Controller.test;

import com.classes.Backend.Service.mail.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test/mail")
public class MailTestController {

    private final MailService mailService;

    @GetMapping("/welcome")
    public ResponseEntity<?> sendTestWelcomeEmail(@RequestParam String to) {
        mailService.sendInstituteWelcomeEmail(to, "Test Institute", to);
        return ResponseEntity.ok(Map.of(
                "message", "Welcome email sent asynchronously to " + to,
                "note", "Check inbox/spam. This endpoint is temporary."
        ));
    }
}
