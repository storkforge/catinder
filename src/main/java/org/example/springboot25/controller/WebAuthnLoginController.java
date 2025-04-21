package org.example.springboot25.controller;

import org.example.springboot25.dto.PasskeyLoginDTO;
import org.example.springboot25.service.PasskeyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/webauthn/login")
public class WebAuthnLoginController {

    private final PasskeyService passkeyService;

    public WebAuthnLoginController(PasskeyService passkeyService) {
        this.passkeyService = passkeyService;
    }

    @PostMapping("/start")
    public ResponseEntity<?> startLogin(@RequestBody String email) {
        String challenge = passkeyService.startLogin(email);

        return ResponseEntity.ok(
                Map.of("publicKeyCredentialRequestOptions", Map.of(
                        "challenge", challenge,
                        "rpId", "localhost",
                        "userVerification", "preferred"
                ))
        );
    }

    @PostMapping("/finish")
    public ResponseEntity<?> finishLogin(@RequestBody PasskeyLoginDTO loginDTO) {
        passkeyService.finishLogin(
                loginDTO.getCredentialId(),
                loginDTO.getClientDataHash(),
                loginDTO.getAuthenticatorData(),
                loginDTO.getSignature()
        );
        return ResponseEntity.ok("Login successful");
    }
}
