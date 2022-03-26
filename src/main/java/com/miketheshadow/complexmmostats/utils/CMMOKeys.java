package com.miketheshadow.complexmmostats.utils;

import com.miketheshadow.autoregister.annotations.InjectPlugin;
import com.miketheshadow.complexmmostats.ComplexMMOStats;
import org.bukkit.NamespacedKey;

public class CMMOKeys {

    @InjectPlugin
    private static ComplexMMOStats plugin;

    public static NamespacedKey CMMOITEM = register("CMMOITEM");
    public static NamespacedKey CREATOR_UUID = register("CREATOR_UUID");
    public static NamespacedKey UNIQUE_ITEM_ID = register("UNIQUE_ITEM_ID");

    private static NamespacedKey register(String keyName) {
        return new NamespacedKey(plugin, keyName);
    }

}
