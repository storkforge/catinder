package org.example.springboot25.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.springboot25.service.PasskeyService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/passkey")
@PreAuthorize("isAuthenticated()")  // Or appropriate security expression
public class PasskeyController {

    private final PasskeyService passkeyService;

    public PasskeyController(PasskeyService passkeyService) {
        this.passkeyService = passkeyService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerPasskey(@RequestBody String credentialJson) {
        try {
            if (credentialJson == null || credentialJson.isBlank()) {
                return ResponseEntity.badRequest().body("Credential data cannot be empty");
            }

            String credentialId = passkeyService.saveCredential(credentialJson);
            return ResponseEntity.ok("Passkey registered with ID: " + credentialId);
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body("Invalid credential format: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to register passkey: " + e.getMessage());
        }
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<String> checkCredential(@PathVariable String id) {
        boolean exists = passkeyService.credentialExists(id);
        return ResponseEntity.ok(exists ? "Credential found" : "Not found");
    }
}
