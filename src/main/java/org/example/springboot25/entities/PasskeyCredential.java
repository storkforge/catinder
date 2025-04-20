package org.example.springboot25.entities;

public class PasskeyCredential {

    private String userName;
    private String credentialId;
    private String publicKey;
    private long signatureCount;

    // Getters
    public String getUserName() {
        return userName;
    }

    public String getCredentialId() {
        return credentialId;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public long getSignatureCount() {
        return signatureCount;
    }

    // Setters
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setCredentialId(String credentialId) {
        this.credentialId = credentialId;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public void setSignatureCount(long signatureCount) {
        this.signatureCount = signatureCount;
    }
}
