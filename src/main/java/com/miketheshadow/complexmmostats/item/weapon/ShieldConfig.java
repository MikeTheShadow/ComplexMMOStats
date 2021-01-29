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

public class ShieldConfig {

    public static FileConfiguration SHIELD_CONFIG;

    public static final String CONFIG_NAME = "ShieldConfig.yml";

    public static void load() {
        if(create()) return;
        SHIELD_CONFIG = YamlConfiguration.loadConfiguration(getFile());

    }

    private static boolean create() {
        File file = getFile();
        if(file.exists()) return false;

        try {
            file.createNewFile();
            SHIELD_CONFIG = YamlConfiguration.loadConfiguration(file);
            SHIELD_CONFIG.set("PureDefenseShield","");
            SHIELD_CONFIG.set("PureDefenseShield.Wield",3);
            SHIELD_CONFIG.set("PureDefenseShield.VisibleName","§6PureDefenseShield");
            SHIELD_CONFIG.set("PureDefenseShield.Type","Shield");
            SHIELD_CONFIG.set("PureDefenseShield.Defense",3023);
            SHIELD_CONFIG.set("PureDefenseShield.Durability",125);
            SHIELD_CONFIG.set("PureDefenseShield.Rarity",1);
            SHIELD_CONFIG.set("PureDefenseShield.Material", Material.SHIELD.toString());
            SHIELD_CONFIG.set("PureDefenseShield.Stat.STAMINA",80);
            SHIELD_CONFIG.set("PureDefenseShield.Stat.AttackSpeed",1);
            SHIELD_CONFIG.save(file);
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Shield config created!");
        } catch (IOException e) { e.printStackTrace(); }

        return true;
    }

    private static File getFile() {
        return new File(ComplexMMOStats.INSTANCE.getDataFolder().getAbsolutePath() + "\\" + CONFIG_NAME);
    }

    public static ItemStack getItemFromConfig(String itemName, Player player) {

        //makes it easier to get sub items when the . is added first
        String id = itemName + ".";

        if(SHIELD_CONFIG.getString(itemName) == null) return null;

        HashMap<Stat,Integer> statMap = new HashMap<>();

        //preload Stats here
        for(Stat stat : Stat.values()) {
            int statAmount = SHIELD_CONFIG.getInt(id + "Stat." + stat.toString());
            if(statAmount != 0) {
                statMap.put(stat,statAmount);
            }
        }

        //create the shield here
        return ShieldBuilder.createShield(SHIELD_CONFIG.getString(id + "VisibleName"),
                SHIELD_CONFIG.getString(id + "Type"),
                SHIELD_CONFIG.getInt(id + "Wield"),
                SHIELD_CONFIG.getInt(id + "Defense"),
                SHIELD_CONFIG.getInt(id + "Durability"),
                statMap,
                SHIELD_CONFIG.getInt(id + "Rarity"),
                Material.valueOf(SHIELD_CONFIG.getString(id + "Material"))
                ,player);
    }

}
