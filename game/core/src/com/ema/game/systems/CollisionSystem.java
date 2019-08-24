package com.ema.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.physics.box2d.Body;
import com.ema.game.components.BodyComponent;
import com.ema.game.components.CollisionComponent;
import com.ema.game.components.MapObjectComponent;
import com.ema.game.components.PlayerComponent;
import com.ema.game.components.TypeComponent;

import sun.util.locale.StringTokenIterator;

public class CollisionSystem extends IteratingSystem {
    private ComponentMapper<BodyComponent> bodyMapper;
    private ComponentMapper<CollisionComponent> collisionMapper;
    private ComponentMapper<TypeComponent> typeMapper;
    private ImmutableArray<Entity> objects;
    private ImmutableArray<Entity> entities;

    public CollisionSystem(Engine engine) {
        super(Family.all(CollisionComponent.class).get());
        bodyMapper = ComponentMapper.getFor(BodyComponent.class);
        collisionMapper = ComponentMapper.getFor(CollisionComponent.class);
        typeMapper = ComponentMapper.getFor(TypeComponent.class);
        objects = engine.getEntitiesFor(Family.all(MapObjectComponent.class).get());
        entities = engine.getEntitiesFor(Family.all(CollisionComponent.class).get());
        this.updateCollision();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
    }

    public void updateCollision() {
        for (Entity entity : entities) {
            collisionMapper.get(entity).collision_up = false;
            collisionMapper.get(entity).collision_down = false;
            collisionMapper.get(entity).collision_right = false;
            collisionMapper.get(entity).collision_left = false;

            for (Entity object : objects) {
                if (bodyMapper.get(object).body.getFixtureList().get(0).testPoint(bodyMapper.get(entity).body.getPosition().x, bodyMapper.get(entity).body.getPosition().y + 0.32f)) {
                    collisionMapper.get(entity).collision_up = true;
                }
                if (bodyMapper.get(object).body.getFixtureList().get(0).testPoint(bodyMapper.get(entity).body.getPosition().x, bodyMapper.get(entity).body.getPosition().y - 0.32f)) {
                    collisionMapper.get(entity).collision_down = true;
                }
                if (bodyMapper.get(object).body.getFixtureList().get(0).testPoint(bodyMapper.get(entity).body.getPosition().x + 0.32f, bodyMapper.get(entity).body.getPosition().y)) {
                    collisionMapper.get(entity).collision_right = true;
                }
                if (bodyMapper.get(object).body.getFixtureList().get(0).testPoint(bodyMapper.get(entity).body.getPosition().x - 0.32f, bodyMapper.get(entity).body.getPosition().y)) {
                    collisionMapper.get(entity).collision_left = true;
                }
            }
        }
    }

    public void updateObjects() {
        objects = this.getEngine().getEntitiesFor(Family.all(MapObjectComponent.class).get());
    }
}