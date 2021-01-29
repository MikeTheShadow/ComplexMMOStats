package com.miketheshadow.complexmmostats.combat;

import com.miketheshadow.complexmmostats.utils.CombatPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ComplexLoginEvent implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(5000);

        if(!CombatPlayer.players.containsKey(event.getPlayer().getUniqueId()))
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Added player: " + event.getPlayer().getName() + "\n" + CombatPlayer.players.size());

            CombatPlayer.players.put(event.getPlayer().getUniqueId(),new CombatPlayer(event.getPlayer(),null));
    }

}
