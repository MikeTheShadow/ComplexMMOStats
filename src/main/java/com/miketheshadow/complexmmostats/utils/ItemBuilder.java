package com.miketheshadow.complexmmostats.utils;

import com.miketheshadow.autoregister.annotations.InjectPlugin;
import com.miketheshadow.complexmmostats.ComplexMMOStats;
import com.miketheshadow.mmotextapi.itembuilder.ToolItemBuilder;
import com.miketheshadow.mmotextapi.text.Grade;
import com.miketheshadow.mmotextapi.text.ItemStat;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

public class ItemBuilder {

    @InjectPlugin
    private static ComplexMMOStats plugin;

    public static ItemStack createItemFromStats(String itemName, Material material, String weaponType, int handling, int defense, int durability,
                                         HashMap<ItemStat, Integer> baseStats,int rarity, Player player) {

        HashMap<ItemStat,Integer> stats = new HashMap<>();

        stats.put(ItemStat.HANDLING,handling);
        stats.put(ItemStat.DEFENSE,defense);
        stats.put(ItemStat.DURABILITY,durability);

        ToolItemBuilder toolItemBuilder = new ToolItemBuilder(plugin,material,itemName,player,stats,baseStats);
        toolItemBuilder.setItemType(weaponType);
        toolItemBuilder.setGrade(Grade.values()[rarity]);
        return toolItemBuilder.build();
    }

    public static ItemStack createWeaponFromStats(String itemName, Material material, String weaponType, int handling, int attackDamage, int durability,
                                                HashMap<ItemStat, Integer> baseStats,int rarity,int attackSpeed, Player player) {

        HashMap<ItemStat,Integer> stats = new HashMap<>();

        stats.put(ItemStat.HANDLING,handling);
        stats.put(ItemStat.ATTACK_DAMAGE,attackDamage);
        stats.put(ItemStat.DURABILITY,durability);
        stats.put(ItemStat.ATTACK_SPEED,attackSpeed);

        ToolItemBuilder toolItemBuilder = new ToolItemBuilder(plugin,material,itemName,player,stats,baseStats);
        toolItemBuilder.setItemType(weaponType);
        toolItemBuilder.setGrade(Grade.values()[rarity]);
        return toolItemBuilder.build();
    }

}
