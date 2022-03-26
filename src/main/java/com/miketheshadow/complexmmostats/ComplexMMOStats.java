package com.miketheshadow.complexmmostats;

import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.miketheshadow.autoregister.api.AutoRegister;
import com.miketheshadow.complexmmostats.item.armor.ArmorConfig;
import com.miketheshadow.complexmmostats.item.weapon.ShieldConfig;
import com.miketheshadow.complexmmostats.item.weapon.WeaponConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class ComplexMMOStats extends JavaPlugin {

    @Override
    public void onEnable() {

        AutoRegister autoRegister = new AutoRegister(this,"com.miketheshadow.complexmmostats");
        autoRegister.defaultSetup();

        if (!Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
            getLogger().warning("*** HolographicDisplays is not installed or not enabled. ***");
        } else {
            try {
                HologramsAPI.getHolograms(this).clear();
                getLogger().log(Level.FINEST, "Holograms enabled!");
            } catch (Exception ignored) {
            }
        }

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
