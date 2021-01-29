package com.miketheshadow.complexmmostats.item;

import org.bukkit.inventory.ItemStack;

public interface ComplexItem {

    String getDescription();

    String getName();

    String getItemNBT();

    String getGradeColor();

    ItemStack getItem();

}
