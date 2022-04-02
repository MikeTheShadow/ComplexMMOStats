package com.miketheshadow.complexmmostats.utils;

import com.miketheshadow.complexmmostats.ComplexMMOStats;
import org.bukkit.NamespacedKey;

public class CMMOKeys {

    public static NamespacedKey CMMOITEM = load("CMMOITEM");
    public static NamespacedKey CREATOR_UUID = load("CREATOR_UUID");
    public static NamespacedKey UNIQUE_ITEM_ID = load("UNIQUE_ITEM_ID");

    private static NamespacedKey load(String keyName) {
        return new NamespacedKey(ComplexMMOStats.getInstance(), keyName);
    }
}
