package com.ema.game.components;

import com.badlogic.ashley.core.Component;

public class PlayerComponent implements Component {
    public final int NO_CLASS = 0;
    public final int WARRIOR_CLASS = 1;
    public final int ROGUE_CLASS = 2;

    public int playerClass = 0;

    public int maxHealth = 50;
    public int health = 50;
    public int strength = 5;
    public int armor = 1;

    public int level = 1;
    public int xp = 0;
    public int xpForLevel = 20 * level;

    public boolean spellInQueue = false;

    // The player may have only one of each types of spell buffs at a time.
    public int buffValue = 0;
    public int armorBuffDuration = 0;
    public int strengthBuffDuration = 0;
    public boolean hasArmorBuff = false;
    public boolean hasStrengthBuff = false;

    public int round = 0;
    public boolean nearExit = false;
    public boolean onExit = false;
}