package com.ema.game.controller;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class TouchController extends ApplicationAdapter implements InputProcessor {
    // we will use 32px/unit in world
    public final static float SCALE = 32f;
    public final static float INV_SCALE = 1.f/SCALE;
    // this is our "target" resolution, not that the window can be any size, it is not bound to this one
    public final static float VP_WIDTH = 1280 * INV_SCALE;
    public final static float VP_HEIGHT = 720 * INV_SCALE;

    Vector2 mousePos;

    private OrthographicCamera camera;
    private Body player;
    private Array<Body> mapTiles;
    private ExtendViewport viewport;
    private ShapeRenderer shapes;

    private boolean canMove;

    public TouchController(OrthographicCamera camera, Body player, Array<Body> mapTiles) {
        mousePos = new Vector2(0, 0);
        this.camera = camera;
        this.player = player;
        this.mapTiles = mapTiles;
        canMove = true;

    }

    Vector3 tp = new Vector3();
    boolean dragging;
    @Override public boolean mouseMoved (int screenX, int screenY) {
        // we can also handle mouse movement without anything pressed
//		camera.unproject(tp.set(screenX, screenY, 0));
        return false;
    }

    @Override public boolean touchDown (int screenX, int screenY, int pointer, int button) {
        // ignore if its not left mouse button or first touch pointer
        if (button != Input.Buttons.LEFT || pointer > 0) return false;
        //camera.unproject(tp.set(screenX, screenY, 0));
        dragging = true;

        if (    screenX >= Gdx.graphics.getWidth()/4 &&
                screenX <= Gdx.graphics.getWidth() - Gdx.graphics.getWidth()/4 &&
                screenY <= Gdx.graphics.getHeight()/2) {

            for (Body tile : mapTiles) {
                if (tile.getFixtureList().get(0).testPoint(player.getPosition().x, player.getPosition().y + 0.32f)) {
                    canMove = false;
                }
            }

            if (canMove) {
                player.setTransform(player.getPosition().x, player.getPosition().y + 0.32f, 0);
            } else {
                canMove = true;
            }

        }
        else if(screenX >= Gdx.graphics.getWidth()/4 &&
                screenX <= Gdx.graphics.getWidth() - Gdx.graphics.getWidth()/4 &&
                screenY >= Gdx.graphics.getHeight()/2) {

            for (Body tile : mapTiles) {
                if (tile.getFixtureList().get(0).testPoint(player.getPosition().x, player.getPosition().y - 0.32f)) {
                    canMove = false;
                }
            }

            if (canMove) {
                player.setTransform(player.getPosition().x, player.getPosition().y - 0.32f, 0);
            } else {
                canMove = true;
            }

        }
        else if(screenX >= Gdx.graphics.getWidth()/2 &&
                screenY >= Gdx.graphics.getHeight()/4 &&
                screenY <= Gdx.graphics.getHeight() - Gdx.graphics.getHeight()/4 ) {

            for (Body tile : mapTiles) {
                if (tile.getFixtureList().get(0).testPoint(player.getPosition().x + 0.32f, player.getPosition().y)) {
                    canMove = false;
                }
            }

            if (canMove) {
                player.setTransform(player.getPosition().x + 0.32f, player.getPosition().y, 0);
            } else {
                canMove = true;
            }
        }
        else if(screenX <= Gdx.graphics.getWidth()/2 &&
                screenY >= Gdx.graphics.getHeight()/4 &&
                screenY <= Gdx.graphics.getHeight() - Gdx.graphics.getHeight()/4 ) {

            for (Body tile : mapTiles) {
                if (tile.getFixtureList().get(0).testPoint(player.getPosition().x - 0.32f, player.getPosition().y)) {
                    canMove = false;
                }
            }

            if (canMove) {
                player.setTransform(player.getPosition().x - 0.32f, player.getPosition().y, 0);
            } else {
                canMove = true;
            }
        }


//        System.out.println("Controller zoom " + camera.zoom);
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
        player.setLinearVelocity(0, 0);
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


    public OrthographicCamera getCamera (){
        return camera;
    }

    public Body getPlayer () {
        return player;
    }
}