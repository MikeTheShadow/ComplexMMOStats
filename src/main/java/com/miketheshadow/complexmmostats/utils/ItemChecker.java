package com.miketheshadow.complexmmostats.utils;

import de.tr7zw.nbtapi.NBTContainer;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemChecker {

    public static boolean isAnyWeapon(ItemStack stack) {
        if(stack == null || stack.getType() == Material.AIR || !isValidComplexMMOItem(stack))   return false;
        int handling = NBTItem.convertItemtoNBT(stack).getCompound("tag").getInteger("handling");
        return  handling == 2 || handling == 1;
    }

    public static boolean isTwoHandedWeapon(ItemStack stack) {
        if(stack == null || stack.getType() == Material.AIR || !isValidComplexMMOItem(stack)) return false;
        Bukkit.getServer().broadcastMessage("handling: " + NBTItem.convertItemtoNBT(stack).getCompound("tag").getInteger("handling"));
        return NBTItem.convertItemtoNBT(stack).getCompound("tag").getInteger("handling") == 2;
    }

    public static boolean isShield(ItemStack stack) {
        return NBTItem.convertItemtoNBT(stack).getCompound("tag").getInteger("handling") == 3;
    }

    public static boolean isArmor(ItemStack stack) {

        return NBTItem.convertItemtoNBT(stack).getCompound("tag").getInteger("handling") == 5;
    }

    public static boolean isValidComplexMMOItem(ItemStack stack) {

        NBTContainer container = NBTItem.convertItemtoNBT(stack);
        if(container.getCompound("tag") != null) {
            return container.getCompound("tag").hasKey("CMMOITEM");
        }
        return false;
    }

}
