package com.miketheshadow.complexmmostats;

import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.miketheshadow.complexmmostats.item.armor.ArmorConfig;
import com.miketheshadow.complexmmostats.item.weapon.ShieldConfig;
import com.miketheshadow.complexmmostats.item.weapon.WeaponConfig;
import com.miketheshadow.complexmmostats.utils.LoaderTool;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class ComplexMMOStats extends JavaPlugin {

    public static ComplexMMOStats INSTANCE;

    @Override
    public void onEnable() {
        if (!Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
            getLogger().severe("*** HolographicDisplays is not installed or not enabled. ***");
        } else {
            try {
                HologramsAPI.getHolograms(this).clear();
                getLogger().log(Level.FINEST, "Holograms enabled!");
            } catch (Exception ignored) {
            }
        }
        // Plugin startup logic
        INSTANCE = this;
        if (!this.getDataFolder().exists()) {

            if (!this.getDataFolder().mkdir()) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error! Can't create config folder! Disabling...");
                Bukkit.getPluginManager().disablePlugin(this);
                return;
            }
        }
        WeaponConfig.load();
        ShieldConfig.load();
        ArmorConfig.load();

        LoaderTool loaderTool = new LoaderTool(this, "com.miketheshadow.complexmmostats");
        try {
            loaderTool.defaultSetup();
        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "ComplexMMOStats passed all checks!");
    }

    @Override
    public void onDisable() {
    }

    public void reloadConfig() {
        WeaponConfig.load();
        ShieldConfig.load();
        ArmorConfig.load();
    }
}
