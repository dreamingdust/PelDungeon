package com.ema.game;

import com.ema.game.screens.ChooseHeroScreen;
import com.ema.game.screens.GameOverScreen;
import com.ema.game.screens.MainScreen;
import com.ema.game.screens.MenuScreen;

public class Dungeon extends com.badlogic.gdx.Game {
    public enum GameScreen {MENU, CHOOSE_HERO, APPLICATION, GAME_OVER}

    public GameAssetManager assetManager;
    private DatabaseHelper dbHelper;


    public Dungeon(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public DatabaseHelper getDbHelper() {
        return dbHelper;
    }

    public void create () {

        assetManager = new GameAssetManager();

        assetManager.queueAddImages();
        assetManager.queueAddSkin();

        assetManager.manager.finishLoading();

        changeScreen(GameScreen.MENU, 0);
    }

    public void changeScreen(GameScreen screen, int playerClass){
        switch(screen){
            case MENU:
                this.setScreen(new MenuScreen(this));
                break;
            case CHOOSE_HERO:
                this.setScreen(new ChooseHeroScreen(this));
                break;
            case APPLICATION:
                this.setScreen(new MainScreen(this, playerClass));
                break;
            case GAME_OVER:
                this.setScreen(new GameOverScreen(this));
        }
    }
}
