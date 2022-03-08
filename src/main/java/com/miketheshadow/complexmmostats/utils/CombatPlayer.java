package com.miketheshadow.complexmmostats.utils;

import com.miketheshadow.mmotextapi.text.ItemStat;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import static com.miketheshadow.complexmmostats.ComplexMMOStats.INSTANCE;
import static com.miketheshadow.mmotextapi.text.ItemStat.*;

public class CombatPlayer {

    public static HashMap<UUID, CombatPlayer> players = new HashMap<>();

    private float damage;
    private float attackSpeed;
    //critical stuff
    private float criticalRate = 20;
    private float criticalDamage = 50;
    private float flatBonusAD;
    private float percentBonusDamage;
    //STORE AS .00
    private float percentDamageReduction;

    //defensive stats
    private float defense;
    private float blockRate = 0.1F;
    private float parryRate;

    private HashMap<String, Float> flatStats;
    private HashMap<String, Float> percentBonusStats;

    //TODO get this rather than having it be a fixed value
    private final double baseHealth = 5000;

    private float intel;

    public double bonusHealth = 0;

    private ItemStack currentMainHand;

    private final HashMap<ItemStat, Integer> statMap = buildEmptyMap();

    public CombatPlayer(Player player, ItemStack forceMainHand) {
        update(player, forceMainHand);
    }

