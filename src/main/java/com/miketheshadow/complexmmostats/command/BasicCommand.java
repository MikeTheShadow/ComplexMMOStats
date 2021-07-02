package com.miketheshadow.complexmmostats.command;

import com.miketheshadow.complexmmostats.ComplexMMOStats;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;

public abstract class BasicCommand implements CommandExecutor {

    public BasicCommand(String name) {
        ComplexMMOStats.INSTANCE.getCommand(name).setExecutor(this);
    }

    public BasicCommand(String name, TabCompleter completer) {
        ComplexMMOStats.INSTANCE.getCommand(name).setExecutor(this);
        ComplexMMOStats.INSTANCE.getCommand(name).setTabCompleter(completer);
    }
}
