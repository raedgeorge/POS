package com.atech.pos.utils;

public class StringUtils {

    public static String convertEachWorldToFirstLetterUpperCase(String text){

        StringBuilder stringBuilder = new StringBuilder();

        String[] wordsArray = text.trim().split(" ");

        int index = 0;

        for (String str : wordsArray){
            stringBuilder.append(convertWorldToFirstLetterUpperCase(str));

            if (index != wordsArray.length - 1){
                stringBuilder.append(" ");
            }
            index++;
        }

        return stringBuilder.toString();
    }

    public static String convertWorldToFirstLetterUpperCase(String text){

        StringBuilder stringBuilder = new StringBuilder();

        String trimmedString = text.trim();

        char firstChar = trimmedString.toUpperCase().charAt(0);

        char[] chars = trimmedString.toLowerCase().toCharArray();

        int index = 0;

        for (char ch : chars){
            if (index == 0){
                stringBuilder.append(firstChar);
                index++;
            } else {
                stringBuilder.append(ch);
            }
        }

        return stringBuilder.toString();
    }
}
