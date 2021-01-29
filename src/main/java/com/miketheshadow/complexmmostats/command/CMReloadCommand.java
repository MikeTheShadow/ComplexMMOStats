package com.miketheshadow.complexmmostats.command;

import com.miketheshadow.complexmmostats.ComplexMMOStats;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CMReloadCommand extends BasicCommand {
    public CMReloadCommand() {
        super("cmreload");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(ChatColor.GREEN + "Starting reload!");
        ComplexMMOStats.INSTANCE.reloadConfig();
        sender.sendMessage(ChatColor.GREEN + "Reload complete!");
        return true;
    }
}
