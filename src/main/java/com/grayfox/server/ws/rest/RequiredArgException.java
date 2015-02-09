package com.grayfox.server.ws.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RequiredArgException extends RuntimeException {

    private static final long serialVersionUID = -1341000608570103181L;

    private final String[] requiredArgs;
    
    private RequiredArgException(String[] requiredArgs) {
        super("arg.required.error=" + Arrays.toString(requiredArgs), null);
        this.requiredArgs = requiredArgs;
    }

    public String[] getRequiredArgs() {
        return requiredArgs;
    }

    public static class Builder {
        
        private List<String> requiredArgs;
        
        public Builder() {
            requiredArgs = new ArrayList<>();
        }

        public Builder addRequiredArg(String requiredArg) {
            requiredArgs.add(requiredArg);
            return this;
        }

        public void throwIfNotEmpty() throws RequiredArgException {
            if (!requiredArgs.isEmpty()) throw new RequiredArgException(requiredArgs.toArray(new String[requiredArgs.size()]));
            else { /* Do nothing */ }
        }
    }
}