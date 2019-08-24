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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.ema.game.components.BodyComponent;
import com.ema.game.components.CollisionComponent;
import com.ema.game.components.MapObjectComponent;

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
    private ExtendViewport viewport;
    private ShapeRenderer shapes;

    private ComponentMapper<BodyComponent> bodyMapper;
    private ComponentMapper<CollisionComponent> collisionMapper;

    private boolean canMove;

    public TouchController(OrthographicCamera camera, Entity player, PooledEngine engine /*, Array<Body> mapTiles*/) {
        mousePos = new Vector2(0, 0);
        this.camera = camera;
        this.engine = engine;
        this.player = player;
        mapTiles = engine.getEntitiesFor(Family.all(MapObjectComponent.class).get());
        bodyMapper = ComponentMapper.getFor(BodyComponent.class);
        collisionMapper = ComponentMapper.getFor(CollisionComponent.class);
        canMove = true;
        direction = DIRECTION_NONE;

    }

    Vector3 tp = new Vector3();
    boolean dragging;
    @Override public boolean mouseMoved (int screenX, int screenY) {
        return false;
    }

    @Override public boolean touchDown (int screenX, int screenY, int pointer, int button) {
        if (button != Input.Buttons.LEFT || pointer > 0) return false;
        dragging = true;

        if (    screenX >= Gdx.graphics.getWidth()/3 &&
                screenX <= Gdx.graphics.getWidth() - Gdx.graphics.getWidth()/3 &&
                screenY <= Gdx.graphics.getHeight()/3) {

            direction = DIRECTION_UP;
            if (collisionMapper.get(player).collision_up) {
                direction = DIRECTION_NONE;
            }
        }
        else if(screenX >= Gdx.graphics.getWidth()/3 &&
                screenX <= Gdx.graphics.getWidth() - Gdx.graphics.getWidth()/3 &&
                screenY >= Gdx.graphics.getHeight() - Gdx.graphics.getHeight()/3) {

            direction = DIRECTION_DOWN;
            if (collisionMapper.get(player).collision_down) {
                direction = DIRECTION_NONE;
            }
        }
        else if(screenX >= Gdx.graphics.getWidth() - Gdx.graphics.getWidth()/3 &&
                screenY >= Gdx.graphics.getHeight()/4 &&
                screenY <= Gdx.graphics.getHeight() - Gdx.graphics.getHeight()/4 ) {

            direction = DIRECTION_RIGHT;
            if (collisionMapper.get(player).collision_right) {
                direction = DIRECTION_NONE;
            }
        }
        else if(screenX <= Gdx.graphics.getWidth()/3 &&
                screenY >= Gdx.graphics.getHeight()/4 &&
                screenY <= Gdx.graphics.getHeight() - Gdx.graphics.getHeight()/4 ) {

            direction = DIRECTION_LEFT;
            if (collisionMapper.get(player).collision_left) {
                direction = DIRECTION_NONE;
            }
        }
        return true;
    }

    @Override public boolean touchDragged (int screenX, int screenY, int pointer) {
        if (!dragging) return false;
        //camera.unproject(tp.set(screenX, screenY, 0));
//        float x = Gdx.input.getDeltaX();
//        float y = Gdx.input.getDeltaY();
//
//        camera.translate(-x,y);
        return true;
    }

    @Override public boolean touchUp (int screenX, int screenY, int pointer, int button) {
        if (button != Input.Buttons.LEFT || pointer > 0) return false;
        camera.unproject(tp.set(screenX, screenY, 0));
        dragging = false;
        player.getComponent(BodyComponent.class).body.setLinearVelocity(0, 0);
        return true;
    }

    @Override public void resize (int width, int height) {
        // viewport must be updated for it to work properly
        viewport.update(width, height, true);
    }

    @Override public void dispose () {
        // disposable stuff must be disposed
        shapes.dispose();
    }

    @Override public boolean keyDown (int keycode) {
        return false;
    }

    @Override public boolean keyUp (int keycode) {
        return false;
    }

    @Override public boolean keyTyped (char character) {
        return false;
    }

    @Override public boolean scrolled (int amount) {

        camera.zoom += .1f;
        System.out.println(amount);

        return true;
    }

    public void updateMapTiles() {
        mapTiles = engine.getEntitiesFor(Family.all(MapObjectComponent.class).get());
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