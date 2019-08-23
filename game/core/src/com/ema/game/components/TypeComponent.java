package com.ema.game.components;

import com.badlogic.ashley.core.Component;

public class TypeComponent implements Component {
    public static final int PLAYER = 0;
    public static final int ENEMY = 1;
    public static final int OBJECT = 3;
    public static final int GROUND = 4;
    public static final int OTHER = 5;

    public int type = OTHER;
}
