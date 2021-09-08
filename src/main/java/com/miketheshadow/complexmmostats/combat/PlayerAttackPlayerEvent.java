package com.miketheshadow.complexmmostats.combat;

import com.miketheshadow.complexmmostats.api.PlayerAttacksPlayerAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Player attacks a player with a weapon only. This will not account for attacks with fire tick explosives etc.
 */
public class PlayerAttackPlayerEvent implements Listener {

    /**
     *
     * @param event PvP event just needs to be parsed to make sure it's not a mob as mob damage is calculated differently
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerAttackPlayerEvent(EntityDamageByEntityEvent event) {

        //Check if event canceled.
        if(event.isCancelled())return;
        //IsPlayer check
        if(!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player))return;
        //Make sure this is a regular attack
        if(!event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) return;

        Player damager = (Player) event.getDamager();
        Player defender = (Player) event.getEntity();
        event.setDamage(0);
        PlayerAttacksPlayerAPI.dealDamage(damager,defender,100.0d);

    }


}
