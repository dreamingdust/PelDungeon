package com.ema.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GameAssetManager {

    public final AssetManager manager = new AssetManager();

    //public final String images = new TextureAtlas("images/pixel_dungeon_spritesheet.png");

    public final String playerImage = "images/human_m.png";
    public final String wallImage = "images/brick_gray0.png";
    public final String groundImage = "images/cobble_blood3.png";
    public final String doorImage = "images/dngn_closed_door.png";
    //public final String enemyImage = "images/enemy.png";

    public void queueAddImages(){
        manager.load(playerImage, Texture.class);
        manager.load(wallImage, Texture.class);
        manager.load(groundImage, Texture.class);
        manager.load(doorImage, Texture.class);
        //manager.load(enemyImage, Texture.class);
    }
}
