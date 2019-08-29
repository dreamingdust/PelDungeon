package com.ema.game.components;

import com.badlogic.ashley.core.Component;

public class EnemyComponent implements Component {
    public int maxHealth = 30;
    public int health = 30;
    public int strength = 4;
    public int armor = 0;

    public int level = 1;
    public int xpOnKill = level + (int)Math.floor(level * 2f);

    public boolean hitLast = false;
    public boolean isStunned = false;
    public boolean hasDebuff = false;

    // DoT - Damage over Time
    public int dotValue = 0;
    public int dotDuration = 0;
    public int stunDuration = 0;
}
