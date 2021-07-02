package com.miketheshadow.complexmmostats.api;

import com.miketheshadow.complexmmostats.utils.CombatPlayer;
import com.miketheshadow.complexmmostats.utils.ItemChecker;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Random;

public class PlayerAttacksEntityAPI extends PlayerAttackBaseAPI {

    //TODO set killer in here somewhere thanks.
    public static void DealDamage(Player damager, LivingEntity defender, double percentOfDamage) {

        CombatPlayer combatDamager = CombatPlayer.getPlayer(damager);
        if(cannotAttack(damager,defender)) return;
        //Check if player is using a weapon
        if(!ItemChecker.isAnyWeapon(damager.getInventory().getItemInMainHand())) {
            return;
        }
        //get damage use would deal if their opponent had 0 hp
        double damage = (combatDamager.getDamage() + (combatDamager.getDamage() * combatDamager.getPercentBonusDamage())) + combatDamager.getFlatBonusAD();
        //apply multiplier
        damage = damage * (percentOfDamage / 100);

        //critical calculation
        if(combatDamager.getCriticalRate() > (new Random()).nextInt(100)) {
            damage += (combatDamager.getCriticalDamage() / 100) * damage;
            Location critLocation = defender.getLocation();
            defender.getWorld().spawnParticle(Particle.CRIT,critLocation,50);
        }

        /*
        TODO figure out how mobs defense will be calculated
        Calculate defense damage reduction
        double reduction = damage * combatDefender.getPercentDamageReduction(armorBypassAmount);
        if(reduction < 0) reduction = 0;
        damage -= reduction;
         */

        damage = damage < 1 ? 1 : damage;
        //TODO add fake knock-back?
        //if(damage > 1);
        if(combatInfo.containsKey(damager.getUniqueId())) {
            //TODO decide the two ways to do this. Either 1. Damage is reduced to a set amount
            //TODO or damage is based on attack speed and if you swing fast you get the same damage as a slow swinger
            //TODO unless you don't attack within a second
            if(!combatInfo.get(damager.getUniqueId()).isAttackReset()) {
                damage = damage * .2;
            }
        }

        if (defender.getHealth() - damage <= 0) {
            //TODO ENABLE THIS IS PAPER IS ADDED
            //defender.setKiller(damager);
            defender.setHealth(0);
        } else {
            defender.setHealth(defender.getHealth() - damage);
        }
        //update combat log
        updateCombatTimers(damager);
        updateAttackTimer(damager,combatDamager);
    }
}
