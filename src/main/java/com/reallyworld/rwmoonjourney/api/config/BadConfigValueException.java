package com.reallyworld.rwmoonjourney.api.config;

public class BadConfigValueException extends Exception {
    private final String key;

    public BadConfigValueException(String key) {
        super("The '" + key + "' key into config file not specified");
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}