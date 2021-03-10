package com.miketheshadow.complexmmostats.listener;

import com.miketheshadow.complexmmostats.api.PlayerAttacksEntityAPI;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerAttacksEntityListener implements Listener {

    @EventHandler
    public void playerAttacksEntityListener(EntityDamageByEntityEvent event) {

        //Check if event canceled.
        if(event.isCancelled())return;
        //Attacker is player and defender is not
        if(!(event.getDamager() instanceof Player) || (event.getEntity() instanceof Player))return;
        //Make sure this is a regular attack
        if(!event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) return;

        //make sure entity attacked is living
        if(!(event.getEntity() instanceof LivingEntity)) return;

        LivingEntity defender = (LivingEntity) event.getEntity();
        Player player = (Player) event.getDamager();
        event.setDamage(0);
        PlayerAttacksEntityAPI.DealDamage(player,defender,100.0d);
    }

}
