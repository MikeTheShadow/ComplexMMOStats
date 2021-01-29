package com.miketheshadow.complexmmostats;

import com.miketheshadow.complexmmostats.combat.ComplexLoginEvent;
import com.miketheshadow.complexmmostats.combat.PlayerAttackPlayerEvent;
import com.miketheshadow.complexmmostats.combat.SwapWeaponsEvent;
import com.miketheshadow.complexmmostats.command.CMReloadCommand;
import com.miketheshadow.complexmmostats.command.SummonItemCommand;
import com.miketheshadow.complexmmostats.item.armor.ArmorConfig;
import com.miketheshadow.complexmmostats.item.weapon.ShieldConfig;
import com.miketheshadow.complexmmostats.item.weapon.WeaponConfig;
import com.miketheshadow.complexmmostats.command.TypeCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class ComplexMMOStats extends JavaPlugin {

    public static ComplexMMOStats INSTANCE;

    @Override
    public void onEnable() {
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
