package com.ema.game.components;

import com.badlogic.ashley.core.Component;

public class RogueComponent implements Component {
    public final int NO_SPELL = 0;
    public final int ENVENOM = 1;
    public final int STAB = 2;
    public final int DOUBLE_STRIKE = 3;
    public final int VANISH = 4;

    public final int envenomCD = 2;
    public final int stabCD = 3;
    public final int doubleStrikeCD = 6;
    public final int vanishCD = 5   ;

    public int envenomRemainingCD = 0;
    public int stabRemainingCD = 0;
    public int doubleStrikeRemainingCD = 0;
    public int vanishRemainingCD = 0;

    public int spell = 0;

    public int envenomValue;
    public int stabValue;
    public int doubleStrikeValue;
    public int vanishValue;

    public int envenomDuration = 1;
    public int vanishDuration = 3;
}
