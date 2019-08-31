package com.ema.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.ema.game.ComponentMapperWrapper;
import com.ema.game.components.CollisionComponent;
import com.ema.game.components.EnemyComponent;
import com.ema.game.components.MapExitComponent;
import com.ema.game.components.MapObjectComponent;
import com.ema.game.components.MovementComponent;

public class CollisionSystem extends EntitySystem {

    private ComponentMapperWrapper components;

    private ImmutableArray<Entity> objects;
    private ImmutableArray<Entity> entities;
    private ImmutableArray<Entity> enemies;
    private ImmutableArray<Entity> movers;



    public CollisionSystem(Engine engine) {
        components = ComponentMapperWrapper.getInstance();
        objects = engine.getEntitiesFor(Family.one(MapObjectComponent.class, MapExitComponent.class).get());
        entities = engine.getEntitiesFor(Family.all(CollisionComponent.class).get());
        enemies = engine.getEntitiesFor(Family.all(EnemyComponent.class).get());
        movers = engine.getEntitiesFor(Family.all(MovementComponent.class).get());
        this.updateObjectCollision();
        this.updateEntityCollision();
        this.updateGlobalCollision();
    }

    public void updateObjectCollision() {
        for (Entity entity : entities) {
            components.collisionMapper.get(entity).object_collision_up = false;
            components.collisionMapper.get(entity).object_collision_down = false;
            components.collisionMapper.get(entity).object_collision_right = false;
            components.collisionMapper.get(entity).object_collision_left = false;

            for (Entity object : objects) {

                if ((   components.bodyMapper.get(object).body.getPosition().x - components.bodyMapper.get(entity).body.getPosition().x <= 0.64f &&
                        components.bodyMapper.get(object).body.getPosition().x - components.bodyMapper.get(entity).body.getPosition().x >= -0.64f) &&
                    (   components.bodyMapper.get(object).body.getPosition().y - components.bodyMapper.get(entity).body.getPosition().y <= 0.64f &&
                        components.bodyMapper.get(object).body.getPosition().y - components.bodyMapper.get(entity).body.getPosition().y >= -0.64f)) {

                    if (components.mapExitMapper.has(object) && components.playerMapper.has(entity)) {
                        components.playerMapper.get(entity).nearExit = true;
                    } else {
                            if (components.bodyMapper.get(object).body.getFixtureList().get(0).testPoint(components.bodyMapper.get(entity).body.getPosition().x, components.bodyMapper.get(entity).body.getPosition().y + 0.32f)) {
                                components.collisionMapper.get(entity).object_collision_up = true;
                                if (components.playerMapper.has(entity)) {
                                    System.out.println("Collision up");
                                }
                            }
                            if (components.bodyMapper.get(object).body.getFixtureList().get(0).testPoint(components.bodyMapper.get(entity).body.getPosition().x, components.bodyMapper.get(entity).body.getPosition().y - 0.32f)) {
                                components.collisionMapper.get(entity).object_collision_down = true;
                                if (components.playerMapper.has(entity)) {
                                    System.out.println("Collision down");
                                }
                            }
                            if (components.bodyMapper.get(object).body.getFixtureList().get(0).testPoint(components.bodyMapper.get(entity).body.getPosition().x + 0.32f, components.bodyMapper.get(entity).body.getPosition().y)) {
                                components.collisionMapper.get(entity).object_collision_right = true;
                                if (components.playerMapper.has(entity)) {
                                    System.out.println("Collision right");
                                }
                            }
                            if (components.bodyMapper.get(object).body.getFixtureList().get(0).testPoint(components.bodyMapper.get(entity).body.getPosition().x - 0.32f, components.bodyMapper.get(entity).body.getPosition().y)) {
                                components.collisionMapper.get(entity).object_collision_left = true;
                                if (components.playerMapper.has(entity)) {
                                    System.out.println("Collision left");
                                }
                            }
                    }
                }
            }
        }
    }

    public void updateEntityCollision() {

        for (Entity entity : entities) {
            components.collisionMapper.get(entity).enemy_collision_up = false;
            components.collisionMapper.get(entity).enemy_collision_down = false;
            components.collisionMapper.get(entity).enemy_collision_right = false;
            components.collisionMapper.get(entity).enemy_collision_left = false;

            for (Entity object : movers) {
                    if (components.bodyMapper.get(object).body.getFixtureList().get(0).testPoint(components.bodyMapper.get(entity).body.getPosition().x, components.bodyMapper.get(entity).body.getPosition().y + 0.32f)) {
                        components.collisionMapper.get(entity).enemy_collision_up = true;
                        components.combatMapper.get(entity).inCombat = true;
                    }
                    if (components.bodyMapper.get(object).body.getFixtureList().get(0).testPoint(components.bodyMapper.get(entity).body.getPosition().x, components.bodyMapper.get(entity).body.getPosition().y - 0.32f)) {
                        components.collisionMapper.get(entity).enemy_collision_down = true;
                        components.combatMapper.get(entity).inCombat = true;
                    }
                    if (components.bodyMapper.get(object).body.getFixtureList().get(0).testPoint(components.bodyMapper.get(entity).body.getPosition().x + 0.32f, components.bodyMapper.get(entity).body.getPosition().y)) {
                        components.collisionMapper.get(entity).enemy_collision_right = true;
                        components.combatMapper.get(entity).inCombat = true;
                    }
                    if (components.bodyMapper.get(object).body.getFixtureList().get(0).testPoint(components.bodyMapper.get(entity).body.getPosition().x - 0.32f, components.bodyMapper.get(entity).body.getPosition().y)) {
                        components.collisionMapper.get(entity).enemy_collision_left = true;
                        components.combatMapper.get(entity).inCombat = true;
                    }
            }
        }
    }


    public void updateGlobalCollision() {
        for (Entity entity : entities) {
            components.collisionMapper.get(entity).collision_down = components.collisionMapper.get(entity).object_collision_down || components.collisionMapper.get(entity).enemy_collision_down;
            components.collisionMapper.get(entity).collision_up = components.collisionMapper.get(entity).object_collision_up || components.collisionMapper.get(entity).enemy_collision_up;
            components.collisionMapper.get(entity).collision_left = components.collisionMapper.get(entity).object_collision_left || components.collisionMapper.get(entity).enemy_collision_left;
            components.collisionMapper.get(entity).collision_right = components.collisionMapper.get(entity).object_collision_right || components.collisionMapper.get(entity).enemy_collision_right;
        }
    }

    public void updateObjects() {
        objects = this.getEngine().getEntitiesFor(Family.all(MapObjectComponent.class).get());
    }
}