package com.miketheshadow.complexmmostats.utils;

import com.miketheshadow.complexmmostats.ComplexMMOStats;
import com.miketheshadow.mmotextapi.text.ItemStat;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

import static com.miketheshadow.complexmmostats.utils.CMMOKeys.HANDLING;
import static com.miketheshadow.mmotextapi.text.ItemStat.*;

public class ItemBuilder {

    public static void modifyContainer(int handling, int durability, HashMap<ItemStat, Integer> stats, int rarity, Player player, PersistentDataContainer container) {
        container.set(CMMOKeys.DURABILITY, PersistentDataType.INTEGER, durability);
        container.set(HANDLING, PersistentDataType.INTEGER, handling);
        container.set(CMMOKeys.RARITY, PersistentDataType.INTEGER, rarity);
        container.set(CMMOKeys.CMMOITEM, PersistentDataType.INTEGER, 1);
        //if item is gear give it a unique id
        if (handling == 1 || handling == 2 || handling == 3 || handling == 5)
            container.set(CMMOKeys.UNIQUE_ITEM_ID, PersistentDataType.STRING, LocalDateTime.now() + "|" + UUID.randomUUID());
        if (player != null)
            container.set(CMMOKeys.CREATOR_UUID, PersistentDataType.STRING, player.getUniqueId().toString());

        if (stats.containsKey(STRENGTH)) {
            container.set(STRENGTH.getNameSpacedKey(ComplexMMOStats.INSTANCE), PersistentDataType.INTEGER, stats.get(STRENGTH));
        }
        if (stats.containsKey(ItemStat.STAMINA)) {
            container.set(STAMINA.getNameSpacedKey(ComplexMMOStats.INSTANCE), PersistentDataType.INTEGER, stats.get(ItemStat.STAMINA));
        }
        if (stats.containsKey(ItemStat.AGILITY)) {
            container.set(AGILITY.getNameSpacedKey(ComplexMMOStats.INSTANCE), PersistentDataType.INTEGER, stats.get(ItemStat.AGILITY));
        }
        if (stats.containsKey(ItemStat.INTELLIGENCE)) {
            container.set(INTELLIGENCE.getNameSpacedKey(ComplexMMOStats.INSTANCE), PersistentDataType.INTEGER, stats.get(ItemStat.INTELLIGENCE));
        }

    }

}
