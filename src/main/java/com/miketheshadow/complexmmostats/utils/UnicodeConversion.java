package com.miketheshadow.complexmmostats.utils;

public class UnicodeConversion {

    public static char N_SPACE = '\uF801';
    public static char N_SPACE_2 = '\uF802';


    public static final String STRENGTH_ICON = createWithSpaces('\u2000', '\u2001');

    public static final String STAMINA_ICON = createWithSpaces('\u2002', '\u2003');
    public static final String AGILITY_ICON = createWithSpaces('\u2004', '\u2005');

    public static final String ONE_HANDER_ICON = createWithSpaces('\u2006', '\u2007', '\u2008', '\u2009', '\u200a', '\u200b');

    public static final String TWO_HANDER_ICON = createWithSpaces('\u200c', '\u200d', '\u200e', '\u2009', '\u200a', '\u200b');

    public static final String ITEM_LEVEL_1 = charToString('\u2010');
    public static final String ITEM_LEVEL_2 = charToString('\u2011');
    public static final String ITEM_LEVEL_3 = charToString('\u2012');
    public static final String ITEM_LEVEL_4 = charToString('\u2013');
    public static final String ITEM_LEVEL_5 = charToString('\u2014');
    public static final String ITEM_LEVEL_6 = charToString('\u2015');
    public static final String ITEM_LEVEL_7 = charToString('\u2016');
    public static final String ITEM_LEVEL_8 = charToString('\u2017');
    public static final String ITEM_LEVEL_9 = charToString('\u2018');
    public static final String ITEM_LEVEL_10 = charToString('\u2019');
    public static final String ITEM_LEVEL_11 = charToString('\u201a');
    public static final String ITEM_LEVEL_12 = charToString('\u201b');

    public static final char NUM_0 = '\u200f'; //NOTE this was the red square in the top corner
    public static final char NUM_1 = '\u201c';
    public static final char NUM_2 = '\u201d';
    public static final char NUM_3 = '\u201e';
    public static final char NUM_4 = '\u201f';
    public static final char NUM_5 = '\u2020';
    public static final char NUM_6 = '\u2021';
    public static final char NUM_7 = '\u2022';
    public static final char NUM_8 = '\u2023';
    public static final char NUM_9 = '\u2024';

    public static String numToUnicode(long num) {

        String numString = String.valueOf(num);

        StringBuilder unicodeString = new StringBuilder();

        for(char c : numString.toCharArray()) {
            switch (c) {
                case '1':
                    unicodeString.append(NUM_1);
                    break;
                case '2':
                    unicodeString.append(NUM_2);
                    break;
                case '3':
                    unicodeString.append(NUM_3);
                    break;
                case '4':
                    unicodeString.append(NUM_4);
                    break;
                case '5':
                    unicodeString.append(NUM_5);
                    break;
                case '6':
                    unicodeString.append(NUM_6);
                    break;
                case '7':
                    unicodeString.append(NUM_7);
                    break;
                case '8':
                    unicodeString.append(NUM_8);
                    break;
                case '9':
                    unicodeString.append(NUM_9);
                    break;
                case '0':
                    unicodeString.append(NUM_0);
                    break;
            }
            //unicodeString.append(N_SPACE_2);
        }

        return unicodeString.toString();
    }

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
