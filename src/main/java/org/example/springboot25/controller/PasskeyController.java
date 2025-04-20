package org.example.springboot25.controller;

import org.example.springboot25.service.PasskeyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/passkey")
public class PasskeyController {

    private final PasskeyService passkeyService;

    public PasskeyController(PasskeyService passkeyService) {
        this.passkeyService = passkeyService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerPasskey(@RequestBody String credentialJson) {
        passkeyService.saveCredential(credentialJson);
        return ResponseEntity.ok("Passkey registered!");
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<String> checkCredential(@PathVariable String id) {
        boolean exists = passkeyService.credentialExists(id);
        return ResponseEntity.ok(exists ? "Credential found" : "Not found");
    }
}
