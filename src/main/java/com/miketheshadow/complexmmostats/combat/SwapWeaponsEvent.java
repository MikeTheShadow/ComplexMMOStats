package com.miketheshadow.complexmmostats.combat;

import com.miketheshadow.complexmmostats.ComplexMMOStats;
import com.miketheshadow.complexmmostats.utils.CombatPlayer;
import com.miketheshadow.complexmmostats.utils.ItemChecker;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

public class SwapWeaponsEvent implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerSwapWeaponEvent(PlayerItemHeldEvent event) {
        if(event.isCancelled()) return;
        ItemStack stack = event.getPlayer().getInventory().getItem(event.getNewSlot());
        if(stack == null) {
            stack = new ItemStack(Material.AIR);
        }
        if(ItemChecker.isValidComplexMMOItem(stack) && !NBTItem.convertItemtoNBT(CombatPlayer.getPlayer(event.getPlayer()).getCurrentMainHand()).toString().equals(NBTItem.convertItemtoNBT(stack).toString()))
            CombatPlayer.getPlayer(event.getPlayer()).update(event.getPlayer(),stack);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerSwapArmorWeaponEvent(InventoryClickEvent event) {
        if(event.isCancelled()) return;

        updatePlayerLater((Player)event.getWhoClicked());
    }

    private void updatePlayerLater(Player player) {
        (new BukkitRunnable() {
            @Override
            public void run() {
                Player attemptRefresh = Bukkit.getPlayer(player.getUniqueId());
                CombatPlayer.getPlayer(player).update(attemptRefresh,null);
            }
        }).runTaskLater(ComplexMMOStats.INSTANCE,1);
    }
}
