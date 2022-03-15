package com.miketheshadow.complexmmostats.command;

import com.miketheshadow.complexmmostats.annotations.CMMOCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CMMOCommand(command = "type")
public class TypeCommand extends BasicCommand {
    public TypeCommand() {
        super("type");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) return false;
        sender.sendMessage(((Player) sender).getInventory().getItemInMainHand().getType().toString());
        return true;
    }
}
