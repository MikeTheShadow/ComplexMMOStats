package com.miketheshadow.complexmmostats.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ItemChecker {

    public static boolean isAnyWeapon(ItemStack stack) {
        if(!isValidComplexMMOItem(stack))   return false;
        //int handling = NBTItem.convertItemtoNBT(stack).getCompound("tag").getInteger("handling");
        int handling = getHandling(stack);
        return  handling == 2 || handling == 1;
    }

    public static boolean isTwoHandedWeapon(ItemStack stack) {
        if(stack == null || stack.getType() == Material.AIR || !isValidComplexMMOItem(stack)) return false;
        return getHandling(stack) == 2;
    }

    public static boolean isOneHandedWeapon(ItemStack stack) {
        if(stack == null || stack.getType() == Material.AIR || !isValidComplexMMOItem(stack)) return false;
        return getHandling(stack) == 1;
    }


    public static boolean isShield(ItemStack stack) {
        if(!isValidComplexMMOItem(stack)) return false;
        return getHandling(stack) == 3;
    }

    public static boolean isArmor(ItemStack stack) {

        return getHandling(stack) == 5;
    }

    private static int getHandling(ItemStack stack) {
        PersistentDataContainer container = stack.getItemMeta().getPersistentDataContainer();
        return container.get(NBTData.CMMOITEM, PersistentDataType.INTEGER);
    }

    public static boolean isValidComplexMMOItem(ItemStack stack) {
        if(stack == null || stack.getType() == Material.AIR || !stack.hasItemMeta()) return false;
        PersistentDataContainer container = stack.getItemMeta().getPersistentDataContainer();
        //if(container.getCompound("tag") != null) { return container.getCompound("tag").hasKey("CMMOITEM"); }
        return container.has(NBTData.CMMOITEM,PersistentDataType.INTEGER);
    }

    public static boolean isDifferentWeapon(ItemStack newWeapon, ItemStack oldWeapon) {
        if(isAnyWeapon(oldWeapon) && isAnyWeapon(newWeapon)) {
            return !newWeapon.getItemMeta().getPersistentDataContainer().get(NBTData.UNIQUE_ITEM_ID, PersistentDataType.STRING)
                    .equals(oldWeapon.getItemMeta().getPersistentDataContainer().get(NBTData.UNIQUE_ITEM_ID, PersistentDataType.STRING));
        }

        return true;
    }

}
