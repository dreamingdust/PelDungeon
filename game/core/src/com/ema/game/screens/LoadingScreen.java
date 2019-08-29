package com.ema.game.screens;

import com.badlogic.gdx.Screen;
import com.ema.game.Dungeon;

import static com.ema.game.Dungeon.GameScreen.MENU;

public class LoadingScreen implements Screen {

    private Dungeon parent;

    public LoadingScreen (Dungeon game) {
        parent = game;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        parent.changeScreen(MENU);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
