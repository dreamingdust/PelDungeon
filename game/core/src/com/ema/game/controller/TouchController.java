package com.ema.game.controller;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.ema.game.ComponentMapperWrapper;
import com.ema.game.components.BodyComponent;
import com.ema.game.components.CollisionComponent;
import com.ema.game.components.EnemyComponent;
import com.ema.game.components.MapObjectComponent;
import com.ema.game.components.PlayerComponent;
import com.ema.game.components.RogueComponent;
import com.ema.game.components.WarriorComponent;
import com.ema.game.systems.CombatSystem;
import com.ema.game.systems.MovementSystem;
import com.ema.game.systems.RogueSystem;
import com.ema.game.systems.WarriorSystem;

public class TouchController extends ApplicationAdapter implements InputProcessor {
    private static final int DIRECTION_LEFT = 1;
    private static final int DIRECTION_RIGHT = 2;
    private static final int DIRECTION_UP = 3;
    private static final int DIRECTION_DOWN = 4;
    private static final int DIRECTION_NONE = 0;

    int direction;

    Vector2 mousePos;

    private OrthographicCamera camera;
    private PooledEngine engine;
    private Entity player;
    private ImmutableArray<Entity> mapTiles;
    private FitViewport viewport;

    private ComponentMapperWrapper components;
    private WarriorComponent warrior;
    private RogueComponent rogue;

    ImmutableArray<Entity> enemies;

    Vector3 tp = new Vector3();
    boolean dragging;

    boolean touchedEnemy;

    public TouchController(OrthographicCamera camera, Entity player, PooledEngine engine) {
        mousePos = new Vector2(0, 0);
        this.camera = camera;
        this.engine = engine;
        this.player = player;
        mapTiles = engine.getEntitiesFor(Family.all(MapObjectComponent.class).get());

        components = ComponentMapperWrapper.getInstance();
        if (components.warriorMapper.has(player)) {
            warrior = components.warriorMapper.get(player);
        } else if (components.rogueMapper.has(player)) {
            rogue = components.rogueMapper.get(player);
        }

        enemies = engine.getEntitiesFor(Family.all(EnemyComponent.class).get());
        touchedEnemy = false;
        direction = DIRECTION_NONE;
    }

    // Although unnecessary for mobile game, the parent class is abstract and they have
    // to be overridden.
    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown (int screenX, int screenY, int pointer, int button) {
        if (button != Input.Buttons.LEFT || pointer > 0) return false;
        dragging = true;
        touchedEnemy = false;

        for (Entity enemy : enemies) {
            tp.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(tp);

            components.enemyMapper.get(enemy).hitLast = false;

            if (components.bodyMapper.get(enemy).body.getFixtureList().get(0).testPoint(tp.x, tp.y)){
                touchedEnemy = true;
                if (components.playerMapper.get(player).spellInQueue) {
                    if (components.warriorMapper.has(player)) {
                        engine.getSystem(WarriorSystem.class).spellCombat(player, enemy);
                    } else if (components.rogueMapper.has(player)) {
                        engine.getSystem(RogueSystem.class).spellCombat(player, enemy);
                    }
                } else {
                    engine.getSystem(CombatSystem.class).updateMeleeCombat(player, enemy);
                }
                components.playerMapper.get(player).round++;
            }

            if (components.bodyMapper.get(player).body.getFixtureList().get(0).testPoint(tp.x, tp.y)){
                if (components.playerMapper.get(player).spellInQueue) {
                    if (components.warriorMapper.has(player) && warrior.spell == warrior.ARMORUP) {
                        engine.getSystem(WarriorSystem.class).spellCombat(player, enemy);
                    } else if (components.rogueMapper.has(player) && rogue.spell == rogue.ENVENOM) {
                        engine.getSystem(RogueSystem.class).spellCombat(player, enemy);
                    }
                } else {
                    engine.getSystem(CombatSystem.class).updateMeleeCombat(player, enemy);
                }
                components.playerMapper.get(player).round++;
            }
        }

        if (!touchedEnemy) {
            if (screenX >= Gdx.graphics.getWidth() / 3 &&
                    screenX <= Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 3 &&
                    screenY <= Gdx.graphics.getHeight() / 3) {

                direction = DIRECTION_UP;
                if (components.collisionMapper.get(player).collision_up) {
                    direction = DIRECTION_NONE;
                }
            } else if (screenX >= Gdx.graphics.getWidth() / 3 &&
                    screenX <= Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 3 &&
                    screenY >= Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 3) {

                direction = DIRECTION_DOWN;
                if (components.collisionMapper.get(player).collision_down) {
                    direction = DIRECTION_NONE;
                }
            } else if (screenX >= Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 3 &&
                    screenY >= Gdx.graphics.getHeight() / 4 &&
                    screenY <= Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 4) {

                direction = DIRECTION_RIGHT;
                if (components.collisionMapper.get(player).collision_right) {
                    direction = DIRECTION_NONE;
                }
            } else if (screenX <= Gdx.graphics.getWidth() / 3 &&
                    screenY >= Gdx.graphics.getHeight() / 4 &&
                    screenY <= Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 4) {

                direction = DIRECTION_LEFT;
                if (components.collisionMapper.get(player).collision_left) {
                    direction = DIRECTION_NONE;
                }
            }

            engine.getSystem(MovementSystem.class).updateMovement(player);
        }


        return true;
    }

    @Override
    public boolean touchDragged (int screenX, int screenY, int pointer) {
        if (!dragging) return false;
        camera.unproject(tp.set(screenX, screenY, 0));
        float x = Gdx.input.getDeltaX();
        float y = Gdx.input.getDeltaY();

        camera.translate(-x,y);
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public boolean touchUp (int screenX, int screenY, int pointer, int button) {
        if (button != Input.Buttons.LEFT || pointer > 0) return false;
        camera.unproject(tp.set(screenX, screenY, 0));
        dragging = false;
        player.getComponent(BodyComponent.class).body.setLinearVelocity(0, 0);
        return true;
    }

    @Override
    public void resize (int width, int height) {
        // viewport must be updated for it to work properly
        viewport.update(width, height, true);
    }

    @Override
    public void dispose () {
        // disposable stuff must be disposed
    }

    public int getMovementDirection() {
        return direction;
    }

    public void setMovementDirection(int direction) {
        this.direction = direction;
    }

    public OrthographicCamera getCamera (){
        return camera;
    }
}