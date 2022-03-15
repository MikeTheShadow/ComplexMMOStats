package com.miketheshadow.complexmmostats.command;

import com.miketheshadow.complexmmostats.ComplexMMOStats;
import com.miketheshadow.complexmmostats.annotations.CMMOCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

@CMMOCommand(command = "cmreload")
public class CMReloadCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(ChatColor.GREEN + "Starting reload!");
        ComplexMMOStats.INSTANCE.reloadConfig();
        sender.sendMessage(ChatColor.GREEN + "Reload complete!");
        return true;
    }
}
