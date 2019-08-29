package com.ema.game.components;

import com.badlogic.ashley.core.Component;

public class WarriorComponent implements Component {
    public boolean useBash = false;
    public boolean useRend = false;
    public boolean useExecute = false;
    public boolean useArmorUp = false;

    public final int NO_SPELL = 0;
    public final int BASH = 1;
    public final int REND = 2;
    public final int EXECUTE = 3;
    public final int ARMORUP = 4;

    public int spell = 0;

    public int bashValue;
    public int rendValue;
    public int executeValue;
    public int armorUpValue;

    public int bashDuration = 1;
    public int rendDuration = 3;
}
