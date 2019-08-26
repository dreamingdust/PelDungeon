package com.ema.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.World;
import com.ema.game.ComponentMapperWrapper;
import com.ema.game.components.BodyComponent;
import com.ema.game.components.CombatComponent;
import com.ema.game.components.EnemyComponent;
import com.ema.game.components.PlayerComponent;

public class CombatSystem extends IteratingSystem {
    private ComponentMapperWrapper components;
    private World world;
    private Engine engine;

    public CombatSystem(Engine engine, World world) {
        super(Family.all(PlayerComponent.class).get());
        components = ComponentMapperWrapper.getInstance();
        this.world = world;
        this.engine = engine;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }

    public void updateCombat(Entity player, Entity enemy) {
        if (components.combatMapper.get(player).inCombat && components.combatMapper.get(enemy).inCombat) {
            if (components.playerMapper.get(player).strength > components.enemyMapper.get(enemy).armor) {
                components.enemyMapper.get(enemy).health -= (components.playerMapper.get(player).strength - components.enemyMapper.get(enemy).armor);
            }
            if (components.enemyMapper.get(enemy).strength > components.playerMapper.get(player).armor) {
                components.playerMapper.get(player).health -= (components.enemyMapper.get(enemy).strength - components.playerMapper.get(player).armor);
            }
        }

        components.enemyMapper.get(enemy).hitLast = true;
        System.out.println(components.playerMapper.get(player).health + " - " + components.enemyMapper.get(enemy).health);
        if (components.enemyMapper.get(enemy).health <= 0) {

            world.destroyBody(components.bodyMapper.get(enemy).body);
            this.getEngine().removeEntity(enemy);
        }
        engine.getSystem(CollisionSystem.class).updateEntityCollision();
        engine.getSystem(CollisionSystem.class).updateGlobalCollision();
    }
}