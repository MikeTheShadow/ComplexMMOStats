package com.miketheshadow.complexmmostats.utils;

public class UnicodeConversion {

    public static char N_SPACE = '\uF801';

    public static String STRENGTH_ICON = createWithSpaces('\u2000', '\u2001');

    public static String STAMINA_ICON = createWithSpaces('\u2002', '\u2003');
    public static String AGILITY_ICON = createWithSpaces('\u2004', '\u2005');

    public static String ONE_HANDER_ICON = createWithSpaces('\u2006', '\u2007', '\u2008', '\u2009', '\u200a', '\u200b');

    public static String TWO_HANDER_ICON = createWithSpaces('\u200c', '\u200d', '\u200e', '\u2009', '\u200a', '\u200b');

    public static String ITEM_LEVEL_1 = charToString('\u2010');
    public static String ITEM_LEVEL_2 = charToString('\u2011');
    public static String ITEM_LEVEL_3 = charToString('\u2012');
    public static String ITEM_LEVEL_4 = charToString('\u2013');
    public static String ITEM_LEVEL_5 = charToString('\u2014');
    public static String ITEM_LEVEL_6 = charToString('\u2015');
    public static String ITEM_LEVEL_7 = charToString('\u2016');
    public static String ITEM_LEVEL_8 = charToString('\u2017');
    public static String ITEM_LEVEL_9 = charToString('\u2018');
    public static String ITEM_LEVEL_10 = charToString('\u2019');
    public static String ITEM_LEVEL_11 = charToString('\u201a');
    public static String ITEM_LEVEL_12 = charToString('\u201b');

    private static String charToString(char character) {

        return Character.toString(character);

    }

    private static String createWithSpaces(char... items) {

        StringBuilder builder = new StringBuilder();

        for (char item : items) {
            builder.append(item);
            builder.append(N_SPACE);
        }

        return builder.toString();
    }


/*

DELETE AS USED

// 200f Red square not yet used page 200
//201c
201d
201e
201f
2020
2021
2022
2023
2024
2025
2026
2027
2028
2029
202a
202b
202c
202d
202e
202f
2030
2031
     */
}
