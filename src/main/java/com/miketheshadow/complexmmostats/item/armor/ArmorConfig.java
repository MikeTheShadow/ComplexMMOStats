package com.miketheshadow.complexmmostats.item.armor;

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

public class ArmorConfig {

    public static FileConfiguration ARMOR_CONFIG;

    public static final String CONFIG_NAME = "ArmorConfig.yml";

    public static void load() {
        if(create()) return;
        ARMOR_CONFIG = YamlConfiguration.loadConfiguration(getFile());

    }

    private static boolean create() {
        File file = getFile();
        if(file.exists()) return false;

        try {
            file.createNewFile();
            ARMOR_CONFIG = YamlConfiguration.loadConfiguration(file);
            ARMOR_CONFIG.set("PureDefenseArmor","");
            ARMOR_CONFIG.set("PureDefenseArmor.Wield",5);
            ARMOR_CONFIG.set("PureDefenseArmor.VisibleName","§6PureDefense");
            ARMOR_CONFIG.set("PureDefenseArmor.Defense",4091);
            ARMOR_CONFIG.set("PureDefenseArmor.Durability",125);
            ARMOR_CONFIG.set("PureDefenseArmor.Rarity",1);
            ARMOR_CONFIG.set("PureDefenseArmor.Material", "DIAMOND");
            ARMOR_CONFIG.set("PureDefenseArmor.Stat.STAMINA",504);
            ARMOR_CONFIG.save(file);
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Shield config created!");
        } catch (IOException e) { e.printStackTrace(); }

        return true;
    }

    private static File getFile() {
        return new File(ComplexMMOStats.INSTANCE.getDataFolder().getAbsolutePath() + "\\" + CONFIG_NAME);
    }

    public static ItemStack getItemFromConfig(String itemName,String partName, Player player) {

        //makes it easier to get sub items when the . is added first

        String baseItemName = itemName.replace("_" + partName,"");

        Bukkit.getServer().getConsoleSender().sendMessage(baseItemName);

        String id = baseItemName + ".";

        double partStatAmount = getPartStatAmount(partName);

        if(ARMOR_CONFIG.getString(baseItemName) == null) return null;

        HashMap<Stat,Integer> statMap = new HashMap<>();

        //preload Stats here
        for(Stat stat : Stat.values()) {
            int statAmount = (int) Math.round(ARMOR_CONFIG.getInt(id + "Stat." + stat.toString()) * partStatAmount);
            if(statAmount != 0) {
                statMap.put(stat,statAmount);
            }
        }

        //precalculate defense
        int defense = (int) Math.round(ARMOR_CONFIG.getInt(id + "Defense") * partStatAmount);

        //create the armor here
        return ArmorBuilder.createArmor(ARMOR_CONFIG.getString(id + "VisibleName"),
                ARMOR_CONFIG.getInt(id + "Wield"),
                defense,
                ARMOR_CONFIG.getInt(id + "Durability"),
                statMap,
                ARMOR_CONFIG.getInt(id + "Rarity"),
                Material.valueOf(ARMOR_CONFIG.getString(id + "Material") + "_" + partName),
                partName
                ,player);
    }

    private static double getPartStatAmount(String name) {

        if(name.equals("HELMET")) return 0.214;
        if(name.equals("CHESTPLATE")) return 0.357;
        if(name.equals("LEGGINGS")) return 0.286;
        if(name.equals("BOOTS")) return 0.143;
        throw new RuntimeException("Critical error! Unable to find armor type of " + name + "!");
    }

}
