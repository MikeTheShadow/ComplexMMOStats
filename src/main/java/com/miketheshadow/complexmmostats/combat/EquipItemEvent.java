package com.miketheshadow.complexmmostats.combat;

import com.miketheshadow.complexmmostats.utils.TimedItem;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

@Deprecated
public class EquipItemEvent implements Listener {

    public static final int MAIN_HAND = 0;
    public static final int OFF_HAND = 40;

    public static HashMap<UUID, TimedItem> equipCoolDown = new HashMap<>();

    /*
     * Handle weapon equip first
     */

    private static final int[] ARMOR_SLOTS = new int[] {36,37,38,39};

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        System.out.println(event.getSlot());

        PlayerInventory playerInventory = event.getWhoClicked().getInventory();
        ItemStack mainHand = playerInventory.getItem(0);
        ItemStack offhand = playerInventory.getItemInOffHand();

        TimedItem oldItems = equipCoolDown.get(event.getWhoClicked().getUniqueId());

        InventoryAction action = event.getAction();

        if(action == InventoryAction.MOVE_TO_OTHER_INVENTORY
                || action == InventoryAction.PICKUP_ALL
                || action == InventoryAction.PICKUP_HALF
                || action == InventoryAction.PICKUP_ONE
                || action == InventoryAction.PICKUP_SOME) {
            doAlert((Player) event.getWhoClicked(),event);
        }
        if(action == InventoryAction.HOTBAR_SWAP) {
            if(mainHand != oldItems.mainHand || offhand != oldItems.offhand || isSameArmor(oldItems.armor,playerInventory.getArmorContents())) {
                event.getWhoClicked().sendMessage(ChatColor.RED + "Your gear slots are locked!");
                event.setResult(Event.Result.DENY);
                event.setCancelled(true);
            }
        }
        if(action == InventoryAction.SWAP_WITH_CURSOR) {
            doAlert((Player) event.getWhoClicked(),event);
        }
    }

    public boolean isSameArmor(ItemStack[] oldArmor,ItemStack[] currentArmor) {
        AtomicBoolean isSame = new AtomicBoolean(true);
        Arrays.stream(oldArmor).forEach(i -> {
            if(Arrays.stream(currentArmor).noneMatch(x -> x == i)) isSame.set(false);
        });
        return isSame.get();
    }

    @EventHandler
    public void onSwapItemsEvent(PlayerSwapHandItemsEvent event) {
        event.setCancelled(true);
    }

    public static void doAlert(Player player, InventoryClickEvent event) {
        //if(!LocalDateTime.now().isAfter(item.time)) return;
        if(event.getSlot() == OFF_HAND || event.getSlot() == MAIN_HAND || Arrays.stream(ARMOR_SLOTS).anyMatch(i -> i == event.getSlot())) {
            player.sendMessage(ChatColor.RED + "Your gear slots are locked!");
            event.setResult(Event.Result.DENY);
            event.setCancelled(true);
        }
    }
}
