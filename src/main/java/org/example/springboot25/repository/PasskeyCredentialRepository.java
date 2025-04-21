package org.example.springboot25.repository;

import org.example.springboot25.entities.PasskeyCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.example.springboot25.entities.User;

import java.util.List;
import java.util.Optional;

public interface PasskeyCredentialRepository extends JpaRepository<PasskeyCredential, Long> {
    Optional<PasskeyCredential> findByCredentialId(String credentialId);
    List<PasskeyCredential> findAllByUser(User user);
    boolean existsByCredentialId(String credentialId);
    void deleteByCredentialId(String credentialId);
}
