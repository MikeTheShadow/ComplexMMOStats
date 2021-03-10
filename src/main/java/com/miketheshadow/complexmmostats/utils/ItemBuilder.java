package com.miketheshadow.complexmmostats.utils;

import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

import static com.miketheshadow.complexmmostats.utils.NBTData.HANDLING;
import static com.miketheshadow.complexmmostats.utils.NBTData.STAMINA;

public class ItemBuilder {

    public static PersistentDataContainer modifyContainer(int handling, int durability, HashMap<Stat, Integer> stats, int rarity, Player player, PersistentDataContainer container) {
        container.set(NBTData.DURABILITY, PersistentDataType.INTEGER,durability);
        container.set(HANDLING, PersistentDataType.INTEGER, handling);
        container.set(NBTData.RARITY, PersistentDataType.INTEGER, rarity);
        container.set(NBTData.CMMOITEM,PersistentDataType.INTEGER,1);
        //if item is gear give it a unique id
        if(handling == 1 || handling == 2 || handling == 3 || handling == 5) container.set(NBTData.UNIQUE_ITEM_ID,PersistentDataType.STRING, LocalDateTime.now().toString() + "|" + UUID.randomUUID().toString());
        if(player != null)container.set(NBTData.CREATOR_UUID,PersistentDataType.STRING,player.getUniqueId().toString());
        /*
        compound.setInteger("durability",durability);
        compound.setInteger("handling",handling);
        compound.setInteger("rarity",rarity);
        compound.setBoolean("CMMOITEM",true);
        if(player != null)compound.setString("creatorUUID",player.getUniqueId().toString());
        for(Map.Entry<Stat,Integer> stat : stats.entrySet()) {
            compound.setInteger(stat.getKey().name(),stat.getValue());
        }
         */
        if(stats.containsKey(Stat.STRENGTH)) {
            container.set(NBTData.STRENGTH,PersistentDataType.INTEGER,stats.get(Stat.STRENGTH));
        }
        if(stats.containsKey(Stat.STAMINA)) {
            container.set(STAMINA,PersistentDataType.INTEGER,stats.get(Stat.STAMINA));
        }
        if(stats.containsKey(Stat.AGILITY)) {
            container.set(NBTData.AGILITY,PersistentDataType.INTEGER,stats.get(Stat.AGILITY));
        }
        if(stats.containsKey(Stat.INTELLIGENCE)) {
            container.set(NBTData.INTELLIGENCE,PersistentDataType.INTEGER,stats.get(Stat.INTELLIGENCE));
        }

        return container;
    }

}
