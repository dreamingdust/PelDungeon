package com.ema.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.ema.game.ComponentMapperWrapper;
import com.ema.game.components.PlayerComponent;
import com.ema.game.components.WarriorComponent;

import java.util.HashMap;

public class WarriorSystem extends EntitySystem {

    ComponentMapperWrapper components;
    private Engine engine;
    Entity player;

    ImageButton bash;
    ImageButton rend;
    ImageButton execute;
    ImageButton armorUp;

    ImageButton.ImageButtonStyle bashClickedStyle;
    ImageButton.ImageButtonStyle rendClickedStyle;
    ImageButton.ImageButtonStyle executeClickedStyle;
    ImageButton.ImageButtonStyle armorUpClickedStyle;

    ImageButton.ImageButtonStyle bashNormalState;
    ImageButton.ImageButtonStyle rendNormalState;
    ImageButton.ImageButtonStyle executeNormalState;
    ImageButton.ImageButtonStyle armorUpNormalState;

    public WarriorSystem(Engine engine) {
        player = engine.getEntitiesFor(Family.all(PlayerComponent.class).get()).first();
        components = ComponentMapperWrapper.getInstance();
        this.engine = engine;
    }

    public void updateWarrior() {
        components.warriorMapper.get(player).bashValue = 5 + (int)Math.floor(components.playerMapper.get(player).strength*0.5f);
        components.warriorMapper.get(player).rendValue = 2 + (int)Math.floor(components.playerMapper.get(player).strength*0.2f);
        components.warriorMapper.get(player).executeValue = 7 + (int)Math.floor(components.playerMapper.get(player).strength*0.4f);
        components.warriorMapper.get(player).armorUpValue = 1 + (int)Math.floor(components.playerMapper.get(player).strength*0.1f);
    }

    public void spellCombat(Entity player, Entity enemy) {
        WarriorComponent warrior = components.warriorMapper.get(player);

        if (warrior.spell == warrior.BASH) {
            engine.getSystem(CombatSystem.class).updateSpellCombat(player, enemy, warrior.bashValue, warrior.bashDuration, false, true);
            resetButton(bash, bashNormalState);
        } else if (warrior.spell == warrior.REND) {
            engine.getSystem(CombatSystem.class).updateSpellCombat(player, enemy, warrior.rendValue, warrior.rendDuration, true, false);
            resetButton(rend, rendNormalState);
        } else if (warrior.spell == warrior.EXECUTE) {
            engine.getSystem(CombatSystem.class).updateSpellCombat(player, enemy, warrior.executeValue, 0, false, false);
            resetButton(execute, executeNormalState);
        } else if (warrior.spell == warrior.ARMORUP) {
            components.playerMapper.get(player).armor += warrior.armorUpValue;
            resetButton(armorUp, armorUpNormalState);
        }

        engine.getSystem(CollisionSystem.class).updateEntityCollision();
        engine.getSystem(CollisionSystem.class).updateGlobalCollision();
    }

