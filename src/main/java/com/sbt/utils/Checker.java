package com.sbt.utils;

import com.sbt.exceptions.IdFormatException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Checker {
    private static final Pattern VK_ID = Pattern.compile("^[0-9]+$");

    public static void checkVKID(String id) throws IdFormatException {
        Matcher m = VK_ID.matcher(id);
        if (!m.matches()) throw new IdFormatException();
    }
}
