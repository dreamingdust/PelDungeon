package com.ema.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class GameAssetManager {

    public final AssetManager manager = new AssetManager();

    //public final String images = new TextureAtlas("images/pixel_dungeon_spritesheet.png");

    public final String PLAYER_IMAGE = "images/human_m.png";
    public final String WALL_IMAGE = "images/brick_gray0.png";
    public final String GROUND_IMAGE = "images/cobble_blood3.png";
    public final String DOOR_IMAGE = "images/dngn_closed_door.png";
    public final String RAT_IMAGE = "images/grey_rat.png";
    public final String HEALTH_BAR_IMAGE = "images/health_bar.png";
    public final String BACKGROUND_IMAGE = "images/stone_gray2.png";
    public final String HERO_IMAGE = "images/heroine.png";
    //public final String enemyImage = "images/enemy.png";

    public final String SKIN = "skin/craftacular-ui.json";

    public void queueAddImages(){
        manager.load(PLAYER_IMAGE, Texture.class);
        manager.load(WALL_IMAGE, Texture.class);
        manager.load(GROUND_IMAGE, Texture.class);
        manager.load(DOOR_IMAGE, Texture.class);
        manager.load(RAT_IMAGE, Texture.class);
        manager.load(HEALTH_BAR_IMAGE, Texture.class);
        manager.load(BACKGROUND_IMAGE, Texture.class);
        manager.load(HERO_IMAGE, Texture.class);
        //manager.load(enemyImage, Texture.class);
    }



    public void queueAddSkin(){
        SkinLoader.SkinParameter params = new SkinLoader.SkinParameter("skin/craftacular-ui.atlas");
        manager.load(SKIN, Skin.class, params);
    }
}
