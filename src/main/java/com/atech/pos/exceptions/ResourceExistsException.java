package com.atech.pos.exceptions;

public class ResourceExistsException extends Exception{

    public ResourceExistsException(String entity, String key, Object value){
        super("%s exists for %s: %s".formatted(entity, key, value));
    }
}
