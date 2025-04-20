package org.example.springboot25.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.springboot25.service.PasskeyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/passkey")
@PreAuthorize("isAuthenticated()")
public class PasskeyController {

    private final PasskeyService passkeyService;

    public PasskeyController(PasskeyService passkeyService) {
        this.passkeyService = passkeyService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerPasskey(
            @RequestBody String credentialJson,
            Authentication authentication
    ) {
        if (credentialJson == null || credentialJson.isBlank()) {
            throw new BadRequestException("Credential data cannot be empty");
        }

        String userName = authentication.getName();
        passkeyService.saveCredential(credentialJson, userName);

        return ResponseEntity.ok("Passkey registered for user: " + userName);
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<String> checkCredential(@PathVariable String id) {
        boolean exists = passkeyService.credentialExists(id);
        return ResponseEntity.ok(exists ? "Credential found" : "Not found");
    }
}
