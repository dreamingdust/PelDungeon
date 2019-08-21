package com.ema.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.ema.game.components.BodyComponent;
import com.ema.game.components.PlayerComponent;
import com.ema.game.controller.TouchController;

public class MovementSystem extends IteratingSystem {
    TouchController controller;

    private static final int DIRECTION_LEFT = 1;
    private static final int DIRECTION_RIGHT = 2;
    private static final int DIRECTION_UP = 3;
    private static final int DIRECTION_DOWN = 4;
    private static final int DIRECTION_NONE = 0;

    private int direction;

    public MovementSystem(TouchController controller) {
        super(Family.all(PlayerComponent.class).get());
        this.controller = controller;
        direction = DIRECTION_NONE;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        direction = controller.getMovementDirection();

        if (direction == DIRECTION_LEFT) {
            entity.getComponent(BodyComponent.class).body.setTransform(entity.getComponent(BodyComponent.class).body.getPosition().x - 0.32f, entity.getComponent(BodyComponent.class).body.getPosition().y, 0);
        } else if (direction == DIRECTION_RIGHT) {
            entity.getComponent(BodyComponent.class).body.setTransform(entity.getComponent(BodyComponent.class).body.getPosition().x + 0.32f, entity.getComponent(BodyComponent.class).body.getPosition().y, 0);
        } else if (direction == DIRECTION_DOWN) {
            entity.getComponent(BodyComponent.class).body.setTransform(entity.getComponent(BodyComponent.class).body.getPosition().x, entity.getComponent(BodyComponent.class).body.getPosition().y - 0.32f, 0);
        } else if (direction == DIRECTION_UP) {
            entity.getComponent(BodyComponent.class).body.setTransform(entity.getComponent(BodyComponent.class).body.getPosition().x, entity.getComponent(BodyComponent.class).body.getPosition().y + 0.32f, 0);
        }

        controller.setMovementDirection(DIRECTION_NONE);
    }

}
