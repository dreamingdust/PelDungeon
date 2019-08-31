package com.ema.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class GameAssetManager {

    public final AssetManager manager = new AssetManager();

    // Map textures
    private final String WALL_IMAGE = "images/brick_gray0.png";
    private final String GROUND_IMAGE = "images/cobble_blood3.png";
    private final String DOOR_IMAGE = "images/dngn_closed_door.png";
    private final String RAT_IMAGE = "images/grey_rat.png";
    private final String BACKGROUND_IMAGE = "images/stone_gray2.png";
    private final String HERO_IMAGE = "images/hero.png";
    private final String HEROINE_IMAGE = "images/heroine.png";
    private final String EXIT_IMAGE = "images/stone_stairs_down.png";

    // Skill textures
    private final String DH_AXE_BG_IMAGE = "images/dh_axe_backg.png";
    private final String DH_AXE_IMAGE = "images/dh_axe_w_backg.png";

    private final String SH_AXE_BG_IMAGE = "images/sh_axe_backg.png";
    private final String SH_AXE_IMAGE = "images/sh_axe_w_backg.png";

    private final String MACE_BG_IMAGE = "images/chain_mace_backg.png";
    private final String MACE_IMAGE = "images/chain_mace_w_backg.png";

    private final String DAGGER_BG_IMAGE = "images/dagger_backg.png";
    private final String DAGGER_IMAGE = "images/dagger_w_backg.png";

    private final String SWORD_BG_IMAGE = "images/sword_backg.png";
    private final String SWORD_IMAGE = "images/sword_w_backg.png";

    private final String SHIELD_BG_IMAGE = "images/shield_backg.png";
    private final String SHIELD_IMAGE = "images/shield_w_backg.png";

    private final String POISON_BG_IMAGE = "images/poison_backg.png";
    private final String POISON_IMAGE = "images/poison_w_backg.png";


    private final String WEAPON_IMAGE = "images/sabre1_silver.png";
    private final String ARMOR_IMAGE = "images/elven_scalemail.png";
    private final String POTION_IMAGE = "images/magenta.png";
    private final String SCROLL_IMAGE = "images/poison_w_backg.png";


    private final String SKIN = "skin/craftacular-ui.json";


    public void queueAddImages(){
        manager.load(WALL_IMAGE, Texture.class);
        manager.load(GROUND_IMAGE, Texture.class);
        manager.load(DOOR_IMAGE, Texture.class);
        manager.load(RAT_IMAGE, Texture.class);
        manager.load(BACKGROUND_IMAGE, Texture.class);
        manager.load(HERO_IMAGE, Texture.class);
        manager.load(HEROINE_IMAGE, Texture.class);
        manager.load(EXIT_IMAGE, Texture.class);

        manager.load(DH_AXE_BG_IMAGE, Texture.class);
        manager.load(DH_AXE_IMAGE, Texture.class);

        manager.load(SH_AXE_BG_IMAGE, Texture.class);
        manager.load(SH_AXE_IMAGE, Texture.class);

        manager.load(MACE_BG_IMAGE, Texture.class);
        manager.load(MACE_IMAGE, Texture.class);

        manager.load(DAGGER_BG_IMAGE, Texture.class);
        manager.load(DAGGER_IMAGE, Texture.class);

        manager.load(SWORD_BG_IMAGE, Texture.class);
        manager.load(SWORD_IMAGE, Texture.class);

        manager.load(SHIELD_BG_IMAGE, Texture.class);
        manager.load(SHIELD_IMAGE, Texture.class);

        manager.load(POISON_BG_IMAGE, Texture.class);
        manager.load(POISON_IMAGE, Texture.class);


        manager.load(WEAPON_IMAGE, Texture.class);
        manager.load(ARMOR_IMAGE, Texture.class);
        manager.load(POTION_IMAGE, Texture.class);


    }



    public void queueAddSkin(){
        SkinLoader.SkinParameter params = new SkinLoader.SkinParameter("skin/craftacular-ui.atlas");
        manager.load(SKIN, Skin.class, params);
    }
}
