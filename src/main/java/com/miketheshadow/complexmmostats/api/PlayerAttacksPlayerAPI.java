package com.miketheshadow.complexmmostats.api;

import com.miketheshadow.autoregister.annotations.InjectPlugin;
import com.miketheshadow.complexmmostats.ComplexMMOStats;
import com.miketheshadow.complexmmostats.utils.CMMOKeys;
import com.miketheshadow.complexmmostats.utils.CombatPlayer;
import com.miketheshadow.complexmmostats.utils.ItemChecker;
import com.miketheshadow.mmotextapi.text.ItemStat;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.util.Random;

public class PlayerAttacksPlayerAPI extends PlayerAttackBaseAPI {

    /**
     * The combat hashmap. This hashmap store the information on how long ago a player attacked another player
     * This servers the purpose of both telling when the last attack was so that damage can be calculated accordingly
     * and when the player was last attacked (combat log purposes)
     */

    @InjectPlugin
    private static ComplexMMOStats plugin;

    public static void dealDamage(Player damager, Player defender, double percentOfDamage) {

        CombatPlayer combatDamager = CombatPlayer.getPlayer(damager);
        CombatPlayer combatDefender = CombatPlayer.getPlayer(defender);

        //TODO decide how you can attack in the area. Probably make it so that you can disable skills in a peace zone
        if (cannotAttack(damager, defender)) return;

        //Check if player is using a weapon
        if (!ItemChecker.isAnyWeapon(damager.getInventory().getItemInMainHand())) {
            return;
        }

        //get damage use would deal if their opponent had 0 hp
        double damage = (combatDamager.getDamage() + (combatDamager.getDamage() * combatDamager.getPercentBonusDamage())) + combatDamager.getFlatBonusAD();

        //apply multiplier
        damage = damage * (percentOfDamage / 100);

        //critical calculation
        if (combatDamager.getCriticalRate() > (new Random()).nextInt(100)) {
            damage += (combatDamager.getCriticalDamage() / 100) * damage;
            Location critLocation = defender.getLocation();
            defender.getWorld().spawnParticle(Particle.CRIT, critLocation, 50);
        }

        boolean didBlock = false;
        if (ItemChecker.isOneHandedWeapon(defender.getInventory().getItemInMainHand()))
            didBlock = combatDefender.getBlockRate() > (new Random()).nextInt(100) && ItemChecker.isShield(defender.getInventory().getItemInOffHand());
        boolean blockBypass = false;
        int armorBypassAmount = 0;

        //Shield calculations
        if (ItemChecker.isTwoHandedWeapon(damager.getInventory().getItemInMainHand())
                && ItemChecker.isShield(defender.getInventory().getItemInOffHand())) {

            //calculate ignore shield
            if (didBlock && (new Random()).nextInt(100) > 50) {
                blockBypass = true;
            }
            //calculate defense bypass for block
            if (didBlock ^ !blockBypass) {

                if ((new Random()).nextInt(100) > 50) {
                    armorBypassAmount += defender.getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().get(ItemStat.DEFENSE.getNameSpacedKey(plugin), PersistentDataType.INTEGER);
                    createTemporaryHologram(damager, defender, ChatColor.RED + "Shield Penetrated!");
                    createTemporaryHologram(defender, damager, ChatColor.GREEN + "Shield Penetrated!");
                }
            }
        }

        //Calculate defense damage reduction
        double reduction = damage * combatDefender.getPercentDamageReduction(armorBypassAmount);
        if (reduction < 0) reduction = 0;
        damage -= reduction;

        //do block and parry
        if (combatDefender.getParryRate() > (new Random()).nextInt(100) && ItemChecker.isAnyWeapon(defender.getInventory().getItemInMainHand())) {
            defender.getWorld().playSound(defender.getLocation(), Sound.BLOCK_ANVIL_LAND, 10, 5);

            createTemporaryHologram(damager, defender, ChatColor.GREEN + "Parried!");
            createTemporaryHologram(defender, damager, ChatColor.RED + "Parried!");
        } else if (didBlock && !blockBypass) {
            defender.getWorld().playSound(defender.getLocation(), Sound.ITEM_SHIELD_BLOCK, 10, 5);
            createTemporaryHologram(damager, defender, ChatColor.GREEN + "Blocked!");
            createTemporaryHologram(defender, damager, ChatColor.RED + "Blocked!");
        }
        //if neither block nor parry then continue
        else {
            damage = damage < 1 ? 1 : damage;
            //TODO add fake knock-back?
            //if(damage > 1);
            if (combatInfo.containsKey(damager.getUniqueId())) {
                //TODO decide the two ways to do this. Either 1. Damage is reduced to a set amount
                //TODO or damage is based on attack speed and if you swing fast you get the same damage as a slow swinger
                //TODO unless you don't attack within a second
                if (!combatInfo.get(damager.getUniqueId()).isAttackReset()) {
                    damage = damage * .2;
                }
            }
            if (defender.getHealth() - damage <= 0) {
                //TODO enable if paper comes back
                //defender.setKiller(damager);
                defender.setHealth(0);
            } else {
                defender.setHealth(defender.getHealth() - damage);
            }

        }
        //update combat log
        updateCombatTimers(damager, defender);
        updateAttackTimer(damager, combatDamager);
    }

}
