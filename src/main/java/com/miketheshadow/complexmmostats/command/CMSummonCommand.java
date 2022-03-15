package com.miketheshadow.complexmmostats.command;

import com.miketheshadow.complexmmostats.annotations.CMMOCommand;
import com.miketheshadow.complexmmostats.item.armor.ArmorConfig;
import com.miketheshadow.complexmmostats.item.weapon.ShieldConfig;
import com.miketheshadow.complexmmostats.item.weapon.WeaponConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CMMOCommand(command = "cmsummon")
public class CMSummonCommand  implements CommandExecutor {

    private static final String[] parts = {"HELMET", "CHESTPLATE", "LEGGINGS", "BOOTS"};

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        //argument len check
        if (args.length != 3) {
            sender.sendMessage(ChatColor.YELLOW + "/cmsummon [itemName] [player] [isCrafted]");
            return true;
        }

        //player exists check. Do this before item because we'll need to supply the player name to the weapon crafting
        Player player = Bukkit.getPlayer(args[1]);
        if (player == null) {
            sender.sendMessage(ChatColor.RED + "Unable to find player " + args[1]);
            return true;
        }
        //item exist check

        String itemName = args[0];
        Player crafter = args[2].equalsIgnoreCase("true") ? player : null;

        ItemStack stack = WeaponConfig.getItemFromConfig(itemName, crafter);
        if (stack == null) stack = ShieldConfig.getItemFromConfig(itemName, crafter);
        if (stack == null) {
            for (String partName : parts) {
                if (itemName.contains(partName)) {
                    stack = ArmorConfig.getItemFromConfig(itemName, partName, crafter);
                    break;
                }
            }

        }
        if (stack == null) {
            player.sendMessage("Unable to get item " + args[0]);
            return true;
        }
        /* POTENTIAL INVENTORY FULL CODE
        if(inventory.getSize() - inventory.getContents().length < 1) {
            player.sendMessage(ChatColor.RED + "Inventory full! Item " + stack.getItemMeta().getDisplayName() + " dropped on ground!");
            player.getWorld().dropItem(player.getLocation(),stack);
        }
         */

        player.getInventory().addItem(stack);

        return true;
    }


}
