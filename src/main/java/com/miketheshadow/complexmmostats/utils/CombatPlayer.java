package com.miketheshadow.complexmmostats.utils;

import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

import static com.miketheshadow.complexmmostats.utils.NBTData.*;

public class CombatPlayer {

    public static HashMap<UUID,CombatPlayer> players = new HashMap<>();

    private List<ItemStack> gear;

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


    //TODO get this rather than having it be a fixed value
    private final double baseHealth = 5000;

    private float intel;

    public double bonusHealth = 0;

    private ItemStack currentMainHand;

    private final HashMap<Stat,Integer> statMap = buildEmptyMap();

    public CombatPlayer(Player player,ItemStack forceMainHand) {
        update(player,forceMainHand);
    }

    public void update(Player player,ItemStack forceMainHand) {
        List<ItemStack> currentGear = new ArrayList<>();
        currentGear.add(player.getInventory().getItemInMainHand());
        currentGear.add(player.getInventory().getItemInOffHand());
        currentGear.addAll(Arrays.asList(player.getInventory().getArmorContents()));
        if(currentGear == gear) return;
        gear = currentGear;

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
        calculateOffensiveStats(player,forceMainHand);


        //calculate final stats
        double totalHealth = baseHealth + bonusHealth;

        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(totalHealth);
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(2.4 + attackSpeed);
        //player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(2.4 + ( 1 / attackSpeed));
        player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(1);
        player.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(0);
        player.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS).setBaseValue(0);
        player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(0);
        if(player.getHealth() > player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue()) {
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


    private void calculateOffensiveStats(Player player,ItemStack forceMainHand) {

        attackSpeed = 1;

        PlayerInventory inventory = player.getInventory();
        ItemStack mainHand = forceMainHand == null ? inventory.getItemInMainHand() : forceMainHand;
        ItemStack offHand = inventory.getItemInOffHand();

        //check if the main hand is a weapon (shields do not count)
        if(ItemChecker.isValidComplexMMOItem(mainHand) && !ItemChecker.isShield(mainHand) && !ItemChecker.isArmor(mainHand)) {
            addMainHandStats(mainHand);
        } else if(ItemChecker.isValidComplexMMOItem(this.currentMainHand)) {
            //if the main hand is not a valid weapon then the player is considered unarmed.
            addMainHandStats(currentMainHand);
        }
        //Make sure that offhand is a valid weapon

        if(!ItemChecker.isTwoHandedWeapon(mainHand) && ItemChecker.isValidComplexMMOItem(offHand)) {

            if (ItemChecker.isShield(offHand))  {
                addStatsToSelf(offHand);
            } else {
                addStatsToSelf(offHand);
                //add the duel wield bonus here
                attackSpeed += .2;
            }
        }


        Arrays.stream(inventory.getArmorContents()).forEach(armor -> {
            if(ItemChecker.isValidComplexMMOItem(armor)) addStatsToSelf(armor);
        });

    }

    private void addMainHandStats(ItemStack stack) {
        this.currentMainHand = stack;
        PersistentDataContainer container = stack.getItemMeta().getPersistentDataContainer();
        //NBTContainer container = NBTItem.convertItemtoNBT(stack);
        //NBTCompound weaponContainer = container.getCompound("tag");
        this.damage = container.get(NBTData.ATTACK_DAMAGE, PersistentDataType.INTEGER);
        //TODO ENABLE -> this.attackSpeed = weaponContainer.getFloat("attack_speed");
        addStats(container);
    }

    private void addStatsToSelf(ItemStack stack) {
        addStats(stack.getItemMeta().getPersistentDataContainer());
    }

    private void addStats(PersistentDataContainer container) {

        if(container.has(STRENGTH,PersistentDataType.INTEGER)) {
            int amount = container.get(STRENGTH,PersistentDataType.INTEGER);
            this.damage += (amount * 0.4);
            this.parryRate += (amount * 0.03);
            statMap.put(Stat.STRENGTH,statMap.get(Stat.STRENGTH) + amount);
        }

        if(container.has(STAMINA,PersistentDataType.INTEGER)) {
            int amount = container.get(STAMINA,PersistentDataType.INTEGER);
            this.bonusHealth += amount * 10;
            this.blockRate += (amount * 0.03);
            statMap.put(Stat.STAMINA,statMap.get(Stat.STAMINA) + amount);
        }
        if(container.has(AGILITY,PersistentDataType.INTEGER)) {
            int amount = container.get(AGILITY,PersistentDataType.INTEGER);
            this.criticalRate += amount * 0.05;
            this.criticalDamage += amount * 0.05;
            statMap.put(Stat.AGILITY,statMap.get(Stat.AGILITY) + amount);
        }
        if(container.has(INTELLIGENCE,PersistentDataType.INTEGER)) {
            int amount = container.get(INTELLIGENCE,PersistentDataType.INTEGER);
            this.intel += amount;
            statMap.put(Stat.INTELLIGENCE,statMap.get(Stat.INTELLIGENCE) + amount);
        }

        /*
        if(compound.hasKey(Stat.STRENGTH.name())) {
            int amount = compound.getInteger(Stat.STRENGTH.name());
            this.damage += (amount * 0.4);
            this.parryRate += (amount * 0.03);
            statMap.put(Stat.STRENGTH,statMap.get(Stat.STRENGTH) + amount);
        }

        if(compound.hasKey(Stat.STAMINA.name())) {
            int amount = compound.getInteger(Stat.STAMINA.name());
            this.bonusHealth += amount * 10;
            this.blockRate += (amount * 0.03);
            statMap.put(Stat.STAMINA,statMap.get(Stat.STAMINA) + amount);
        }
        if(compound.hasKey(Stat.AGILITY.name())) {
            int amount = compound.getInteger(Stat.AGILITY.name());
            this.criticalRate += amount * 0.05;
            this.criticalDamage += amount * 0.05;
            statMap.put(Stat.AGILITY,statMap.get(Stat.AGILITY) + amount);
        }
        if(compound.hasKey(Stat.INTELLIGENCE.name())) {
            int amount = compound.getInteger(Stat.INTELLIGENCE.name());
            this.intel += amount;
            statMap.put(Stat.INTELLIGENCE,statMap.get(Stat.INTELLIGENCE) + amount);
        //for shields or armor

        if(compound.hasKey("defense")) {
            this.defense += compound.getInteger("defense");
        }
        }
         */

        //for shields or armor
        if(container.has(DEFENSE,PersistentDataType.INTEGER)) {
            this.defense += container.get(DEFENSE,PersistentDataType.INTEGER);
        }

    }

    public ItemStack getCurrentMainHand() {
        return this.currentMainHand;
    }

    private HashMap<Stat,Integer> buildEmptyMap() {
        HashMap<Stat,Integer> quickMap = new HashMap<>();
        for (Stat stat : Stat.values()) {
            quickMap.put(stat,0);
        }
        return quickMap;
    }

    public static CombatPlayer getPlayer(Player player) {
        if(!players.containsKey(player.getUniqueId())) throw new RuntimeException(ChatColor.RED + "Error! Player " + player.getName() + " not in combat DB!");
        return players.get(player.getUniqueId());
    }

    public float getBlockRate() {
        return blockRate;
    }

    public float getParryRate() {
        return parryRate;
    }

    public List<ItemStack> getGear() {
        return gear;
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
        if(reducedDefence < 0) reducedDefence = 0;
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
