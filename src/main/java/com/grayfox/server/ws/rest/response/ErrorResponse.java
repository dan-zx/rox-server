package com.grayfox.server.ws.rest.response;

import java.io.Serializable;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class ErrorResponse implements Serializable {

    private static final long serialVersionUID = -7554491972876922142L;

    private final String errorCode;
    private final String errorMessage;

    public ErrorResponse(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((errorCode == null) ? 0 : errorCode.hashCode());
        result = prime * result + ((errorMessage == null) ? 0 : errorMessage.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ErrorResponse other = (ErrorResponse) obj;
        if (errorCode == null) {
            if (other.errorCode != null) return false;
        } else if (!errorCode.equals(other.errorCode)) return false;
        if (errorMessage == null) {
            if (other.errorMessage != null) return false;
        } else if (!errorMessage.equals(other.errorMessage)) return false;
        return true;
    }

    public String toJson() {
        JsonObject response = new JsonObject();
        JsonObject errorElement = new JsonObject();
        errorElement.addProperty("errorCode", errorCode);
        errorElement.addProperty("errorMessage", errorMessage);
        response.add("error", errorElement);
        return new Gson().toJson(response);
    }

    @Override
    public String toString() {
        return "ErrorResponse [errorCode=" + errorCode + ", errorMessage=" + errorMessage + "]";
    }
}