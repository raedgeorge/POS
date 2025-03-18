package com.atech.pos.utils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class EntityUtils {

    public static String resolveSortByField(Class<?> entityClass, String sortBy, String defaultSortBy){

        List<String> fields = Arrays.stream(entityClass.getDeclaredFields())
                .map(Field::getName)
                .map(String::toLowerCase)
                .toList();

        return (sortBy != null && fields.contains(sortBy.toLowerCase())) ? sortBy : defaultSortBy;
    }
}
