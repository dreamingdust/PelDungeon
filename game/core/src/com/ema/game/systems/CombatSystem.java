package com.ema.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.World;
import com.ema.game.ComponentMapperWrapper;
import com.ema.game.components.PlayerComponent;

public class CombatSystem extends IteratingSystem {

    // TODO: inCombat state is never set back to false?

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

    public void updateMeleeCombat(Entity player, Entity enemy) {
        if (components.combatMapper.get(player).inCombat && components.combatMapper.get(enemy).inCombat) {

            if (components.playerMapper.get(player).strength > components.enemyMapper.get(enemy).armor) {
                components.enemyMapper.get(enemy).health -= (components.playerMapper.get(player).strength - components.enemyMapper.get(enemy).armor);
                if (components.enemyMapper.get(enemy).hasDebuff) {
                }
            }

            // The DoT ignores any armor
            components.enemyMapper.get(enemy).health -= components.enemyMapper.get(enemy).dotValue;

            enemyAttack(player, enemy);
        }

        checkEnemy(player, enemy);

        engine.getSystem(CollisionSystem.class).updateEntityCollision();
        engine.getSystem(CollisionSystem.class).updateGlobalCollision();
    }

    public void updateSpellCombat(Entity player, Entity enemy, int spellValue, int duration, boolean isDebuff, boolean isStun) {
        if (components.combatMapper.get(player).inCombat && components.combatMapper.get(enemy).inCombat) {
            System.out.println("Why am I here?");
            if (isDebuff) {
                components.enemyMapper.get(enemy).hasDebuff = true;
                components.enemyMapper.get(enemy).dotValue = spellValue;
                components.enemyMapper.get(enemy).dotDuration = duration;
            } else {
                if (spellValue > components.enemyMapper.get(enemy).armor) {
                    components.enemyMapper.get(enemy).health -= (spellValue - components.enemyMapper.get(enemy).armor);
                }
                components.enemyMapper.get(enemy).health -= components.enemyMapper.get(enemy).dotValue;
            }

            if (isStun) {
                components.enemyMapper.get(enemy).isStunned = true;
                components.enemyMapper.get(enemy).stunDuration = duration;
            }

            enemyAttack(player, enemy);

        }

        checkEnemy(player, enemy);

    }

    private void enemyAttack(Entity player, Entity enemy) {
        if ((   components.enemyMapper.get(enemy).strength > components.playerMapper.get(player).armor)
                && !components.enemyMapper.get(enemy).isStunned) {
            components.playerMapper.get(player).health -= (components.enemyMapper.get(enemy).strength - components.playerMapper.get(player).armor);
        } else if (components.enemyMapper.get(enemy).isStunned) {
            components.enemyMapper.get(enemy).stunDuration--;
            if (components.enemyMapper.get(enemy).stunDuration == 0) {
                components.enemyMapper.get(enemy).isStunned = false;
            }
        }

        if (components.playerMapper.get(player).hasArmorBuff) {
            components.playerMapper.get(player).armorBuffDuration--;
            if (components.playerMapper.get(player).armorBuffDuration == 0) {
                components.playerMapper.get(player).hasArmorBuff = false;
            }
        }
        if (components.playerMapper.get(player).hasStrengthBuff) {
            components.playerMapper.get(player).strengthBuffDuration--;
            if (components.playerMapper.get(player).strengthBuffDuration == 0) {
                components.playerMapper.get(player).hasStrengthBuff = false;
            }
        }

        if (components.warriorMapper.has(player)) {
            engine.getSystem(WarriorSystem.class).decrementCooldowns();
        } else if (components.rogueMapper.has(player)) {
            engine.getSystem(RogueSystem.class).decrementCooldowns();
        }
    }

    private void checkEnemy(Entity player, Entity enemy) {
        components.enemyMapper.get(enemy).hitLast = true;
        if (components.enemyMapper.get(enemy).health <= 0) {
            components.playerMapper.get(player).xp += components.enemyMapper.get(enemy).xpOnKill;

            world.destroyBody(components.bodyMapper.get(enemy).body);
            this.getEngine().removeEntity(enemy);
            components.updateComponents();
        }

        if (components.playerMapper.get(player).xp >= components.playerMapper.get(player).xpForLevel) {
            components.playerMapper.get(player).level++;
            components.playerMapper.get(player).xp -= components.playerMapper.get(player).xpForLevel;
            components.playerMapper.get(player).xpForLevel = 20 * components.playerMapper.get(player).level;
            if (components.warriorMapper.has(player)) {
                engine.getSystem(WarriorSystem.class).updateWarrior();
            } else if (components.rogueMapper.has(player)) {
                engine.getSystem(RogueSystem.class).updateRogue();
            }
        }
    }
}