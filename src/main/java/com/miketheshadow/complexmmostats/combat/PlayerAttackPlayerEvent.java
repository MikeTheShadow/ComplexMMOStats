package com.miketheshadow.complexmmostats.combat;

import com.miketheshadow.complexmmostats.utils.CombatPlayer;
import com.miketheshadow.complexmmostats.utils.ItemChecker;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

/**
 * Player attacks a player with a weapon only. This will not account for attacks with fire tick explosives etc.
 */
public class PlayerAttackPlayerEvent implements Listener {

    /**
     * The combat hashmap. This hashmap store the information on how long ago a player attacked another player
     * This servers the purpose of both telling when the last attack was so that damage can be calculated accordingly
     * and when the player was last attacked (combat log purposes)
     */

    public static HashMap<UUID, LocalDateTime> combatInfo = new HashMap<>();
    public static HashMap<UUID, LocalDateTime> attackSpeed = new HashMap<>();

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

        if(!ItemChecker.isAnyWeapon(damager.getInventory().getItemInMainHand())) {
            event.setDamage(1);
            return;
        }

        CombatPlayer combatDamager = CombatPlayer.getPlayer(damager);
        CombatPlayer combatDefender = CombatPlayer.getPlayer(defender);

        double damage = (combatDamager.getDamage() + (combatDamager.getDamage() * combatDamager.getPercentBonusDamage())) + combatDamager.getFlatBonusAD();

        //critical calculation
        if(combatDamager.getCriticalRate() > (new Random()).nextInt(100)) {
            damage += (combatDamager.getCriticalDamage() / 100) * damage;
            Location critLocation = defender.getLocation();
            defender.getWorld().spawnParticle(Particle.CRIT,critLocation,50);
        }



        boolean didBlock = combatDefender.getBlockRate() > (new Random()).nextInt(100) && ItemChecker.isShield(defender.getInventory().getItemInOffHand());
        boolean blockBypass = false;
        int armorBypassAmount = 0;

        if(ItemChecker.isTwoHandedWeapon(damager.getInventory().getItemInMainHand()) && ItemChecker.isShield(defender.getInventory().getItemInOffHand())) {

            //calculate ignore shield
            if(didBlock && (new Random()).nextInt(100) > 50) {
                blockBypass = true;
                damager.sendMessage(ChatColor.GREEN + "Block Ignored!");
                defender.sendMessage(ChatColor.RED + "Block Ignored!");
            }
            //calculate defense bypass for block
            if(didBlock ^ !blockBypass) {
                if((new Random()).nextInt(100) > 50) {
                    armorBypassAmount += NBTItem.convertItemtoNBT(defender.getInventory().getItemInOffHand()).getCompound("tag").getInteger("defense");
                    damager.sendMessage(ChatColor.GREEN + "Shield Penetrated!");
                    defender.sendMessage(ChatColor.RED + "Shield Penetrated!");
                }
            }


        }

        //Calculate defense damage reduction
        double reduction = damage * combatDefender.getPercentDamageReduction(armorBypassAmount);
        if(reduction < 0) reduction = 0;
        damage -= reduction;

        //do block and parry
        if(combatDefender.getParryRate() > (new Random()).nextInt(100) && ItemChecker.isAnyWeapon(defender.getInventory().getItemInMainHand())) {
            defender.getWorld().playSound(defender.getLocation(), Sound.BLOCK_ANVIL_LAND,10,5);
            damager.sendMessage(ChatColor.RED + "Parried!");
            defender.sendMessage(ChatColor.GREEN + "Parried!");
        }
        else if(didBlock && !blockBypass) {
            defender.getWorld().playSound(defender.getLocation(), Sound.ITEM_SHIELD_BLOCK,10,5);
            damager.sendMessage(ChatColor.RED + "Blocked!");
            defender.sendMessage(ChatColor.GREEN + "Blocked!");
        }
        //if neither block nor parry then continue
        else {
            if(defender.getHealth() - damage < 0) {
                defender.setHealth(0);
            }
            damage = damage < 0 ? 1 : damage;

            if(defender.getHealth() - damage < 0) {
                defender.setHealth(0);
            } else {
                defender.setHealth(defender.getHealth() - damage);
            }
        }

        //update combat log
        updateCombatTimers(damager);
        updateCombatTimers(defender);

        event.setDamage(0);
    }

    private static void updateCombatTimers(Player player) {
        if(combatInfo.containsKey(player.getUniqueId())) {
            combatInfo.replace(player.getUniqueId(),LocalDateTime.now());
        } else {
            combatInfo.put(player.getUniqueId(),LocalDateTime.now());
        }
    }

    @EventHandler
    public void onDurabilityChange(PlayerItemDamageEvent event) {
        if(ItemChecker.isValidComplexMMOItem(event.getItem()))event.setCancelled(true);
    }
}
