package com.miketheshadow.complexmmostats.utils;

import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTContainer;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ItemBuilder {

    public static ItemStack getItemStack(int handling, int durability, HashMap<Stat, Integer> stats, int rarity, Player player, NBTContainer container, NBTCompound compound) {
        compound.setInteger("durability",durability);
        compound.setInteger("handling",handling);
        compound.setInteger("rarity",rarity);
        compound.setBoolean("CMMOITEM",true);
        if(player != null)compound.setString("creatorUUID",player.getUniqueId().toString());
        for(Map.Entry<Stat,Integer> stat : stats.entrySet()) {
            compound.setInteger(stat.getKey().name(),stat.getValue());
        }

        return NBTItem.convertNBTtoItem(container);
    }

}
