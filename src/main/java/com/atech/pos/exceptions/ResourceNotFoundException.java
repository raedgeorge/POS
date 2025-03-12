package com.atech.pos.exceptions;

public class ResourceNotFoundException extends Exception{

    public ResourceNotFoundException(String entity, String key, Object value) {
        super("%s not found for %s: %s".formatted(entity, key, value));
    }
}
