package com.example.finalyear.common.utils;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.regex.Pattern;

public class Validator {
    public static <T> Boolean isBlankOrNull(T item) {
        if (item == null) {
            return true;
        }
        if (item instanceof String && ((String) item).trim().length() <= 0) {
            return true;
        }
        if (item instanceof List && ((List<?>) item).isEmpty()) {
            return true;
        }
        return false;
    }

    @SafeVarargs
    public static <T> Boolean isBlankOrNull(T... items) {
        if (items == null) return true;
        for (T item : items) {
            if(isBlankOrNull(item)){
                return true;
            }
        }
        return false;
    }

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    @NonNull
    public static Boolean isValidEmail(String string) {
        return EMAIL_PATTERN.matcher(string).matches();
    }


    @NonNull
    public static Boolean isNumber(String s) {
        return s.matches("\\d+");
    }

    public static boolean isHyphen(String s) {
        return s.contains("-");
    }
}
