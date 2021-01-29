package com.miketheshadow.complexmmostats.item;

import com.miketheshadow.complexmmostats.utils.Attribute;
import com.miketheshadow.complexmmostats.utils.Stat;

import java.util.HashMap;

public interface ComplexGear extends ComplexItem {

    //String getBaseDamage(); //move to weapon

    //int getAttackSpeed(); //move to weapon

    HashMap<Attribute,Integer> getSetBonus();

    HashMap<Stat,Integer> getStats();

    int getLevelRequirement();

    //
    HashMap<Integer,Integer> getCurrentLevel();

    
}
