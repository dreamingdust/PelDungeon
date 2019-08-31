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
import com.ema.game.components.RogueComponent;

import java.util.HashMap;

public class RogueSystem extends EntitySystem {
    private ComponentMapperWrapper components;
    private RogueComponent rogue;
    private Engine engine;
    private Entity player;
    private Skin skin;

    private ImageButton envenom;
    private ImageButton stab;
    private ImageButton doubleStrike;
    private ImageButton vanish;

    private ImageButton.ImageButtonStyle envenomClickedStyle;
    private ImageButton.ImageButtonStyle stabClickedStyle;
    private ImageButton.ImageButtonStyle doubleStrikeClickedStyle;
    private ImageButton.ImageButtonStyle vanishClickedStyle;

    private ImageButton.ImageButtonStyle envenomNormalState;
    private ImageButton.ImageButtonStyle stabNormalState;
    private ImageButton.ImageButtonStyle doubleStrikeNormalState;
    private ImageButton.ImageButtonStyle vanishNormalState;

    private ImageButton.ImageButtonStyle noImageStyle;

    float skillButtonWidth;
    float skillButtonHeight;

    public RogueSystem(Engine engine, Skin skin) {
        player = engine.getEntitiesFor(Family.all(PlayerComponent.class).get()).first();
        components = ComponentMapperWrapper.getInstance();
        rogue = components.rogueMapper.get(player);
        this.engine = engine;
        this.skin = skin;
    }


    public void updateRogue() {
        rogue.envenomValue = 2 + (int)Math.floor(components.playerMapper.get(player).strength*0.2f);
        rogue.stabValue = 2 + components.playerMapper.get(player).strength + components.playerMapper.get(player).level;
        rogue.doubleStrikeValue = components.playerMapper.get(player).strength*2;
        rogue.vanishValue = 2 + (int)Math.floor(components.playerMapper.get(player).strength*0.2f);
    }

    public void spellCombat(Entity player, Entity enemy) {
        if (components.combatMapper.get(player).inCombat && components.combatMapper.get(enemy).inCombat) {
            if (rogue.spell == rogue.ENVENOM) {
                if (!components.playerMapper.get(player).hasStrengthBuff) {
                    components.playerMapper.get(player).strength += rogue.envenomValue;
                    components.playerMapper.get(player).buffValue = rogue.envenomValue;
                    components.playerMapper.get(player).strengthBuffDuration = rogue.envenomDuration;
                    components.playerMapper.get(player).hasStrengthBuff = true;
                } else {
                    components.playerMapper.get(player).strengthBuffDuration = rogue.envenomDuration;
                }
                rogue.envenomRemainingCD = rogue.envenomCD;
                cooldownButton(envenom, noImageStyle);
            } else if (rogue.spell == rogue.STAB) {
                engine.getSystem(CombatSystem.class).updateSpellCombat(player, enemy, rogue.stabValue, 0, false, false);
                rogue.stabRemainingCD = rogue.stabCD;
                cooldownButton(stab, noImageStyle);
            } else if (rogue.spell == rogue.DOUBLE_STRIKE) {
                engine.getSystem(CombatSystem.class).updateSpellCombat(player, enemy, rogue.doubleStrikeValue, 0, false, false);
                rogue.doubleStrikeRemainingCD = rogue.doubleStrikeCD;
                cooldownButton(doubleStrike, noImageStyle);
            } else if (rogue.spell == rogue.VANISH) {
                engine.getSystem(CombatSystem.class).updateSpellCombat(player, enemy, rogue.doubleStrikeValue, rogue.vanishDuration, false, true);
                rogue.vanishRemainingCD = rogue.vanishCD;
                cooldownButton(vanish, noImageStyle);
            }
        }

        engine.getSystem(CollisionSystem.class).updateEntityCollision();
        engine.getSystem(CollisionSystem.class).updateGlobalCollision();
    }

