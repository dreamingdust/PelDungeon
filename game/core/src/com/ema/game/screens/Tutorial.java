package com.ema.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ema.game.Dungeon;

import static com.ema.game.Dungeon.GameScreen.MENU;

public class Tutorial implements Screen {
    private Dungeon parent;
    private Stage stage;
    private Skin skin;

    String tutorial;

    public Tutorial (Dungeon game) {
        parent = game;

        /// create stage and set it as input processor
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        tutorial = new String("How to move? \n" +
                "Touch the sides of the display to move in the respective direction. \n" +
                "Movement to walls is not possible and you have to find your way through the dungeon fighting monsters and improving your character. \n" +
                "You have a choice between two classes - Warrior and Rogue. Both classes have a set of four abilities which have different effects. \n" +
                "The Warrior has Bash that does small amount of damage and stuns the enemy for two rounds. \n" +
                "The next two skills do moderate damage and the last one increases her armor. \n" +
                "The Rogue can increase his own strength with envenom and do high amount of damage with his two middle abilities.\n" +
                "His last ability stuns the enemys for three rounds.");
    }

    @Override
    public void show() {
        // Create a table that fills the screen. Everything else will go inside this table.
        Table table = new Table();
        table.setFillParent(true);
        Texture bg = parent.assetManager.manager.get("images/stone_gray2.png");

        table.background(new TiledDrawable(new TextureRegion(bg)));
//        table.setDebug(true);
        stage.addActor(table);

        skin = parent.assetManager.manager.get("skin/craftacular-ui.json");


        //create buttons
        TextButton gameOver = new TextButton(tutorial, skin);
        gameOver.setDisabled(true);

        TextButton startOver = new TextButton("Go to menu", skin);

        //add buttons to table
        table.add(gameOver).fillX().uniformX().size(Gdx.graphics.getWidth()*0.4f, Gdx.graphics.getHeight()*0.3f);
        table.row().pad(10, 0, 10, 0);
        table.add(startOver).fillX().uniformX().size(Gdx.graphics.getWidth()*0.2f, Gdx.graphics.getHeight()*0.1f);
        table.row().pad(10, 0, 10, 0);

        startOver.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(MENU, 0);
            }
        });
    }

    @Override
    public void render(float delta) {

        // clear the screen ready for next set of images to be drawn
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // tell our stage to do actions and draw itself
        stage.act();
        stage.draw();
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
