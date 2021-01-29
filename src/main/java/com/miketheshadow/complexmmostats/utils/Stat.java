package com.miketheshadow.complexmmostats.utils;

public enum Stat {

    STRENGTH,AGILITY,STAMINA,INTELLIGENCE;


    public static int convertStrengthToDamage(int strength) {
        return (int) (strength * .2);
    }

    public static int convertStaminaToHealth(int stamina) {
        return (int) stamina * 12;
    }
}
