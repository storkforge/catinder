package org.example.springboot25.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.springboot25.entities.PasskeyCredential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PasskeyService {

    private static final Logger logger = LoggerFactory.getLogger(PasskeyService.class);

    // Enkel in-memory storage – byt till databas om ni vill
    private final ConcurrentHashMap<String, PasskeyCredential> credentialStore = new ConcurrentHashMap<>();

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void saveCredential(String rawCredentialJson, String userName) {
        try {
            JsonNode json = objectMapper.readTree(rawCredentialJson);

            if (!json.has("id") || !json.has("rawId") || !json.has("response")) {
                logger.error("Missing required fields in credential JSON");
                throw new IllegalArgumentException("Credential JSON missing required fields");
            }

            String id = json.get("id").asText();
            String rawId = json.get("rawId").asText();
            String publicKey = json.get("response").toString();

            if (id.isEmpty() || publicKey.isEmpty()) {
                logger.error("Empty values for required fields: id={}, publicKey={}", id, publicKey);
                throw new IllegalArgumentException("Credential contains empty required values");
            }

            PasskeyCredential credential = new PasskeyCredential();
            credential.setUserName(userName);
            credential.setCredentialId(id);
            credential.setPublicKey(publicKey);
            credential.setSignatureCount(0);

            credentialStore.put(id, credential);

            logger.info("Passkey credential saved: ID={}, PublicKey={}", id, publicKey);

        } catch (Exception e) {
            logger.error("Failed to parse and save passkey credential: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to process credential", e);
        }
    }

    public PasskeyCredential getCredentialById(String id) {
        return credentialStore.get(id);
    }

    public boolean credentialExists(String id) {
        return credentialStore.containsKey(id);
    }
}
