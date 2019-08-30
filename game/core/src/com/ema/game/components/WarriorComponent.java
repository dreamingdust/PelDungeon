package com.ema.game.components;

import com.badlogic.ashley.core.Component;

public class WarriorComponent implements Component {
    public final int NO_SPELL = 0;
    public final int BASH = 1;
    public final int REND = 2;
    public final int EXECUTE = 3;
    public final int ARMORUP = 4;

    // CD -> Cooldown, aka how long the player has to wait to use the spell again
    public final int bashCD = 3;
    public final int rendCD = 1;
    public final int executeCooldown = 5;
    public final int armorUpCooldown = 2;

    public int bashRemainingCD = 0;
    public int rendRemainingCD = 0;
    public int executeRemainingCD = 0;
    public int armorUpRemainingCD = 0;

    public int bashValue;
    public int rendValue;
    public int executeValue;
    public int armorUpValue;

    public int bashDuration = 2;
    public int rendDuration = 3;
    public int armorUpDuration = 2;

    public int spell = 0;
}
