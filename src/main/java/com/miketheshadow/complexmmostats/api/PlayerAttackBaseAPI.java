package com.miketheshadow.complexmmostats.api;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.miketheshadow.complexmmostats.ComplexMMOStats;
import com.miketheshadow.complexmmostats.utils.AttackTimer;
import com.miketheshadow.complexmmostats.utils.CombatPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.MILLIS;

public class PlayerAttackBaseAPI {

    public static HashMap<UUID, AttackTimer> combatInfo = new HashMap<>();

    public static boolean canAttack(Player attacker, Entity defender) {
        EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(attacker,defender, EntityDamageEvent.DamageCause.CUSTOM,0);
        Bukkit.getPluginManager().callEvent(event);
        return event.isCancelled();
    }

    public static void createTemporaryHologram(Player player, Player player2, String text) {
        if(!Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) return;
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

    public static void updateAttackTimer(Player player, CombatPlayer combatPlayer) {
        UUID uuid = player.getUniqueId();
        LocalDateTime attackResetTime = LocalDateTime.now().plus((long) ((1 / combatPlayer.getAttackSpeed()) * 1000),MILLIS);

        if(combatInfo.containsKey(uuid)) {
            combatInfo.get(uuid).setAttackTime(attackResetTime);
        } else {
            combatInfo.put(uuid,new AttackTimer(attackResetTime));
        }
    }

    public static void updateCombatTimers(Player... players) {
        for(Player player : players) {
            UUID uuid = player.getUniqueId();
            if(combatInfo.containsKey(uuid)) {
                combatInfo.get(uuid).setCombatTime(LocalDateTime.now());
            } else {
                combatInfo.put(uuid,new AttackTimer());
            }
        }
    }

}