    public void update(Player player, ItemStack forceMainHand) {

        //reset health value
        this.bonusHealth = 0;
        damage = 0;
        //critical stuff
        criticalRate = 10;
        criticalDamage = 50;
        flatBonusAD = 0;
        percentBonusDamage = 0;
        //STORE AS .00
        percentDamageReduction = 0;

        //defensive stats
        defense = 0;
        blockRate = 0.1F;
        parryRate = 0;
        calculateOffensiveStats(player, forceMainHand);


        //calculate final stats
        double totalHealth = baseHealth + bonusHealth;

        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(totalHealth);
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(2.4 + attackSpeed);
        //player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(2.4 + ( 1 / attackSpeed));
        player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(1);
        player.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(0);
        player.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS).setBaseValue(0);
        player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(0);
        if (player.getHealth() > player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue()) {
            player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
        }
        //player.sendMessage(ChatColor.GREEN + ":" + damage);
        //calculate defense
        percentDamageReduction = defense / (defense + 7900);

    }

    /*
        public static void update(Player player,ItemStack forceMainHand) {

        if(!players.containsKey(player.getUniqueId())) throw new RuntimeException(ChatColor.RED + "Error! Player " + player.getName() + " not in combat DB!");
        players.replace(player.getUniqueId(),new CombatPlayer(player,forceMainHand,players.get(player.getUniqueId()).gear));

    }
     */


    private void calculateOffensiveStats(Player player, ItemStack forceMainHand) {

        attackSpeed = 1;

        PlayerInventory inventory = player.getInventory();
        ItemStack mainHand = forceMainHand == null ? inventory.getItemInMainHand() : forceMainHand;
        ItemStack offHand = inventory.getItemInOffHand();

        //check if the main hand is a weapon (shields do not count)
        boolean isValidWeapon = ItemChecker.isAnyWeapon(mainHand);
        boolean isTwoHanded = ItemChecker.isTwoHandedWeapon(mainHand);
        boolean isOffhandShield = ItemChecker.isShield(offHand);
        boolean isOffhandWeapon = ItemChecker.isOneHandedWeapon(offHand);

        if (isValidWeapon) {
            addMainHandStats(mainHand);
        } else if (ItemChecker.isValidComplexMMOItem(this.currentMainHand)) {
            //if the main hand is not a valid weapon then the player is considered unarmed.
            addMainHandStats(currentMainHand);
        }

        //Make sure that offhand is a valid weapon
        if (!isTwoHanded && (isOffhandShield || isOffhandWeapon)) {

            addStatsToSelf(offHand);
            if (isOffhandShield) {
                //add the duel wield bonus here
                attackSpeed += .2;
            }
        }


        Arrays.stream(inventory.getArmorContents()).forEach(armor -> {
            if (ItemChecker.isArmor(armor)) addStatsToSelf(armor);
        });

    }

    private void addMainHandStats(ItemStack stack) {
        this.currentMainHand = stack;
        PersistentDataContainer container = stack.getItemMeta().getPersistentDataContainer();
        this.damage = container.get(CMMOKeys.ATTACK_DAMAGE, PersistentDataType.INTEGER);
        //TODO ENABLE -> this.attackSpeed = weaponContainer.getFloat("attack_speed");
        addStats(container);
    }

    private void addStatsToSelf(ItemStack stack) {
        addStats(stack.getItemMeta().getPersistentDataContainer());
    }

    private void addStats(PersistentDataContainer container) {

        if (container.has(STRENGTH.getNameSpacedKey(INSTANCE), PersistentDataType.INTEGER)) {
            int amount = container.get(STRENGTH.getNameSpacedKey(INSTANCE), PersistentDataType.INTEGER);
            this.damage += (amount * 0.4);
            this.parryRate += (amount * 0.03);
            statMap.put(STRENGTH, statMap.get(STRENGTH) + amount);
        }

        if (container.has(STAMINA.getNameSpacedKey(INSTANCE), PersistentDataType.INTEGER)) {
            int amount = container.get(STAMINA.getNameSpacedKey(INSTANCE), PersistentDataType.INTEGER);
            this.bonusHealth += amount * 10;
            this.blockRate += (amount * 0.03);
            statMap.put(STAMINA, statMap.get(STAMINA) + amount);
        }
        if (container.has(AGILITY.getNameSpacedKey(INSTANCE), PersistentDataType.INTEGER)) {
            int amount = container.get(AGILITY.getNameSpacedKey(INSTANCE), PersistentDataType.INTEGER);
            this.criticalRate += amount * 0.05;
            this.criticalDamage += amount * 0.05;
            statMap.put(AGILITY, statMap.get(AGILITY) + amount);
        }
        if (container.has(INTELLIGENCE.getNameSpacedKey(INSTANCE), PersistentDataType.INTEGER)) {
            int amount = container.get(INTELLIGENCE.getNameSpacedKey(INSTANCE), PersistentDataType.INTEGER);
            this.intel += amount;
            statMap.put(INTELLIGENCE, statMap.get(INTELLIGENCE) + amount);
        }

        /*
        if(compound.hasKey(STRENGTH.name())) {
            int amount = compound.getInteger(STRENGTH.name());
            this.damage += (amount * 0.4);
            this.parryRate += (amount * 0.03);
            statMap.put(STRENGTH,statMap.get(STRENGTH) + amount);
        }

        if(compound.hasKey(STAMINA.name())) {
            int amount = compound.getInteger(STAMINA.name());
            this.bonusHealth += amount * 10;
            this.blockRate += (amount * 0.03);
            statMap.put(STAMINA,statMap.get(STAMINA) + amount);
        }
        if(compound.hasKey(AGILITY.name())) {
            int amount = compound.getInteger(AGILITY.name());
            this.criticalRate += amount * 0.05;
            this.criticalDamage += amount * 0.05;
            statMap.put(AGILITY,statMap.get(AGILITY) + amount);
        }
        if(compound.hasKey(INTELLIGENCE.name())) {
            int amount = compound.getInteger(INTELLIGENCE.name());
            this.intel += amount;
            statMap.put(INTELLIGENCE,statMap.get(INTELLIGENCE) + amount);
        //for shields or armor

        if(compound.hasKey("defense")) {
            this.defense += compound.getInteger("defense");
        }
        }
         */

        //for shields or armor
        if (container.has(DEFENSE.getNameSpacedKey(INSTANCE), PersistentDataType.INTEGER)) {
            this.defense += container.get(DEFENSE.getNameSpacedKey(INSTANCE), PersistentDataType.INTEGER);
        }

    }

    public ItemStack getCurrentMainHand() {
        return this.currentMainHand;
    }

    private HashMap<ItemStat, Integer> buildEmptyMap() {
        HashMap<ItemStat, Integer> quickMap = new HashMap<>();
        for (ItemStat stat : ItemStat.values()) {
            quickMap.put(stat, 0);
        }
        return quickMap;
    }

    public static CombatPlayer getPlayer(Player player) {
        if (!players.containsKey(player.getUniqueId()))
            throw new RuntimeException(ChatColor.RED + "Error! Player " + player.getName() + " not in combat DB!");
        return players.get(player.getUniqueId());
    }

    public float getBlockRate() {
        return blockRate;
    }

    public float getParryRate() {
        return parryRate;
    }

    public float getDamage() {
        return damage;
    }

    public float getAttackSpeed() {
        return attackSpeed;
    }

    public float getCriticalRate() {
        return criticalRate;
    }

    public float getFlatBonusAD() {
        return flatBonusAD;
    }

    public float getPercentBonusDamage() {
        return percentBonusDamage;
    }

    public float getPercentDamageReduction() {
        return percentDamageReduction;
    }

    public float getPercentDamageReduction(float armorReduction) {
        float reducedDefence = defense - armorReduction;
        if (reducedDefence < 0) reducedDefence = 0;
        return reducedDefence / (reducedDefence + 7900);
    }

    public double getBaseHealth() {
        return baseHealth;
    }

    public float getIntel() {
        return intel;
    }

    public float getCriticalDamage() {
        return criticalDamage;
    }
}
