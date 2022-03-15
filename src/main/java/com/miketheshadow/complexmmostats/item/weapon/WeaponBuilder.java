package com.miketheshadow.complexmmostats.item.weapon;

import com.miketheshadow.complexmmostats.utils.CMMOKeys;
import com.miketheshadow.complexmmostats.utils.ItemBuilder;
import com.miketheshadow.mmotextapi.text.ItemStat;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WeaponBuilder extends ItemBuilder {

//    public static ItemStack createWeapon(String weaponName, String weaponType, int handling, int attackDamage, int durability, HashMap<ItemStat, Integer> stats, int rarity, int attackSpeed, Material material, Player player) {
//
//        List<String> lore = new ArrayList<>();
//
//        String creatorName = null;
//        //TODO enable if paper is added
//        //if(player != null) creatorName = player.displayName().examinableName();
//        if (player != null) creatorName = player.getDisplayName();
//        lore.add(String.format("§6%s", weaponType));
//        lore.add(String.format("§b%s", rarity));
//        lore.add("");
//        lore.add("§e§l§m------==§e§l[ §6Stats §e§l]§e§l§m==------");
//        lore.add("");
//        lore.add(String.format("§6Attack Damage§7: §f%s", attackDamage));
//        lore.add(String.format("§6Attack Speed§7: §f%s", attackSpeed));
//        lore.add(String.format("§6Durability§7: §f%s", durability));
//        lore.add("");
//        lore.add("§e§l§m----==§e§l[ §2Attributes §e§l]§e§l§m==----");
//        lore.add("");
//        stats.forEach((key, value) -> lore.add(String.format("§2" + key + "§7: §f%s", value)));
//        if (creatorName != null) {
//            lore.add("");
//            lore.add(String.format("§7Created by §b%s", creatorName));
//        }
//
//        ItemStack stack = new ItemStack(material, 1);
//        ItemMeta meta = stack.getItemMeta();
//        meta.setDisplayName(weaponName);
//        meta.setLore(lore);
//        stack.setItemMeta(meta);
//        //TODO enable if paper is added
//        //meta.displayName(CustomComponents.createDisplayName(weaponName));
//        //meta.lore(CustomComponents.createLore(meta.lore(),lore));
//        return applyTags(stack, handling, attackDamage, durability, stats, rarity, player);
//    }
//
//
//    public static ItemStack applyTags(ItemStack item, int handling, int attackDamage, int durability, HashMap<ItemStat, Integer> stats, int rarity, Player player) {
//
//
//        ItemMeta meta = item.getItemMeta();
//        PersistentDataContainer container = meta.getPersistentDataContainer();
//        modifyContainer(handling, durability, stats, rarity, player, container);
//        container.set(CMMOKeys.ATTACK_DAMAGE, PersistentDataType.INTEGER, attackDamage);
//
//        //NBTContainer container = NBTItem.convertItemtoNBT(item);
//        //NBTCompound compound = container.getCompound("tag");
//        //compound.setInteger("attack_damage",attackDamage);
//        item.setItemMeta(meta);
//        return item;
//    }


}
