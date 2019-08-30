package com.ema.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
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
    private ComponentMapperWrapper components;
    private Engine engine;
    private Entity player;
    private Skin skin;
    private WarriorComponent warrior;

    private ImageButton bash;
    private ImageButton rend;
    private ImageButton execute;
    private ImageButton armorUp;

    private ImageButton.ImageButtonStyle bashClickedStyle;
    private ImageButton.ImageButtonStyle rendClickedStyle;
    private ImageButton.ImageButtonStyle executeClickedStyle;
    private ImageButton.ImageButtonStyle armorUpClickedStyle;

    private ImageButton.ImageButtonStyle bashNormalState;
    private ImageButton.ImageButtonStyle rendNormalState;
    private ImageButton.ImageButtonStyle executeNormalState;
    private ImageButton.ImageButtonStyle armorUpNormalState;

    private ImageButton.ImageButtonStyle noImageStyle;

    float skillButtonWidth;
    float skillButtonHeight;

    public WarriorSystem(Engine engine, Skin skin) {
        player = engine.getEntitiesFor(Family.all(PlayerComponent.class).get()).first();
        components = ComponentMapperWrapper.getInstance();
        warrior = components.warriorMapper.get(player);
        this.engine = engine;
        this.skin = skin;
    }

    public void updateWarrior() {
        warrior.bashValue = 5 + (int)Math.floor(components.playerMapper.get(player).strength*0.5f);
        warrior.rendValue = 2 + (int)Math.floor(components.playerMapper.get(player).strength*0.2f);
        warrior.executeValue = 7 + (int)Math.floor(components.playerMapper.get(player).strength*0.4f);
        warrior.armorUpValue = 1 + (int)Math.floor(components.playerMapper.get(player).strength*0.1f);
    }

    public void spellCombat(Entity player, Entity enemy) {
        if (components.combatMapper.get(player).inCombat && components.combatMapper.get(enemy).inCombat) {
            if (warrior.spell == warrior.BASH && warrior.bashRemainingCD == 0) {
                engine.getSystem(CombatSystem.class).updateSpellCombat(player, enemy, warrior.bashValue, warrior.bashDuration, false, true);
                warrior.bashRemainingCD = warrior.bashCD;
                cooldownButton(bash, noImageStyle);
            } else if (warrior.spell == warrior.REND && warrior.rendRemainingCD == 0) {
                engine.getSystem(CombatSystem.class).updateSpellCombat(player, enemy, warrior.rendValue, warrior.rendDuration, true, false);
                warrior.rendRemainingCD = warrior.rendCD;
                cooldownButton(rend, noImageStyle);
            } else if (warrior.spell == warrior.EXECUTE && warrior.executeRemainingCD == 0) {
                engine.getSystem(CombatSystem.class).updateSpellCombat(player, enemy, warrior.executeValue, 0, false, false);
                warrior.executeRemainingCD = warrior.executeCooldown;
                cooldownButton(execute, noImageStyle);
            } else if (warrior.spell == warrior.ARMORUP && warrior.armorUpRemainingCD == 0) {
                if (!components.playerMapper.get(player).hasArmorBuff) {
                    components.playerMapper.get(player).armor += warrior.armorUpValue;
                    components.playerMapper.get(player).buffValue = warrior.armorUpValue;
                    components.playerMapper.get(player).armorBuffDuration = warrior.armorUpDuration;
                    components.playerMapper.get(player).hasArmorBuff = true;
                } else {
                    // If Armor Up is already existing on the player, reset the duration.
                    components.playerMapper.get(player).armorBuffDuration = warrior.armorUpDuration;
                }
                warrior.armorUpRemainingCD = warrior.armorUpCooldown;
                cooldownButton(armorUp, noImageStyle);
            }
        }

        engine.getSystem(CollisionSystem.class).updateEntityCollision();
        engine.getSystem(CollisionSystem.class).updateGlobalCollision();
    }

    public void decrementCooldowns() {
        if (warrior.bashRemainingCD > 1) {
            warrior.bashRemainingCD--;
        } else if (warrior.bashRemainingCD == 1){
            warrior.bashRemainingCD--;
            resetButton(bash, bashNormalState);
        }
        if (warrior.rendRemainingCD > 1) {
            warrior.rendRemainingCD--;
        } else if (warrior.rendRemainingCD == 1){
            warrior.rendRemainingCD--;
            resetButton(rend, rendNormalState);
        }
        if (warrior.executeRemainingCD > 1) {
            warrior.executeRemainingCD--;
        } else if (warrior.executeRemainingCD == 1){
            warrior.executeRemainingCD--;
            resetButton(execute, executeNormalState);
        }
        if (warrior.armorUpRemainingCD > 1) {
            warrior.armorUpRemainingCD--;
        } else if (warrior.armorUpRemainingCD == 1){
            warrior.armorUpRemainingCD--;
            resetButton(armorUp, armorUpNormalState);
        }
    }

    public Table updateUI(HashMap<String, Texture> skillSetTextures) {
        Table warriorUI = new Table();
        warriorUI.setPosition(Gdx.graphics.getHeight()*0.02f, Gdx.graphics.getHeight()*0.02f);
        warriorUI.setSize(Gdx.graphics.getHeight()*0.52f, Gdx.graphics.getHeight()*0.13f);
        warriorUI.left();

        ImageButton defaultSkinStyles = new ImageButton(skin);


        Drawable buttonClicked = defaultSkinStyles.getStyle().over;


        // New styles have to be created because setting just one of the values through getStyle()
        // changes the style of all the buttons
        bashClickedStyle = new ImageButton.ImageButtonStyle(buttonClicked, null, buttonClicked, new TextureRegionDrawable(skillSetTextures.get("BASH")), null, null);
        rendClickedStyle = new ImageButton.ImageButtonStyle(buttonClicked, null, buttonClicked, new TextureRegionDrawable(skillSetTextures.get("REND")), null, null);
        executeClickedStyle = new ImageButton.ImageButtonStyle(buttonClicked, null, buttonClicked, new TextureRegionDrawable(skillSetTextures.get("EXECUTE")), null, null);
        armorUpClickedStyle = new ImageButton.ImageButtonStyle(buttonClicked, null, buttonClicked, new TextureRegionDrawable(skillSetTextures.get("ARMORUP")), null, null);

        bashNormalState = new ImageButton.ImageButtonStyle(null, null, null, new TextureRegionDrawable(skillSetTextures.get("BASH_BG")), null, null);
        rendNormalState = new ImageButton.ImageButtonStyle(null, null, null, new TextureRegionDrawable(skillSetTextures.get("REND_BG")), null, null);
        executeNormalState = new ImageButton.ImageButtonStyle(null, null, null, new TextureRegionDrawable(skillSetTextures.get("EXECUTE_BG")), null, null);
        armorUpNormalState = new ImageButton.ImageButtonStyle(null, null, null, new TextureRegionDrawable(skillSetTextures.get("ARMORUP_BG")), null, null);

        noImageStyle = new ImageButton.ImageButtonStyle(defaultSkinStyles.getStyle().up, null, null, null, null, null);

        bash = new ImageButton(bashNormalState);
        rend = new ImageButton(rendNormalState);
        execute = new ImageButton(executeNormalState);
        armorUp = new ImageButton(armorUpNormalState);


        rend.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (warrior.rendRemainingCD == 0) {
                    if (warrior.spell != 1) {
                        rend.setStyle(rendClickedStyle);
                        warrior.spell = warrior.REND;
                        components.playerMapper.get(player).spellInQueue = true;

                        if (warrior.bashRemainingCD == 0) {
                            bash.setStyle(bashNormalState);
                        }
                        if (warrior.executeRemainingCD == 0) {
                            execute.setStyle(executeNormalState);
                        }
                        if (warrior.armorUpRemainingCD == 0) {
                            armorUp.setStyle(armorUpNormalState);
                        }
                    } else {
                        resetButton(rend, rendNormalState);
                    }
                }
            }
        });
        bash.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (warrior.bashRemainingCD == 0) {
                    if (warrior.spell != 2) {
                        bash.setStyle(bashClickedStyle);
                        warrior.spell = warrior.BASH;
                        components.playerMapper.get(player).spellInQueue = true;

                        if (warrior.rendRemainingCD == 0) {
                            rend.setStyle(rendNormalState);
                        }
                        if (warrior.executeRemainingCD == 0) {
                            execute.setStyle(executeNormalState);
                        }
                        if (warrior.armorUpRemainingCD == 0) {
                            armorUp.setStyle(armorUpNormalState);
                        }
                    } else {
                        resetButton(bash, bashNormalState);
                    }
                }
            }
        });

        execute.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (warrior.executeRemainingCD == 0) {
                    if (warrior.spell != 3) {
                        execute.setStyle(executeClickedStyle);
                        warrior.spell = warrior.EXECUTE;
                        components.playerMapper.get(player).spellInQueue = true;

                        if (warrior.bashRemainingCD == 0) {
                            bash.setStyle(bashNormalState);
                        }
                        if (warrior.rendRemainingCD == 0) {
                            rend.setStyle(rendNormalState);
                        }
                        if (warrior.armorUpRemainingCD == 0) {
                            armorUp.setStyle(armorUpNormalState);
                        }
                    } else {
                        resetButton(execute, executeNormalState);
                    }
                }
            }
        });

        armorUp.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (warrior.armorUpRemainingCD == 0) {
                    if (warrior.spell != 4) {
                        armorUp.setStyle(armorUpClickedStyle);
                        warrior.spell = warrior.ARMORUP;
                        components.playerMapper.get(player).spellInQueue = true;

                        if (warrior.bashRemainingCD == 0) {
                            bash.setStyle(bashNormalState);
                        }
                        if (warrior.rendRemainingCD == 0) {
                            rend.setStyle(rendNormalState);
                        }
                        if (warrior.executeRemainingCD == 0) {
                            execute.setStyle(executeNormalState);
                        }
                    } else {
                        resetButton(armorUp, armorUpNormalState);
                    }
                }
            }
        });

        skillButtonWidth = warriorUI.getWidth()/4;
        skillButtonHeight = warriorUI.getHeight();

        warriorUI.add(bash).size(skillButtonWidth, skillButtonHeight);
        warriorUI.add(rend).size(skillButtonWidth, skillButtonHeight);
        warriorUI.add(execute).size(skillButtonWidth, skillButtonHeight);
        warriorUI.add(armorUp).size(skillButtonWidth, skillButtonHeight);


        return warriorUI;
    }

    private void cooldownButton(ImageButton button, ImageButton.ImageButtonStyle style) {
        button.setStyle(style);
        button.setDisabled(true);

        warrior.spell = warrior.NO_SPELL;
        components.playerMapper.get(player).spellInQueue = false;
    }

    private void resetButton(ImageButton button, ImageButton.ImageButtonStyle style) {
        button.setStyle(style);
        button.setDisabled(false);

        warrior.spell = warrior.NO_SPELL;
        components.playerMapper.get(player).spellInQueue = false;
    }

    public Label skillLabel(Label label, int labelPosition) {

        switch (labelPosition) {
            case 0:
                if (warrior.bashRemainingCD != 0) {
                    label.setText(String.valueOf(warrior.bashRemainingCD));
                    label.setPosition(bash.getX() + skillButtonWidth/2 + label.getWidth()/2, bash.getY() + skillButtonHeight/2 + label.getHeight()/2);
                    label.setVisible(true);
                } else {
                    label.setVisible(false);
                }
                break;
            case 1:
                if (warrior.rendRemainingCD != 0) {
                    label.setText(String.valueOf(warrior.rendRemainingCD));
                    label.setPosition(rend.getX() + skillButtonWidth/2 + label.getWidth()/2, rend.getY() + skillButtonHeight/2 + label.getHeight()/2);
                    label.setVisible(true);
                } else {
                    label.setVisible(false);
                }
                break;
            case 2:
                if (warrior.executeRemainingCD != 0) {
                    label.setPosition(execute.getX() + skillButtonWidth/2 + label.getWidth()/2, execute.getY() + skillButtonHeight/2 + label.getHeight()/2);
                    label.setText(String.valueOf(warrior.executeRemainingCD));
                    label.setVisible(true);
                } else {
                    label.setVisible(false);
                }
                break;
            case 3:
                if (warrior.armorUpRemainingCD != 0) {
                    label.setPosition(  armorUp.getX() + skillButtonWidth/2 + label.getWidth()/2,
                                        armorUp.getY() + skillButtonHeight/2 + label.getHeight()/2);
                    label.setText(String.valueOf(warrior.armorUpRemainingCD));
                    label.setVisible(true);
                } else {
                    label.setVisible(false);
                }
            default:
        }

        return label;
    }
}
