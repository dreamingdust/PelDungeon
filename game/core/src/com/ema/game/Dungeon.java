package com.ema.game;

import com.ema.game.screens.ChooseHeroScreen;
import com.ema.game.screens.LoadingScreen;
import com.ema.game.screens.MainScreen;
import com.ema.game.screens.MenuScreen;

public class Dungeon extends com.badlogic.gdx.Game {
    public enum GameScreen {MENU, CHOOSE_HERO, APPLICATION}

    private LoadingScreen loadingScreen;
    private MenuScreen menuScreen;
    private ChooseHeroScreen chooseHeroScreen;
    private MainScreen mainScreen;

    public GameAssetManager assetManager;
    public void create () {
        assetManager = new GameAssetManager();

        assetManager.queueAddImages();
        assetManager.queueAddSkin();

        assetManager.manager.finishLoading();

        loadingScreen = new LoadingScreen(this);
        setScreen(loadingScreen);
    }

    public void changeScreen(GameScreen screen){
        switch(screen){
            case MENU:
                if(menuScreen == null) menuScreen = new MenuScreen(this);
                this.setScreen(menuScreen);
                break;
            case CHOOSE_HERO:
                if(chooseHeroScreen == null) {
                    chooseHeroScreen = new ChooseHeroScreen(this);
                }
                this.setScreen(chooseHeroScreen);
                break;
            case APPLICATION:
                if(mainScreen == null) {
                    mainScreen = new MainScreen(this);
                } else {
                    // TODO: implement resetWorld function to clear already existing entities and objects
                    // mainScreen.resetWorld();
                }
                this.setScreen(mainScreen);
                break;
        }
    }
}
