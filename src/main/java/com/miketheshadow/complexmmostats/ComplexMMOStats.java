package com.miketheshadow.complexmmostats;

import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.miketheshadow.complexmmostats.annotations.CMMOCommand;
import com.miketheshadow.complexmmostats.combat.ComplexLoginEvent;
import com.miketheshadow.complexmmostats.combat.HealthRegenEvent;
import com.miketheshadow.complexmmostats.combat.PlayerAttackPlayerEvent;
import com.miketheshadow.complexmmostats.combat.SwapWeaponsEvent;
import com.miketheshadow.complexmmostats.command.CMReloadCommand;
import com.miketheshadow.complexmmostats.command.CMSummonCommand;
import com.miketheshadow.complexmmostats.command.CMTestCommand;
import com.miketheshadow.complexmmostats.command.TypeCommand;
import com.miketheshadow.complexmmostats.item.armor.ArmorConfig;
import com.miketheshadow.complexmmostats.item.weapon.ShieldConfig;
import com.miketheshadow.complexmmostats.item.weapon.WeaponConfig;
import com.miketheshadow.complexmmostats.listener.PlayerAttacksEntityListener;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.logging.Level;

import static org.reflections.scanners.Scanners.SubTypes;
import static org.reflections.scanners.Scanners.TypesAnnotated;

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
        PluginManager manager = Bukkit.getServer().getPluginManager();
        manager.registerEvents(new PlayerAttackPlayerEvent(), this);
        manager.registerEvents(new ComplexLoginEvent(), this);
        manager.registerEvents(new SwapWeaponsEvent(), this);
        manager.registerEvents(new HealthRegenEvent(), this);
        manager.registerEvents(new PlayerAttacksEntityListener(), this);

        try {
            registerCommands();
        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "ComplexMMOStats passed all checks!");
    }

    private void registerCommands() throws Exception {

        Reflections reflections = new Reflections("com.miketheshadow.complexmmostats.command");

        Set<Class<?>> annotated = reflections.get(SubTypes.of(TypesAnnotated.with(CMMOCommand.class)).asClass());

        for(Class<?> clazz : annotated) {
            CMMOCommand commandAnnotation = clazz.getAnnotation(CMMOCommand.class);
            String commandName = commandAnnotation.command();
            getLogger().info("Registering command: "  + commandName);
            PluginCommand command = Bukkit.getServer().getPluginCommand(commandName);

            if(command == null) {
                throw new NotImplementedException("Missing plugin.yml registration for command: " + commandName);
            }

            command.setExecutor((CommandExecutor) clazz.getDeclaredConstructor().newInstance());
        }

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
