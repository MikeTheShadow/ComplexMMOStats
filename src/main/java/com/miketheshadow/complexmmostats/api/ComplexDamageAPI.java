package com.miketheshadow.complexmmostats.api;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.miketheshadow.complexmmostats.ComplexMMOStats;
import com.miketheshadow.complexmmostats.utils.AttackTimer;
import com.miketheshadow.complexmmostats.utils.CombatPlayer;
import com.miketheshadow.complexmmostats.utils.ItemChecker;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.MILLIS;

public final class ComplexDamageAPI {

    /**
     * The combat hashmap. This hashmap store the information on how long ago a player attacked another player
     * This servers the purpose of both telling when the last attack was so that damage can be calculated accordingly
     * and when the player was last attacked (combat log purposes)
     */
    public static HashMap<UUID, AttackTimer> combatInfo = new HashMap<>();

    public static void dealDamage(Player damager, Player defender, double percentOfDamage) {

        CombatPlayer combatDamager = CombatPlayer.getPlayer(damager);
        CombatPlayer combatDefender = CombatPlayer.getPlayer(defender);

        //TODO decide how you can attack in the area. Probably make it so that you can disable skills in a peace zone
        if(canAttack(damager)) return;

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


        boolean didBlock = false;
        if(ItemChecker.isOneHandedWeapon(defender.getInventory().getItemInMainHand()))
            didBlock = combatDefender.getBlockRate() > (new Random()).nextInt(100) && ItemChecker.isShield(defender.getInventory().getItemInOffHand());
        boolean blockBypass = false;
        int armorBypassAmount = 0;

        //Shield calculations
        if(ItemChecker.isTwoHandedWeapon(damager.getInventory().getItemInMainHand())
                && ItemChecker.isShield(defender.getInventory().getItemInOffHand())) {

            //calculate ignore shield
            if(didBlock && (new Random()).nextInt(100) > 50) {
                blockBypass = true;
            }
            //calculate defense bypass for block
            if(didBlock ^ !blockBypass) {
                if((new Random()).nextInt(100) > 50) {
                    armorBypassAmount += NBTItem.convertItemtoNBT(defender.getInventory().getItemInOffHand()).getCompound("tag").getInteger("defense");
                    createTemporaryHologram(damager,defender, ChatColor.RED + "Shield Penetrated!");
                    createTemporaryHologram(defender,damager,ChatColor.GREEN + "Shield Penetrated!");
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

            createTemporaryHologram(damager,defender,ChatColor.GREEN + "Parried!");
            createTemporaryHologram(defender,damager,ChatColor.RED + "Parried!");
        }
        else if(didBlock && !blockBypass) {
            defender.getWorld().playSound(defender.getLocation(), Sound.ITEM_SHIELD_BLOCK,10,5);
            createTemporaryHologram(damager,defender,ChatColor.GREEN + "Blocked!");
            createTemporaryHologram(defender,damager,ChatColor.RED + "Blocked!");
        }
        //if neither block nor parry then continue
        else {
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
            if (!(defender.getHealth() - damage <= 0)) {
                defender.setKiller(damager);
                defender.setHealth(defender.getHealth() - damage);
            }

        }
        //update combat log
        updateCombatTimers(damager);
        updateCombatTimers(defender);
        updateAttackTimer(damager,combatDamager);
    }

    private static boolean canAttack(Player attacker) {
        return true;
    }

    private static void createTemporaryHologram(Player player,Player player2,String text) {

        Location location = player.getLocation();
        location.add(0,2,0);

        Hologram hologram = HologramsAPI.createHologram(ComplexMMOStats.INSTANCE, location);
        hologram.appendTextLine(text);
        hologram.getVisibilityManager().setVisibleByDefault(false);
        hologram.getVisibilityManager().hideTo(player);
        hologram.getVisibilityManager().showTo(player2);
        (new BukkitRunnable() {
            @Override
            public void run() {
                try { Thread.sleep(1000); }
                catch (InterruptedException e) { e.printStackTrace(); }
                hologram.delete();
            }
        }).runTaskAsynchronously(ComplexMMOStats.INSTANCE);
    }

    private static void updateAttackTimer(Player player, CombatPlayer combatPlayer) {
        UUID uuid = player.getUniqueId();
        LocalDateTime attackResetTime = LocalDateTime.now().plus((long) ((1 / combatPlayer.getAttackSpeed()) * 1000),MILLIS);

        if(combatInfo.containsKey(uuid)) {
            combatInfo.get(uuid).setAttackTime(attackResetTime);
        } else {
            combatInfo.put(uuid,new AttackTimer(attackResetTime));
        }
    }

    private static void updateCombatTimers(Player player) {
        UUID uuid = player.getUniqueId();
        if(combatInfo.containsKey(uuid)) {
            combatInfo.get(uuid).setCombatTime(LocalDateTime.now());
        } else {
            combatInfo.put(uuid,new AttackTimer());
        }
    }

}
