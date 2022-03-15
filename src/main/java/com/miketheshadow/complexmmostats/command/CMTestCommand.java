package com.miketheshadow.complexmmostats.command;

import com.miketheshadow.complexmmostats.annotations.CMMOCommand;
import com.miketheshadow.complexmmostats.utils.UnicodeConversion;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

@CMMOCommand(command = "cmtest")
public class CMTestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) return true;

        ItemStack stack = new ItemStack(Material.STICK);

        ItemMeta meta = stack.getItemMeta();

        meta.setDisplayName("Stick of Wacking");

        List<String> lore = new ArrayList<>();

        String R = ChatColor.RESET + "" + ChatColor.WHITE;

        lore.add(R + UnicodeConversion.STRENGTH_ICON + " " + UnicodeConversion.numToUnicode(453));
        lore.add(R + UnicodeConversion.STAMINA_ICON + " " + UnicodeConversion.numToUnicode(789));
        lore.add(R + UnicodeConversion.AGILITY_ICON + " " + UnicodeConversion.numToUnicode(62));
        lore.add(R + UnicodeConversion.numToUnicode(1234567890987654321L));
        lore.add(R + UnicodeConversion.ONE_HANDER_ICON);
        lore.add(R + UnicodeConversion.TWO_HANDER_ICON);
        lore.add(R + UnicodeConversion.NUM_1);
        lore.add(R + UnicodeConversion.NUM_2);
        lore.add(R + UnicodeConversion.NUM_3);
        lore.add(R + UnicodeConversion.NUM_4);
        lore.add(R + UnicodeConversion.NUM_5);

        meta.setLore(lore);

        stack.setItemMeta(meta);

        ((Player) sender).getInventory().addItem(stack);

        return true;
    }
}
