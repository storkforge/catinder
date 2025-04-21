package org.example.springboot25.service;

import jakarta.servlet.http.HttpSession;
import org.example.springboot25.entities.PasskeyCredential;
import org.example.springboot25.entities.User;
import org.example.springboot25.repository.PasskeyCredentialRepository;
import org.example.springboot25.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class PasskeyService {

    private final PasskeyCredentialRepository credentialRepo;
    private final UserRepository userRepo;
    private final HttpSession session;

    @Autowired
    public PasskeyService(PasskeyCredentialRepository credentialRepo,
                          UserRepository userRepo,
                          HttpSession session) {
        this.credentialRepo = credentialRepo;
        this.userRepo = userRepo;
        this.session = session;
    }

    public boolean credentialExists(String credentialId) {
        return credentialRepo.existsByCredentialId(credentialId);
    }

    public void saveCredential(String credentialJson, String userName) {
        // Placeholder – implement parsing and saving later
        System.out.println("Credential JSON received: " + credentialJson);
    }

    public String startRegistration(String userEmail) {
        Optional<User> optionalUser = userRepo.findByUserEmail(userEmail);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        byte[] challenge = new byte[32];
        new SecureRandom().nextBytes(challenge);
        session.setAttribute("register_challenge", challenge);

        return Base64.getUrlEncoder().withoutPadding().encodeToString(challenge);
    }

    public void finishRegistration(String credentialId, String publicKey, byte[] userHandle, String userEmail) {
        Optional<User> optionalUser = userRepo.findByUserEmail(userEmail);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        User user = optionalUser.get();

        PasskeyCredential credential = new PasskeyCredential();
        credential.setCredentialId(credentialId);
        credential.setPublicKey(publicKey);
        credential.setUserHandle(userHandle);
        credential.setUser(user);
        credential.setSignatureCount(0);
        credentialRepo.save(credential);
    }

    public String startLogin(String userEmail) {
        Optional<User> optionalUser = userRepo.findByUserEmail(userEmail);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = optionalUser.get();
        List<PasskeyCredential> credentials = credentialRepo.findAllByUser(user);
        if (credentials.isEmpty()) {
            throw new RuntimeException("No credentials found for user");
        }

        byte[] challenge = new byte[32];
        new SecureRandom().nextBytes(challenge);
        session.setAttribute("login_challenge", challenge);

        return Base64.getUrlEncoder().withoutPadding().encodeToString(challenge);
    }

    public void finishLogin(String credentialId, byte[] clientDataHash, byte[] authenticatorData, byte[] signature) {
        Optional<PasskeyCredential> optionalCredential = credentialRepo.findByCredentialId(credentialId);
        if (optionalCredential.isEmpty()) {
            throw new RuntimeException("Credential not found");
        }

        PasskeyCredential credential = optionalCredential.get();
        User user = credential.getUser();

        // Bygg authorities från användarrollen
        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + user.getUserRole().name())
        );

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(user.getUserName(), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
