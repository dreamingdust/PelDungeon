package com.ema.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ema.game.Dungeon;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import javax.xml.soap.Text;

import static com.ema.game.Dungeon.GameScreen.APPLICATION;

public class ChooseHeroScreen implements Screen {
    private Dungeon parent;
    private Stage stage;

    private final int WARRIOR_CLASS = 1;
    private final int ROGUE_CLASS = 2;

    public ChooseHeroScreen(Dungeon game){
        parent = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        Table table = new Table();
        table.setFillParent(true);
        Texture bg = parent.assetManager.manager.get("images/stone_gray2.png");
        table.background(new TiledDrawable(new TextureRegion(bg)));

        stage.addActor(table);

        Skin skin = parent.assetManager.manager.get("skin/craftacular-ui.json");
        TextButton chooseYHero = new TextButton("CHOOSE YOUR HERO", skin);
        chooseYHero.setDisabled(true);
        table.add(chooseYHero).fillX().uniformX().colspan(2);

        Texture warriorTexture = parent.assetManager.manager.get("images/heroine.png");
        Texture rogueTexture= parent.assetManager.manager.get("images/hero.png");
        ImageButton warrior = new ImageButton(new TextureRegionDrawable(warriorTexture));
        ImageButton rogue = new ImageButton(new TextureRegionDrawable(rogueTexture));


        table.row().pad(20, 10, 20, 10);
        table.add(warrior).fillX().uniformX().size(Gdx.graphics.getWidth()*0.2f, Gdx.graphics.getHeight()*0.5f);
        table.add(rogue).fillX().uniformX().size(Gdx.graphics.getWidth()*0.2f, Gdx.graphics.getHeight()*0.5f);

        TextButton warriorLabel = new TextButton("WARRIOR", skin);
        TextButton rogueLabel = new TextButton("ROGUE", skin);
        warriorLabel.setDisabled(true);
        rogueLabel.setDisabled(true);

        table.row().pad(20, 10, 20, 10);
        table.add(warriorLabel).fillX().uniformX().size(Gdx.graphics.getWidth()*0.2f, Gdx.graphics.getHeight()*0.05f);
        table.add(rogueLabel).fillX().uniformX().size(Gdx.graphics.getWidth()*0.2f, Gdx.graphics.getHeight()*0.05f);

        warrior.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(APPLICATION, WARRIOR_CLASS);
            }
        });

        rogue.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(APPLICATION, ROGUE_CLASS);
            }
        });

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}