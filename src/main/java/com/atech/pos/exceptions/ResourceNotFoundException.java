package com.atech.pos.exceptions;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String entity, String key, Object value) {
        super("%s not found for %s: %s".formatted(entity, key, value));
    }
}
