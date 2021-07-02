package com.miketheshadow.complexmmostats.utils;

public class UnicodeConversion {

    public static char N_SPACE = '\uF801';

    public static String STRENGTH_ICON = createWithSpaces('\u2000', '\u2001');

    public static String STAMINA_ICON = createWithSpaces('\u2002', '\u2003');
    public static String AGILITY_ICON = createWithSpaces('\u2004', '\u2005');

    public static String ONE_HANDER_ICON = createWithSpaces('\u2006', '\u2007', '\u2008', '\u2009', '\u200a', '\u200b');

    public static String TWO_HANDER_ICON = createWithSpaces('\u200c', '\u200d', '\u200e', '\u2009', '\u200a', '\u200b');

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

200f
2010
2011
2012
2013
2014
2015
2016
2017
2018
2019
201a
201b
201c
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
