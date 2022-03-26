package com.miketheshadow.complexmmostats.command;

import com.miketheshadow.autoregister.annotations.InjectPlugin;
import com.miketheshadow.autoregister.annotations.RegisterCommand;
import com.miketheshadow.complexmmostats.ComplexMMOStats;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

@RegisterCommand(commandName = "cmreload")
public class CMReloadCommand implements CommandExecutor {

    @InjectPlugin
    ComplexMMOStats plugin;

    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        sender.sendMessage(ChatColor.GREEN + "Starting reload!");
        plugin.reloadConfig();
        sender.sendMessage(ChatColor.GREEN + "Reload complete!");
        return true;
    }
}
