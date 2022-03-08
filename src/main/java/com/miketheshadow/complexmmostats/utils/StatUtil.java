package com.miketheshadow.complexmmostats.utils;

public class StatUtil {

    public static float convertStrengthToDamage(float strength) {
        return (float) (strength * .2);
    }

    public static float convertStaminaToHealth(float stamina) {
        return stamina * 12;
    }

}
