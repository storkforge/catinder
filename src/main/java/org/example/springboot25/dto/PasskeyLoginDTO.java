package org.example.springboot25.dto;

public class PasskeyLoginDTO {
    private String credentialId;
    private byte[] clientDataHash;
    private byte[] authenticatorData;
    private byte[] signature;

    public String getCredentialId() {
        return credentialId;
    }

    public void setCredentialId(String credentialId) {
        this.credentialId = credentialId;
    }

    public byte[] getClientDataHash() {
        return clientDataHash;
    }

    public void setClientDataHash(byte[] clientDataHash) {
        this.clientDataHash = clientDataHash;
    }

    public byte[] getAuthenticatorData() {
        return authenticatorData;
    }

    public void setAuthenticatorData(byte[] authenticatorData) {
        this.authenticatorData = authenticatorData;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }
}
