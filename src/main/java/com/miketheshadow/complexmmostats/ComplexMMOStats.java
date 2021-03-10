package com.miketheshadow.complexmmostats;

import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.miketheshadow.complexmmostats.combat.ComplexLoginEvent;
import com.miketheshadow.complexmmostats.combat.HealthRegenEvent;
import com.miketheshadow.complexmmostats.combat.PlayerAttackPlayerEvent;
import com.miketheshadow.complexmmostats.combat.SwapWeaponsEvent;
import com.miketheshadow.complexmmostats.command.CMReloadCommand;
import com.miketheshadow.complexmmostats.command.SummonItemCommand;
import com.miketheshadow.complexmmostats.item.armor.ArmorConfig;
import com.miketheshadow.complexmmostats.item.weapon.ShieldConfig;
import com.miketheshadow.complexmmostats.item.weapon.WeaponConfig;
import com.miketheshadow.complexmmostats.command.TypeCommand;
import com.miketheshadow.complexmmostats.listener.PlayerAttacksEntityListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class ComplexMMOStats extends JavaPlugin {

    public static ComplexMMOStats INSTANCE;

    @Override
    public void onEnable() {

        if (!Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
            getLogger().severe("*** HolographicDisplays is not installed or not enabled. ***");
            //this.setEnabled(false);
            //return;
        } else {
            try {
                HologramsAPI.getHolograms(this).clear();
            } catch (Exception ignored) {}
        }
        // Plugin startup logic
        INSTANCE = this;
        if(!this.getDataFolder().exists()) this.getDataFolder().mkdir();
        WeaponConfig.load();
        ShieldConfig.load();
        ArmorConfig.load();
        PluginManager manager = Bukkit.getServer().getPluginManager();
        manager.registerEvents(new PlayerAttackPlayerEvent(),this);
        manager.registerEvents(new ComplexLoginEvent(),this);
        manager.registerEvents(new SwapWeaponsEvent(),this);
        manager.registerEvents(new HealthRegenEvent(),this);
        manager.registerEvents(new PlayerAttacksEntityListener(), this);
        new SummonItemCommand();
        new TypeCommand();
        new CMReloadCommand();
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "ComplexMMOStats passed all checks!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void reloadConfig() {
        WeaponConfig.load();
        ShieldConfig.load();
        ArmorConfig.load();
    }
}
