package com.ema.game.components;

import com.badlogic.ashley.core.Component;

public class MovementComponent implements Component {
    public boolean sleeping = false;

    private static final int DIRECTION_LEFT = 1;
    private static final int DIRECTION_RIGHT = 2;
    private static final int DIRECTION_UP = 3;
    private static final int DIRECTION_DOWN = 4;
    private static final int DIRECTION_NONE = 0;

    public int direction = DIRECTION_NONE;
}
