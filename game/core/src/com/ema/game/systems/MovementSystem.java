package com.ema.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.physics.box2d.World;
import com.ema.game.ComponentMapperWrapper;
import com.ema.game.components.BodyComponent;
import com.ema.game.components.CollisionComponent;
import com.ema.game.components.EnemyComponent;
import com.ema.game.components.ItemComponent;
import com.ema.game.components.MapExitComponent;
import com.ema.game.components.PlayerComponent;
import com.ema.game.controller.TouchController;

import java.util.Random;

public class MovementSystem extends EntitySystem {
    private static final int DIRECTION_LEFT = 1;
    private static final int DIRECTION_RIGHT = 2;
    private static final int DIRECTION_UP = 3;
    private static final int DIRECTION_DOWN = 4;
    private static final int DIRECTION_NONE = 0;

    TouchController controller;
    PooledEngine engine;
    World world;
    ImmutableArray<Entity> enemies;
    ImmutableArray<Entity> exit;
    ImmutableArray<Entity> items;

    public boolean moveTaken;
    Random rand;

    private PlayerComponent player;

    private ComponentMapper<BodyComponent> bodyMapper;
    private ComponentMapper<CollisionComponent> collisionMapper;

    private ComponentMapperWrapper components;

    private int direction;

    public MovementSystem(TouchController controller, PooledEngine engine, World world) {
        this.controller = controller;
        this.engine = engine;
        this.world = world;

        bodyMapper = ComponentMapper.getFor(BodyComponent.class);
        collisionMapper = ComponentMapper.getFor(CollisionComponent.class);

        enemies = engine.getEntitiesFor(Family.all(EnemyComponent.class).get());
        items = engine.getEntitiesFor(Family.all(ItemComponent.class).get());
        exit = engine.getEntitiesFor(Family.all(MapExitComponent.class).get());
        components = ComponentMapperWrapper.getInstance();

        rand = new Random(System.currentTimeMillis());
        direction = DIRECTION_NONE;
    }

    public void updateMovement(Entity entity) {
        player = components.playerMapper.get(engine.getEntitiesFor(Family.all(PlayerComponent.class).get()).first());
        items = engine.getEntitiesFor(Family.all(ItemComponent.class).get());

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

        if (player.nearExit) {
            if (bodyMapper.get(exit.get(0)).body.getFixtureList().get(0).testPoint(bodyMapper.get(entity).body.getPosition())) {
                player.onExit = true;
            }
        }

        if (player.nearItem) {
            System.out.println("FLAG");
            for (Entity item : items) {
                if (bodyMapper.get(item).body.getFixtureList().get(0).testPoint(bodyMapper.get(entity).body.getPosition())) {
                    if (components.weaponMapper.has(item)) {
                        System.out.println(components.weaponMapper.get(item).name);
                        player.strength += components.weaponMapper.get(item).base_strength;
                    } else if (components.armorMapper.has(item)) {
                        System.out.println(components.armorMapper.get(item).name);

                        player.armor += components.armorMapper.get(item).armor;
                        player.maxHealth += components.armorMapper.get(item).bonus_hp;
                    } else if (components.potionMapper.has(item)) {
                        System.out.println(components.potionMapper.get(item).name);

                        player.health += components.potionMapper.get(item).health;
                        if (player.health + components.potionMapper.get(item).health > player.maxHealth) {
                            player.health = player.maxHealth;
                        } else {
                            player.health += components.potionMapper.get(item).health;
                        }
                        player.strength += components.potionMapper.get(item).strength;
                        player.armor += components.potionMapper.get(item).armor;
                    }
                    world.destroyBody(components.bodyMapper.get(item).body);
                    engine.removeEntity(item);
                    player.nearItem = false;
                }
            }
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
