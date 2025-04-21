package org.example.springboot25.controller;

import jakarta.servlet.http.HttpSession;
import org.example.springboot25.entities.PasskeyCredential;
import org.example.springboot25.entities.User;
import org.example.springboot25.exceptions.BadRequestException;
import org.example.springboot25.repository.PasskeyCredentialRepository;
import org.example.springboot25.repository.UserRepository;
import org.example.springboot25.service.PasskeyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.*;

@RestController
@RequestMapping("/passkey")
public class PasskeyController {

    private final PasskeyService passkeyService;
    private final UserRepository userRepo;
    private final PasskeyCredentialRepository credentialRepo;
    private final HttpSession session;

    public PasskeyController(PasskeyService passkeyService,
                             UserRepository userRepo,
                             PasskeyCredentialRepository credentialRepo,
                             HttpSession session) {
        this.passkeyService = passkeyService;
        this.userRepo = userRepo;
        this.credentialRepo = credentialRepo;
        this.session = session;
    }

    @PostMapping("/register")
    @PreAuthorize("isAuthenticated()")
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
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> checkCredential(@PathVariable String id) {
        boolean exists = passkeyService.credentialExists(id);
        return ResponseEntity.ok(exists ? "Credential found" : "Not found");
    }

    // --- Eva testar passkey login flow ---

    @GetMapping("/webauthn/login/start")
    @PreAuthorize("permitAll()") // 👈 Viktigt: alla ska kunna initiera login
    public ResponseEntity<Map<String, Object>> startPasskeyLogin(@RequestParam String userEmail) {
        Optional<User> userOpt = userRepo.findByUserEmail(userEmail);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found"));
        }

        User user = userOpt.get();
        List<PasskeyCredential> creds = credentialRepo.findAllByUser(user);
        if (creds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "No credentials found"));
        }

        byte[] challenge = new byte[32];
        new SecureRandom().nextBytes(challenge);
        session.setAttribute("login_challenge", challenge);

        List<Map<String, Object>> allowCredentials = creds.stream()
                .map(cred -> Map.of(
                        "type", (Object) "public-key",
                        "id", (Object) Base64.getUrlEncoder().withoutPadding().encodeToString(cred.getCredentialId().getBytes())
                ))
                .toList();

        Map<String, Object> publicKey = Map.of(
                "challenge", Base64.getUrlEncoder().withoutPadding().encodeToString(challenge),
                "rpId", "localhost", // eller din domän i deployment
                "allowCredentials", allowCredentials,
                "userVerification", "preferred"
        );

        return ResponseEntity.ok(Map.of("publicKey", publicKey));
    }

    @PostMapping("/webauthn/login/finish")
    @PreAuthorize("permitAll()")
    public ResponseEntity<String> finishPasskeyLogin(@RequestBody Map<String, Object> credentialJson) {
        try {
            String credentialId = (String) credentialJson.get("id");
            Map<String, Object> response = (Map<String, Object>) credentialJson.get("response");

            byte[] clientDataJSON = Base64.getUrlDecoder().decode((String) response.get("clientDataJSON"));
            byte[] authenticatorData = Base64.getUrlDecoder().decode((String) response.get("authenticatorData"));
            byte[] signature = Base64.getUrlDecoder().decode((String) response.get("signature"));

            // Enligt WebAuthn ska clientDataHash = SHA256(clientDataJSON)
            byte[] clientDataHash = java.security.MessageDigest.getInstance("SHA-256").digest(clientDataJSON);

            passkeyService.finishLogin(credentialId, clientDataHash, authenticatorData, signature);
            return ResponseEntity.ok("Login successful");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed: " + e.getMessage());
        }
    }

    // --- Slut på Eva testar passkey login flow ---
}
