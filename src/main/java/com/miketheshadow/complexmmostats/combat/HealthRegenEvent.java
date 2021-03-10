package com.miketheshadow.complexmmostats.combat;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class HealthRegenEvent implements Listener {

    @EventHandler
    public void onHealthRegenEvent(EntityRegainHealthEvent event) {
        if(!(event.getEntity() instanceof Player)) return;

        //TODO add custom health regen. Use PlayerAttackPlayerEvent.combatInfo to garner if user is in battle or not

        event.setCancelled(true);
        //event.setAmount(400);

    }

    @EventHandler
    public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
        event.getPlayer().setHealth(event.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
    }

}
