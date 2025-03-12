package com.atech.pos.exceptions;

public class ResourceExistsException extends RuntimeException{

    public ResourceExistsException(String entity, String key, Object value){
        super("%s exists for %s: %s".formatted(entity, key, value));
    }
}
