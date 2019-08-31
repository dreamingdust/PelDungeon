package com.ema.game.components;

import com.badlogic.ashley.core.Component;

public class ItemComponent implements Component {
    public final int UNKNOWN_ITEM = 0;
    public final int ARMOR_ITEM = 1;
    public final int WEAPON_ITEM = 2;
    public final int POTION_ITEM = 3;
    public final int SCROLL_ITEM = 4;

    public int item = UNKNOWN_ITEM;

    public int id = 0;
    public String name = "";
    public int value = 0;
    public int bonus_hp = 0;
    public int armor = 0;
    public int level = 0;
}