    public Table updateUI(Skin skin, HashMap<String, Texture> skillSetTextures) {
        Table warriorUI = new Table();
        warriorUI.setSize(Gdx.graphics.getHeight()*0.4f, Gdx.graphics.getHeight()*0.1f);
        warriorUI.setPosition(Gdx.graphics.getHeight()*0.02f, Gdx.graphics.getHeight()*0.02f);
        warriorUI.left();

        ImageButton defaultSkinStyles = new ImageButton(skin);


        Drawable buttonClicked = defaultSkinStyles.getStyle().over;


        // New styles have to be created because when
        bashClickedStyle = new ImageButton.ImageButtonStyle(buttonClicked, null, buttonClicked, new TextureRegionDrawable(skillSetTextures.get("BASH")), null, null);
        rendClickedStyle = new ImageButton.ImageButtonStyle(buttonClicked, null, buttonClicked, new TextureRegionDrawable(skillSetTextures.get("REND")), null, null);
        executeClickedStyle = new ImageButton.ImageButtonStyle(buttonClicked, null, buttonClicked, new TextureRegionDrawable(skillSetTextures.get("EXECUTE")), null, null);
        armorUpClickedStyle = new ImageButton.ImageButtonStyle(buttonClicked, null, buttonClicked, new TextureRegionDrawable(skillSetTextures.get("ARMORUP")), null, null);

        bashNormalState = new ImageButton.ImageButtonStyle(null, null, null, new TextureRegionDrawable(skillSetTextures.get("BASH_BG")), null, null);
        rendNormalState = new ImageButton.ImageButtonStyle(null, null, null, new TextureRegionDrawable(skillSetTextures.get("REND_BG")), null, null);
        executeNormalState = new ImageButton.ImageButtonStyle(null, null, null, new TextureRegionDrawable(skillSetTextures.get("EXECUTE_BG")), null, null);
        executeNormalState = new ImageButton.ImageButtonStyle(null, null, null, new TextureRegionDrawable(skillSetTextures.get("EXECUTE_BG")), null, null);
        armorUpNormalState = new ImageButton.ImageButtonStyle(null, null, null, new TextureRegionDrawable(skillSetTextures.get("ARMORUP_BG")), null, null);

        bash = new ImageButton(bashNormalState);
        rend = new ImageButton(rendNormalState);
        execute = new ImageButton(executeNormalState);
        armorUp = new ImageButton(armorUpNormalState);

        bash.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (components.warriorMapper.get(player).spell != 1) {
                    bash.setStyle(bashClickedStyle);
                    components.warriorMapper.get(player).spell = components.warriorMapper.get(player).BASH;
                    components.playerMapper.get(player).spellInQueue = true;

                    rend.setStyle(rendNormalState);
                    execute.setStyle(executeNormalState);
                    armorUp.setStyle(armorUpNormalState);
                } else {
                    resetButton(bash, bashNormalState);
                }
            }
        });

        rend.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (components.warriorMapper.get(player).spell != 2) {
                    rend.setStyle(rendClickedStyle);
                    components.warriorMapper.get(player).spell = components.warriorMapper.get(player).REND;
                    components.playerMapper.get(player).spellInQueue = true;

                    bash.setStyle(bashNormalState);
                    execute.setStyle(executeNormalState);
                    armorUp.setStyle(armorUpNormalState);
                } else {
                    resetButton(rend, rendNormalState);
                }
            }
        });

        execute.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (components.warriorMapper.get(player).spell != 3) {
                    execute.setStyle(executeClickedStyle);
                    components.warriorMapper.get(player).spell= components.warriorMapper.get(player).EXECUTE;
                    components.playerMapper.get(player).spellInQueue = true;

                    bash.setStyle(bashNormalState);
                    rend.setStyle(rendNormalState);
                    armorUp.setStyle(armorUpNormalState);
                } else {
                    resetButton(execute, executeNormalState);
                }
            }
        });

        armorUp.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (components.warriorMapper.get(player).spell != 4) {
                    armorUp.setStyle(armorUpClickedStyle);
                    components.warriorMapper.get(player).spell = components.warriorMapper.get(player).ARMORUP;
                    components.playerMapper.get(player).spellInQueue = true;

                    bash.setStyle(bashNormalState);
                    rend.setStyle(rendNormalState);
                    execute.setStyle(executeNormalState);
                } else {
                    resetButton(armorUp, armorUpNormalState);
                }
            }
        });

        warriorUI.add(bash).size(warriorUI.getWidth()/4, warriorUI.getHeight());
        warriorUI.add(rend).size(warriorUI.getWidth()/4, warriorUI.getHeight());
        warriorUI.add(execute).size(warriorUI.getWidth()/4, warriorUI.getHeight());
        warriorUI.add(armorUp).size(warriorUI.getWidth()/4, warriorUI.getHeight());

        return warriorUI;
    }

    public void resetButton(ImageButton button, ImageButton.ImageButtonStyle style) {
        button.setStyle(style);
        components.warriorMapper.get(player).spell = components.warriorMapper.get(player).NO_SPELL;
        components.playerMapper.get(player).spellInQueue = false;
    }
}
