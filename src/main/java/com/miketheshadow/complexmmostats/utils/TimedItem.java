package com.miketheshadow.complexmmostats.utils;

import org.bukkit.inventory.ItemStack;

import java.time.LocalDateTime;

public class TimedItem {

    public LocalDateTime time;
    public ItemStack mainHand;
    public ItemStack offhand;
    public ItemStack[] armor;

    public TimedItem(LocalDateTime time, ItemStack stack,ItemStack offhand,ItemStack[] armor) {
        this.time = time;
        this.mainHand = stack;
        this.offhand = offhand;
        this.armor = armor;
    }
}
