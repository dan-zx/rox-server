package com.grayfox.server.service.domain;

import java.io.Serializable;

import com.grayfox.server.domain.Credential;

public class CredentialResult implements Serializable {

    private static final long serialVersionUID = -370088658777393343L;

    private final Credential credential;
    private final boolean isNewUser;

    public CredentialResult(Credential credential, boolean isNewUser) {
        this.credential = credential;
        this.isNewUser = isNewUser;
    }

    public Credential getCredential() {
        return credential;
    }

    public boolean isNewUser() {
        return isNewUser;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((credential == null) ? 0 : credential.hashCode());
        result = prime * result + (isNewUser ? 1231 : 1237);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        CredentialResult other = (CredentialResult) obj;
        if (credential == null) {
            if (other.credential != null) return false;
        } else if (!credential.equals(other.credential)) return false;
        if (isNewUser != other.isNewUser) return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CredentialResult [credential=").append(credential).append(", isNewUser=").append(isNewUser).append("]");
        return builder.toString();
    }
}