package org.example.springboot25.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * Entity representing a WebAuthn passkey credential stored in the system.
 * This entity maps to a user's registered authenticator and contains
 * the necessary information to verify future authentications.
 */
@Entity
@Table(name = "passkey_credentials")
@EntityListeners(AuditingEntityListener.class)
public class PasskeyCredential implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The username associated with this credential.
     */
    @NotNull
    @Column(nullable = false)
    private String userName;

    /**
     * The unique identifier for this credential, serves as the primary key.
     * This is the base64url-encoded credential ID from the authenticator.
     */
    @Id
    @NotNull
    private String credentialId;

    /**
     * The public key used to verify signatures from the authenticator.
     * Stored as TEXT to accommodate potentially large key data.
     */
    @NotNull
    @Column(columnDefinition = "TEXT", nullable = false)
    private String publicKey;

    /**
     * A counter maintained by the authenticator that increases with each use.
     * Used to detect cloned authenticators and prevent replay attacks.
     */
    private long signatureCount;

    @CreatedDate
    @Column(updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    // Default constructor required by JPA
    public PasskeyCredential() {
    }

    // Getters and Setters
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCredentialId() {
        return credentialId;
    }

    public void setCredentialId(String credentialId) {
        this.credentialId = credentialId;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public long getSignatureCount() {
        return signatureCount;
    }

    public void setSignatureCount(long signatureCount) {
        this.signatureCount = signatureCount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PasskeyCredential that)) return false;
        return Objects.equals(credentialId, that.credentialId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(credentialId);
    }

    @Override
    public String toString() {
        return "PasskeyCredential{" +
                "userName='" + userName + '\'' +
                ", credentialId='" + credentialId + '\'' +
                ", signatureCount=" + signatureCount +
                '}';
    }
}
