package com.ema.game.components;

import com.badlogic.ashley.core.Component;

public class EnemyComponent implements Component {
    public int maxHealth = 20;
    public int health = 20;
    public int strength = 2;
    public int armor = 1;

    public boolean hitLast = false;
}
