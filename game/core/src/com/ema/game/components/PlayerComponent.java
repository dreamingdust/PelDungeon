package com.ema.game.components;

import com.badlogic.ashley.core.Component;

public class PlayerComponent implements Component {
    public int maxHealth = 100;
    public int health = 100;
    public int strength = 3;
    public int armor = 0;
}
