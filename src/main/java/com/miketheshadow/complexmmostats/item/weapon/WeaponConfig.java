package com.miketheshadow.complexmmostats.item.weapon;

import com.miketheshadow.complexmmostats.ComplexMMOStats;
import com.miketheshadow.complexmmostats.utils.Stat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class WeaponConfig {

    public static FileConfiguration WEAPON_CONFIG;

    public static final String CONFIG_NAME = "WeaponConfig.yml";

    public static void load() {
        if(create()) return;
        WEAPON_CONFIG = YamlConfiguration.loadConfiguration(getFile());

    }

    private static boolean create() {
        File file = getFile();
        if(file.exists()) return false;

        try {
            file.createNewFile();
            WEAPON_CONFIG = YamlConfiguration.loadConfiguration(file);
            WEAPON_CONFIG.set("TestWeapon","");
            WEAPON_CONFIG.set("TestWeapon.Wield",1);
            WEAPON_CONFIG.set("TestWeapon.VisibleName","§6TestWeapon");
            WEAPON_CONFIG.set("TestWeapon.Type","Sword");
            WEAPON_CONFIG.set("TestWeapon.AttackDamage",410);
            WEAPON_CONFIG.set("TestWeapon.Durability",125);
            WEAPON_CONFIG.set("TestWeapon.Rarity",1);
            WEAPON_CONFIG.set("TestWeapon.Material",Material.DIAMOND_SWORD.toString());
            WEAPON_CONFIG.set("TestWeapon.Stat.STRENGTH",51);
            WEAPON_CONFIG.set("TestWeapon.Stat.STAMINA",34);
            WEAPON_CONFIG.set("TestWeapon.Stat.AttackSpeed",1);
            WEAPON_CONFIG.save(file);
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Weapon config created!");
        } catch (IOException e) { e.printStackTrace(); }

        return true;
    }

    private static File getFile() {
        return new File(ComplexMMOStats.INSTANCE.getDataFolder().getAbsolutePath() + "\\" + CONFIG_NAME);
    }

    public static ItemStack getItemFromConfig(String itemName,Player player) {

        //makes it easier to get sub items when the . is added first
        String id = itemName + ".";

        if(WEAPON_CONFIG.getString(itemName) == null) return null;


        HashMap<Stat,Integer> statMap = new HashMap<>();

        //preload Stats here
        for(Stat stat : Stat.values()) {
            int statAmount = WEAPON_CONFIG.getInt(id + "Stat." + stat.toString());
            if(statAmount != 0) {
                statMap.put(stat,statAmount);
            }
        }

        //create the weapon here
        //(String weaponName, String weaponType, int attackDamage, int durability, HashMap<Stat,Integer> stats, String rarity, String creatorName, Material material)
        return WeaponBuilder.createWeapon(WEAPON_CONFIG.getString(id + "VisibleName"),
                WEAPON_CONFIG.getString(id + "Type"),
                WEAPON_CONFIG.getInt(id + "Wield"),
                WEAPON_CONFIG.getInt(id + "AttackDamage"),
                WEAPON_CONFIG.getInt(id + "Durability"),
                statMap,
                WEAPON_CONFIG.getInt(id + "Rarity"),
                WEAPON_CONFIG.getInt(id + "AttackSpeed")
                ,Material.valueOf(WEAPON_CONFIG.getString(id + "Material"))
                ,player);
    }
}
