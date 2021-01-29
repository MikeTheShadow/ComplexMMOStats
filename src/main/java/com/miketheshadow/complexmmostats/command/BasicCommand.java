package com.miketheshadow.complexmmostats.command;

import com.miketheshadow.complexmmostats.ComplexMMOStats;
import org.bukkit.command.CommandExecutor;

public abstract class BasicCommand implements CommandExecutor {

    public BasicCommand(String name) {
        ComplexMMOStats.INSTANCE.getCommand(name).setExecutor(this);
    }
}
