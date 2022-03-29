package com.david.util;

// a class to read config file  (taken from your project)
public class ConfigReader {

    public static String extractPropertyValue(final String line) {
        int index = 0;
        char ch = line.charAt(index);
        while (ch != '=') {
            index++;
            ch = line.charAt(index);
        }
        index++;
        StringBuilder value = new StringBuilder();
        while (index < line.length()) {
            ch = line.charAt(index);
            value.append(ch);
            index++;
        }
        return value.toString();
    }
}
