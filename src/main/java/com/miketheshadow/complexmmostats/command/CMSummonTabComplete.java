package com.miketheshadow.complexmmostats.command;

import com.miketheshadow.complexmmostats.item.armor.ArmorConfig;
import com.miketheshadow.complexmmostats.item.weapon.ShieldConfig;
import com.miketheshadow.complexmmostats.item.weapon.WeaponConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class CMSummonTabComplete implements TabCompleter {


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {


        if(args.length == 3) {
            return Arrays.asList("true", "false");
        }

        if(args.length > 1) return null;



        Set<String> shieldIds = ShieldConfig.SHIELD_CONFIG.getKeys(false);
        Set<String> weaponIds =  WeaponConfig.WEAPON_CONFIG.getKeys(false);
        Set<String> armorIds  = ArmorConfig.ARMOR_CONFIG.getKeys(false);
        List<String> completeArmorIds = new ArrayList<>();
        for(String id : armorIds) {

            completeArmorIds.add(id + "_HELMET");
            completeArmorIds.add(id + "_CHESTPLATE");
            completeArmorIds.add(id + "_LEGGINGS");
            completeArmorIds.add(id + "_BOOTS");
        }

        List<String> returnList = new ArrayList<>(shieldIds);

        returnList.addAll(weaponIds);
        returnList.addAll(completeArmorIds);

        if(args.length == 1) {
            List<String> modifiedReturnList = new ArrayList<>();

            for(String s : returnList) {
                if(s.startsWith(args[0])) modifiedReturnList.add(s);
            }

            return modifiedReturnList;
        }

        return returnList;
    }

}
