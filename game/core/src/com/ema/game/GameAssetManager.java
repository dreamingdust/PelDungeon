package com.ema.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class GameAssetManager {

    public final AssetManager manager = new AssetManager();

    //public final String images = new TextureAtlas("images/pixel_dungeon_spritesheet.png");

    public final String playerImage = "images/human_m.png";
    public final String wallImage = "images/brick_gray0.png";
    public final String groundImage = "images/cobble_blood3.png";
    public final String doorImage = "images/dngn_closed_door.png";
    public final String ratImage = "images/grey_rat.png";
    public final String healthBarImage = "images/health_bar.png";
    public final String backgroundImage = "images/stone_gray2.png";
    //public final String enemyImage = "images/enemy.png";

    public final String skin = "skin/craftacular-ui.json";

    public void queueAddImages(){
        manager.load(playerImage, Texture.class);
        manager.load(wallImage, Texture.class);
        manager.load(groundImage, Texture.class);
        manager.load(doorImage, Texture.class);
        manager.load(ratImage, Texture.class);
        manager.load(healthBarImage, Texture.class);
        manager.load(backgroundImage, Texture.class);
        //manager.load(enemyImage, Texture.class);
    }



    public void queueAddSkin(){
        SkinLoader.SkinParameter params = new SkinLoader.SkinParameter("skin/craftacular-ui.atlas");
        manager.load(skin, Skin.class, params);
    }
}
