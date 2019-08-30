package com.ema.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.ema.game.ComponentMapperWrapper;
import com.ema.game.components.BodyComponent;
import com.ema.game.components.CollisionComponent;
import com.ema.game.components.EnemyComponent;
import com.ema.game.components.PlayerComponent;
import com.ema.game.controller.TouchController;

import java.util.Random;

public class MovementSystem extends IteratingSystem {
    private static final int DIRECTION_LEFT = 1;
    private static final int DIRECTION_RIGHT = 2;
    private static final int DIRECTION_UP = 3;
    private static final int DIRECTION_DOWN = 4;
    private static final int DIRECTION_NONE = 0;

    TouchController controller;
    PooledEngine engine;
    ImmutableArray<Entity> enemies;

    public boolean moveTaken;
    Random rand;


    private ComponentMapper<BodyComponent> bodyMapper;
    private ComponentMapper<CollisionComponent> collisionMapper;

    private ComponentMapperWrapper components;

    private int direction;

    public MovementSystem(TouchController controller, PooledEngine engine) {
        super(Family.all(PlayerComponent.class).get());
        this.controller = controller;
        this.engine = engine;

        bodyMapper = ComponentMapper.getFor(BodyComponent.class);
        collisionMapper = ComponentMapper.getFor(CollisionComponent.class);
        enemies = engine.getEntitiesFor(Family.all(EnemyComponent.class).get());
        components = ComponentMapperWrapper.getInstance();

        rand = new Random(System.currentTimeMillis());
        direction = DIRECTION_NONE;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }

    public void updateMovement(Entity entity) {
        direction = controller.getMovementDirection();

        if (direction != DIRECTION_NONE) {
            if (direction == DIRECTION_LEFT) {
                bodyMapper.get(entity).body.setTransform(bodyMapper.get(entity).body.getPosition().x - 0.32f, bodyMapper.get(entity).body.getPosition().y, 0);
                components.textureMapper.get(entity).flip = true;
            } else if (direction == DIRECTION_RIGHT) {
                bodyMapper.get(entity).body.setTransform(bodyMapper.get(entity).body.getPosition().x + 0.32f, bodyMapper.get(entity).body.getPosition().y, 0);
                components.textureMapper.get(entity).flip = false;
            } else if (direction == DIRECTION_DOWN) {
                bodyMapper.get(entity).body.setTransform(bodyMapper.get(entity).body.getPosition().x, bodyMapper.get(entity).body.getPosition().y - 0.32f, 0);
            } else if (direction == DIRECTION_UP) {
                bodyMapper.get(entity).body.setTransform(bodyMapper.get(entity).body.getPosition().x, bodyMapper.get(entity).body.getPosition().y + 0.32f, 0);
            }
            controller.setMovementDirection(DIRECTION_NONE);
            moveTaken = true;
            engine.getSystem(CollisionSystem.class).updateObjectCollision();
            engine.getSystem(CollisionSystem.class).updateEntityCollision();
            engine.getSystem(CollisionSystem.class).updateGlobalCollision();
        } else {
            moveTaken = false;
        }


        if (moveTaken) {
            int enemy_direction;
            int num;

            for (Entity enemy : enemies) {
                num = rand.nextInt(4);
                switch (num) {
                    case 1:
                        enemy_direction = DIRECTION_LEFT;
                        break;
                    case 2:
                        enemy_direction = DIRECTION_RIGHT;
                        break;
                    case 3:
                        enemy_direction = DIRECTION_UP;
                        break;
                    case 4:
                        enemy_direction = DIRECTION_DOWN;
                        break;
                    default:
                        enemy_direction = DIRECTION_NONE;
                }

                if (enemy_direction == DIRECTION_LEFT && !collisionMapper.get(enemy).collision_left) {
                    bodyMapper.get(enemy).body.setTransform(bodyMapper.get(enemy).body.getPosition().x - 0.32f, bodyMapper.get(enemy).body.getPosition().y, 0);
                    components.textureMapper.get(enemy).flip = false;

                } else if (enemy_direction == DIRECTION_RIGHT && !collisionMapper.get(enemy).collision_right) {
                    bodyMapper.get(enemy).body.setTransform(bodyMapper.get(enemy).body.getPosition().x + 0.32f, bodyMapper.get(enemy).body.getPosition().y, 0);
                    components.textureMapper.get(enemy).flip = true;

                } else if (enemy_direction == DIRECTION_DOWN && !collisionMapper.get(enemy).collision_down) {
                    bodyMapper.get(enemy).body.setTransform(bodyMapper.get(enemy).body.getPosition().x, bodyMapper.get(enemy).body.getPosition().y - 0.32f, 0);
                } else if (enemy_direction == DIRECTION_UP && !collisionMapper.get(enemy).collision_up) {
                    bodyMapper.get(enemy).body.setTransform(bodyMapper.get(enemy).body.getPosition().x, bodyMapper.get(enemy).body.getPosition().y + 0.32f, 0);
                }

            }
            engine.getSystem(CollisionSystem.class).updateEntityCollision();
            engine.getSystem(CollisionSystem.class).updateGlobalCollision();
        }
    }
}
