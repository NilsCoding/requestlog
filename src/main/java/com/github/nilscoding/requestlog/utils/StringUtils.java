package com.github.nilscoding.requestlog.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Some String utility functions
 *
 * @author NilsCoding
 */
public class StringUtils {

    /**
     * System-dependent line separator
     */
    public static final String NEWLINE = System.getProperty("line.separator", "\n");
    /**
     * Charactes which should be escaped in XML content
     */
    public static final String[] XML_TO_ESCAPE = new String[]{"\"", "'", "<", ">", "&"};
    /**
     * A convenient date format pattern
     */
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    private StringUtils() {
    }

    /**
     * Checks if the given String contains any of the given other String values
     *
     * @param str string to check
     * @param checkFor string(s) to check for
     * @return true if it contains any of the given strings, false if not
     */
    public static boolean containsAny(String str, String... checkFor) {
        if (str == null) {
            return false;
        }
        if ((checkFor == null) || (checkFor.length == 0)) {
            return false;
        }
        for (String oneCheck : checkFor) {
            if (oneCheck == null) {
                continue;
            }
            if (str.contains(oneCheck)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Removes any occurances from the given string
     *
     * @param str string to remove other strings from
     * @param toRemove strings to remove
     * @return the changed string
     */
    public static String removeAny(String str, String... toRemove) {
        if (str == null) {
            return null;
        }
        if ((toRemove == null) || (toRemove.length == 0)) {
            return str;
        }
        for (String oneRemove : toRemove) {
            if ((oneRemove == null) || (oneRemove.length() == 0)) {
                continue;
            }
            str = str.replace(oneRemove, "");
        }
        return str;
    }

    /**
     * Formats the given date to a technically convenient pattern
     *
     * @param d Date to format
     * @return formatted date
     */
    public static String formatDate(Date d) {
        if (d == null) {
            d = new Date();
        }
        return DATE_FORMAT.format(d);
    }

    /**
     * Creates a unique ID string
     *
     * @return unique ID string
     */
    public static String createUniqueId() {
        return UUID.randomUUID().toString();
    }

}
