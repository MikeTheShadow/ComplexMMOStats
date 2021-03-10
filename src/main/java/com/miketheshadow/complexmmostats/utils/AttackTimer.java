package com.miketheshadow.complexmmostats.utils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class AttackTimer {

    private LocalDateTime combatTime;
    private LocalDateTime attackTime;

    public  AttackTimer() {
        combatTime = LocalDateTime.now();
        attackTime = LocalDateTime.now().minus(5, ChronoUnit.SECONDS);
    }

    public AttackTimer(LocalDateTime attackTime) {
        combatTime = LocalDateTime.now();
        this.attackTime = attackTime;
    }

    public void setAttackTime(LocalDateTime attackTime) {
        this.attackTime = attackTime;
    }

    public void setCombatTime(LocalDateTime combatTime) {
        this.combatTime = combatTime;
    }

    public LocalDateTime getAttackTime() {
        return attackTime;
    }

    public LocalDateTime getCombatTime() {
        return combatTime;
    }

    public boolean isAttackReset() {
        return attackTime.isBefore(LocalDateTime.now());
    }
}
