package com.miketheshadow.complexmmostats.utils;

import com.miketheshadow.complexmmostats.ComplexMMOStats;
import org.bukkit.NamespacedKey;

public class NBTData {

    public static NamespacedKey ATTACK_DAMAGE = register("attack_damage");
    public static NamespacedKey DEFENSE = register("defense");
    public static NamespacedKey DURABILITY = register("durability");
    public static NamespacedKey HANDLING = register("handling");
    public static NamespacedKey RARITY = register("rarity");
    public static NamespacedKey CMMOITEM = register("cmmoitem");
    public static NamespacedKey CREATOR_UUID = register("creator_uuid");
    public static NamespacedKey STRENGTH = register("strength");
    public static NamespacedKey AGILITY = register("agility");
    public static NamespacedKey STAMINA = register("stamina");
    public static NamespacedKey INTELLIGENCE = register("intelligence");
    public static NamespacedKey UNIQUE_ITEM_ID = register("unique_id");

    private static NamespacedKey register(String keyName) {
        return new NamespacedKey(ComplexMMOStats.INSTANCE, keyName);
    }

}
