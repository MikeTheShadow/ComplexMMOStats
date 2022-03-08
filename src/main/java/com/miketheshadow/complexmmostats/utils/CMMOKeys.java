package com.miketheshadow.complexmmostats.utils;

import com.miketheshadow.complexmmostats.ComplexMMOStats;
import org.bukkit.NamespacedKey;

public class CMMOKeys {

    public static NamespacedKey ATTACK_DAMAGE = register("attack_damage");
    public static NamespacedKey DEFENSE = register("defense");
    public static NamespacedKey DURABILITY = register("durability");
    public static NamespacedKey HANDLING = register("handling");
    public static NamespacedKey RARITY = register("rarity");
    public static NamespacedKey CMMOITEM = register("cmmoitem");
    public static NamespacedKey CREATOR_UUID = register("creator_uuid");
    public static NamespacedKey UNIQUE_ITEM_ID = register("unique_id");

    private static NamespacedKey register(String keyName) {
        return new NamespacedKey(ComplexMMOStats.INSTANCE, keyName);
    }

}
