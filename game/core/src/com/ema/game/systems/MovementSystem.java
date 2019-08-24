package com.ema.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.ema.game.components.BodyComponent;
import com.ema.game.components.EnemyComponent;
import com.ema.game.components.MovementComponent;
import com.ema.game.components.PlayerComponent;
import com.ema.game.controller.TouchController;

import java.util.Random;

public class MovementSystem extends IteratingSystem {
    TouchController controller;
    PooledEngine engine;
    public boolean moveTaken;
    Random rand;

    private static final int DIRECTION_LEFT = 1;
    private static final int DIRECTION_RIGHT = 2;
    private static final int DIRECTION_UP = 3;
    private static final int DIRECTION_DOWN = 4;
    private static final int DIRECTION_NONE = 0;

    private ComponentMapper<BodyComponent> bodyMapper;

    private int direction;

    public MovementSystem(TouchController controller, PooledEngine engine) {
        super(Family.all(PlayerComponent.class).get());
        this.controller = controller;
        this.engine = engine;

        bodyMapper = ComponentMapper.getFor(BodyComponent.class);

        rand = new Random(System.currentTimeMillis());
        direction = DIRECTION_NONE;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

//        TODO: Refactor the movement system using the MovementComponent
//        TODO: Create a CollisionSystem


        direction = controller.getMovementDirection();

        if (direction != DIRECTION_NONE) {
            if (direction == DIRECTION_LEFT) {
                bodyMapper.get(entity).body.setTransform(bodyMapper.get(entity).body.getPosition().x - 0.32f, bodyMapper.get(entity).body.getPosition().y, 0);
            } else if (direction == DIRECTION_RIGHT) {
                bodyMapper.get(entity).body.setTransform(bodyMapper.get(entity).body.getPosition().x + 0.32f, bodyMapper.get(entity).body.getPosition().y, 0);
            } else if (direction == DIRECTION_DOWN) {
                bodyMapper.get(entity).body.setTransform(bodyMapper.get(entity).body.getPosition().x, bodyMapper.get(entity).body.getPosition().y - 0.32f, 0);
            } else if (direction == DIRECTION_UP) {
                bodyMapper.get(entity).body.setTransform(bodyMapper.get(entity).body.getPosition().x, bodyMapper.get(entity).body.getPosition().y + 0.32f, 0);
            }
            controller.setMovementDirection(DIRECTION_NONE);
            moveTaken = true;
            engine.getSystem(CollisionSystem.class).updateCollision(entity);
        } else {
            moveTaken = false;
        }


        if (moveTaken) {

            for (Entity enemy : engine.getEntitiesFor(Family.all(EnemyComponent.class).get())) {
                rand.nextInt();
            }
        }

    }

    public void collisionCheck(Entity entity) {

    }
}
