package com.miketheshadow.complexmmostats.utils;

import net.kyori.adventure.text.Component;

import java.util.List;

public class CustomComponents {


    //TODO a lot to learn about creating epyc names here

    public static Component createDisplayName(String name) {

        return Component.text(name);
    }

    public static List<Component> createLore(List<Component> lore,List<String> loreData) {

        for(String item : loreData) {
            lore.add(Component.text(item));
        }

        return lore;
    }

}
