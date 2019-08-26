package com.ema.game.components;

import com.badlogic.ashley.core.Component;

public class CollisionComponent implements Component {
    public boolean object_collision_left;
    public boolean object_collision_right;
    public boolean object_collision_up;
    public boolean object_collision_down;


    public boolean enemy_collision_left;
    public boolean enemy_collision_right;
    public boolean enemy_collision_up;
    public boolean enemy_collision_down;

    public boolean collision_left;
    public boolean collision_right;
    public boolean collision_up;
    public boolean collision_down;

}