    public void decrementCooldowns() {
        if (rogue.envenomRemainingCD > 1) {
            rogue.envenomRemainingCD--;
        } else if (rogue.envenomRemainingCD == 1){
            rogue.envenomRemainingCD--;
            resetButton(envenom, envenomNormalState);
        }
        if (rogue.stabRemainingCD > 1) {
            rogue.stabRemainingCD--;
        } else if (rogue.stabRemainingCD == 1){
            rogue.stabRemainingCD--;
            resetButton(stab, stabNormalState);
        }
        if (rogue.doubleStrikeRemainingCD > 1) {
            rogue.doubleStrikeRemainingCD--;
        } else if (rogue.doubleStrikeRemainingCD == 1){
            rogue.doubleStrikeRemainingCD--;
            resetButton(doubleStrike, doubleStrikeNormalState);
        }
        if (rogue.vanishRemainingCD > 1) {
            rogue.vanishRemainingCD--;
        } else if (rogue.vanishRemainingCD == 1){
            rogue.vanishRemainingCD--;
            resetButton(vanish, vanishNormalState);
        }
    }

    public Table updateUI(HashMap<String, Texture> skillSetTextures) {
        Table rogueUI = new Table();
        rogueUI.setPosition(Gdx.graphics.getHeight()*0.02f, Gdx.graphics.getHeight()*0.02f);
        rogueUI.setSize(Gdx.graphics.getHeight()*0.52f, Gdx.graphics.getHeight()*0.13f);
        rogueUI.left();

        ImageButton defaultSkinStyles = new ImageButton(skin);


        Drawable buttonClicked = defaultSkinStyles.getStyle().over;


        envenomClickedStyle = new ImageButton.ImageButtonStyle(buttonClicked, null, buttonClicked, new TextureRegionDrawable(skillSetTextures.get("ENVENOM")), null, null);
        stabClickedStyle = new ImageButton.ImageButtonStyle(buttonClicked, null, buttonClicked, new TextureRegionDrawable(skillSetTextures.get("STAB")), null, null);
        doubleStrikeClickedStyle = new ImageButton.ImageButtonStyle(buttonClicked, null, buttonClicked, new TextureRegionDrawable(skillSetTextures.get("DOUBLE_STRIKE")), null, null);
        vanishClickedStyle = new ImageButton.ImageButtonStyle(buttonClicked, null, buttonClicked, new TextureRegionDrawable(skillSetTextures.get("VANISH")), null, null);

        envenomNormalState = new ImageButton.ImageButtonStyle(null, null, null, new TextureRegionDrawable(skillSetTextures.get("ENVENOM_BG")), null, null);
        stabNormalState = new ImageButton.ImageButtonStyle(null, null, null, new TextureRegionDrawable(skillSetTextures.get("STAB_BG")), null, null);
        doubleStrikeNormalState = new ImageButton.ImageButtonStyle(null, null, null, new TextureRegionDrawable(skillSetTextures.get("DOUBLE_STRIKE_BG")), null, null);
        vanishNormalState = new ImageButton.ImageButtonStyle(null, null, null, new TextureRegionDrawable(skillSetTextures.get("VANISH_BG")), null, null);

        noImageStyle = new ImageButton.ImageButtonStyle(defaultSkinStyles.getStyle().up, null, null, null, null, null);

        envenom = new ImageButton(envenomNormalState);
        stab = new ImageButton(stabNormalState);
        doubleStrike = new ImageButton(doubleStrikeNormalState);
        vanish = new ImageButton(vanishNormalState);

        envenom.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (rogue.envenomRemainingCD == 0) {
                    if (rogue.spell != 1) {
                        envenom.setStyle(envenomClickedStyle);
                        rogue.spell = rogue.ENVENOM;
                        components.playerMapper.get(player).spellInQueue = true;

                        if (rogue.stabRemainingCD == 0) {
                            stab.setStyle(stabNormalState);
                        }
                        if (rogue.doubleStrikeRemainingCD == 0) {
                            doubleStrike.setStyle(doubleStrikeNormalState);
                        }
                        if (rogue.vanishRemainingCD == 0) {
                            vanish.setStyle(vanishNormalState);
                        }
                    } else {
                        resetButton(envenom, envenomNormalState);
                    }
                }
            }
        });

        stab.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (rogue.stabRemainingCD == 0) {
                    if (rogue.spell != 2) {
                        stab.setStyle(stabClickedStyle);
                        rogue.spell = rogue.STAB;
                        components.playerMapper.get(player).spellInQueue = true;

                        if (rogue.envenomRemainingCD == 0) {
                            envenom.setStyle(envenomNormalState);
                        }
                        if (rogue.doubleStrikeRemainingCD == 0) {
                            doubleStrike.setStyle(doubleStrikeNormalState);
                        }
                        if (rogue.vanishRemainingCD == 0) {
                            vanish.setStyle(vanishNormalState);
                        }
                    } else {
                        resetButton(stab, stabNormalState);
                    }
                }
            }
        });

        doubleStrike.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (rogue.doubleStrikeRemainingCD == 0) {
                    if (rogue.spell != 3) {
                        doubleStrike.setStyle(doubleStrikeClickedStyle);
                        rogue.spell = rogue.DOUBLE_STRIKE;
                        components.playerMapper.get(player).spellInQueue = true;

                        if (rogue.envenomRemainingCD == 0) {
                            envenom.setStyle(envenomNormalState);
                        }
                        if (rogue.stabRemainingCD == 0) {
                            stab.setStyle(stabNormalState);
                        }
                        if (rogue.vanishRemainingCD == 0) {
                            vanish.setStyle(vanishNormalState);
                        }
                    } else {
                        resetButton(doubleStrike, doubleStrikeNormalState);
                    }
                }
            }
        });

        vanish.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (rogue.vanishRemainingCD == 0) {
                    if (rogue.spell != 4) {
                        vanish.setStyle(vanishClickedStyle);
                        rogue.spell = rogue.VANISH;
                        components.playerMapper.get(player).spellInQueue = true;

                        if (rogue.envenomRemainingCD == 0) {
                            envenom.setStyle(envenomNormalState);
                        }
                        if (rogue.stabRemainingCD == 0) {
                            stab.setStyle(stabNormalState);
                        }
                        if (rogue.doubleStrikeRemainingCD == 0) {
                            doubleStrike.setStyle(doubleStrikeNormalState);
                        }
                    } else {
                        resetButton(vanish, vanishNormalState);
                    }
                }
            }
        });

        skillButtonWidth = rogueUI.getWidth()/4;
        skillButtonHeight = rogueUI.getHeight();

        rogueUI.add(envenom).size(skillButtonWidth, skillButtonHeight);
        rogueUI.add(stab).size(skillButtonWidth, skillButtonHeight);
        rogueUI.add(doubleStrike).size(skillButtonWidth, skillButtonHeight);
        rogueUI.add(vanish).size(skillButtonWidth, skillButtonHeight);

        return rogueUI;
    }

    private void cooldownButton(ImageButton button, ImageButton.ImageButtonStyle style) {
        button.setStyle(style);
        button.setDisabled(true);

        rogue.spell = rogue.NO_SPELL;
        components.playerMapper.get(player).spellInQueue = false;
    }

    public void resetButton(ImageButton button, ImageButton.ImageButtonStyle style) {
        button.setStyle(style);
        rogue.spell = rogue.NO_SPELL;
        components.playerMapper.get(player).spellInQueue = false;
    }

    public Label skillLabel(Label label, int labelPosition) {

        switch (labelPosition) {
            case 0:
                if (rogue.envenomRemainingCD != 0) {
                    label.setText(String.valueOf(rogue.envenomRemainingCD));
                    label.setPosition(envenom.getX() + skillButtonWidth/2 + label.getWidth()/2, envenom.getY() + skillButtonHeight/2 + label.getHeight()/2);
                    label.setVisible(true);
                } else {
                    label.setVisible(false);
                }
                break;
            case 1:
                if (rogue.stabRemainingCD != 0) {
                    label.setText(String.valueOf(rogue.stabRemainingCD));
                    label.setPosition(stab.getX() + skillButtonWidth/2 + label.getWidth()/2, stab.getY() + skillButtonHeight/2 + label.getHeight()/2);
                    label.setVisible(true);
                } else {
                    label.setVisible(false);
                }
                break;
            case 2:
                if (rogue.doubleStrikeRemainingCD != 0) {
                    label.setText(String.valueOf(rogue.doubleStrikeRemainingCD));
                    label.setPosition(doubleStrike.getX() + skillButtonWidth/2 + label.getWidth()/2, doubleStrike.getY() + skillButtonHeight/2 + label.getHeight()/2);
                    label.setVisible(true);
                } else {
                    label.setVisible(false);
                }
                break;
            case 3:
                if (rogue.vanishRemainingCD != 0) {
                    label.setText(String.valueOf(rogue.vanishRemainingCD));
                    label.setPosition(  vanish.getX() + skillButtonWidth/2 + label.getWidth()/2,
                            vanish.getY() + skillButtonHeight/2 + label.getHeight()/2);
                    label.setVisible(true);
                } else {
                    label.setVisible(false);
                }
            default:
        }

        return label;
    }
}
