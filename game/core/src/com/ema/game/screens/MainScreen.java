package com.ema.game.screens;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ema.game.ComponentMapperWrapper;
import com.ema.game.Dungeon;
import com.ema.game.MapBodyBuilder;
import com.ema.game.components.BodyComponent;
import com.ema.game.components.EnemyComponent;
import com.ema.game.components.MapGroundComponent;
import com.ema.game.components.MapObjectComponent;
import com.ema.game.components.PlayerComponent;
import com.ema.game.components.RogueComponent;
import com.ema.game.components.TextureComponent;
import com.ema.game.controller.TouchController;
import com.ema.game.systems.CollisionSystem;
import com.ema.game.systems.CombatSystem;
import com.ema.game.systems.MovementSystem;
import com.ema.game.systems.RogueSystem;
import com.ema.game.systems.WarriorSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class MainScreen implements Screen {

    private Dungeon parent;
    private OrthographicCamera camera;
    private Stage stage;
    private Box2DDebugRenderer box2DDebugRenderer;
    private TouchController controller;
    private ScreenViewport viewport;
    private SpriteBatch batch;
    private World world;
    private MapBodyBuilder mapBodyBuilder;
    private Skin skin;
    private int playerClass;

    private InputMultiplexer inputMultiplexer;
    private PooledEngine engine;
    private Entity playerEntity;
    private Entity playerContainer;
    private Entity exitEntity;
    private Array<Entity> enemyEntities;
    private Array<Entity> objectEntities;
    private Array<Entity> groundEntities;
    private Array<Entity> itemEntities;
    private ComponentMapperWrapper components;
    private Family renderFamily;
    private Vector3 playerPosition;

    private ProgressBar playerHealthBar;
    private ProgressBar enemyHealthBar;
    private Label playerHealthBarText;
    private Label enemyHealthBarText;

    private Label playerDamageTaken;
    private Label enemyDamageTaken;

    private ArrayList<Label> skillCDLabels;

    private Table skillSet;
    private Table bagSet;
    private ImageButton bag;

    private Random rand;
    private int dungeonLevel = 0;

    public MainScreen(Dungeon game, int playerClass) {

        parent = game;
        camera = new OrthographicCamera();
        box2DDebugRenderer = new Box2DDebugRenderer();
        batch = new SpriteBatch();
        world = new World(new Vector2(0,0), true);
//        viewport = new FitViewport(640/50f, 480/50f, camera);
        viewport = new ScreenViewport(camera);
        viewport.setUnitsPerPixel(0.01f);
        camera.zoom = 0.7f; // 0.25f
        skin = parent.assetManager.manager.get("skin/craftacular-ui.json");
        this.playerClass = playerClass;

        skillCDLabels = new ArrayList<>();

        newMap();

    }

    private void newMap() {
        if (engine != null) {
            engine.removeAllEntities();
        }

        Array<Body> bodies=new Array<>();
        world.getBodies(bodies);
        for (Body body: bodies){
                world.destroyBody(body);
        }

        engine = new PooledEngine();
        mapBodyBuilder = new MapBodyBuilder(world, engine);
        stage = new Stage();

        dungeonLevel++;

        inputMultiplexer = new InputMultiplexer();

        objectEntities = mapBodyBuilder.createWalls();
        groundEntities = mapBodyBuilder.createGround();
        playerEntity = mapBodyBuilder.createPlayer(playerClass);
        itemEntities = mapBodyBuilder.createItems(parent);
        enemyEntities = mapBodyBuilder.createEnemies();
        exitEntity = mapBodyBuilder.createExit();


        components = ComponentMapperWrapper.getInstance();

        rand = new Random(System.currentTimeMillis());


        Entity startTile = groundEntities.get(rand.nextInt(groundEntities.size));
        Entity exitTile = groundEntities.get(rand.nextInt(groundEntities.size));

        components.bodyMapper.get(exitEntity).body.setTransform(components.bodyMapper.get(exitTile).body.getPosition(), 0);
        components.textureMapper.get(exitEntity).texture = parent.assetManager.manager.get("images/stone_stairs_down.png");

        components.bodyMapper.get(playerEntity).body.setTransform(components.bodyMapper.get(startTile).body.getPosition(), 0);

        for (Entity enemy : enemyEntities) {
            components.bodyMapper.get(enemy).body.setTransform(components.bodyMapper.get(groundEntities.get(rand.nextInt(groundEntities.size))).body.getPosition(), 0);
            components.textureMapper.get(enemy).texture = parent.assetManager.manager.get("images/grey_rat.png");
        }

        for (Entity item : itemEntities) {
            components.bodyMapper.get(item).body.setTransform(components.bodyMapper.get(groundEntities.get(rand.nextInt(groundEntities.size))).body.getPosition(), 0);
        }

        for (Entity object : objectEntities) {
            components.textureMapper.get(object).texture = parent.assetManager.manager.get("images/brick_gray0.png");
        }

        for (Entity ground : groundEntities) {
            components.textureMapper.get(ground).texture = parent.assetManager.manager.get("images/cobble_blood3.png");
        }

        if (dungeonLevel == 1) {
            playerContainer = new Entity();
            playerContainer.add(new PlayerComponent());
        }


        camera.position.set(components.bodyMapper.get(playerEntity).body.getPosition(), 0);




        controller = new TouchController(camera, playerEntity, engine);

        engine.addSystem(new CollisionSystem(engine));
        engine.addSystem(new MovementSystem(controller, engine, world));
        engine.addSystem(new CombatSystem(engine, world));


        playerHealthBar = new ProgressBar(0f, components.playerMapper.get(playerEntity).maxHealth, 0.01f, false, skin);
        playerHealthBar.setPosition(Gdx.graphics.getHeight()*0.02f, Gdx.graphics.getHeight() - Gdx.graphics.getHeight() * 0.15f);
        playerHealthBar.setSize(Gdx.graphics.getWidth() * 0.3f, Gdx.graphics.getHeight() * 0.2f);
        playerHealthBar.setColor(Color.RED);
        stage.addActor(playerHealthBar);

        playerHealthBarText = new Label(String.valueOf(components.playerMapper.get(playerEntity).maxHealth), skin);
        playerHealthBarText.setSize(playerHealthBar.getWidth()*0.3f, playerHealthBar.getHeight()*0.3f);
        playerHealthBarText.setPosition(playerHealthBar.getX() + playerHealthBar.getWidth()*0.7f, playerHealthBar.getY() + playerHealthBar.getHeight()*0.35f);
        stage.addActor(playerHealthBarText);




        enemyHealthBar = new ProgressBar(0f, components.playerMapper.get(playerEntity).maxHealth, 0.01f, false, skin);
        enemyHealthBar.setPosition(Gdx.graphics.getWidth() - (Gdx.graphics.getWidth() * 0.35f ), Gdx.graphics.getHeight() - (Gdx.graphics.getHeight() * 0.15f));
        enemyHealthBar.setSize(Gdx.graphics.getWidth() * 0.3f, Gdx.graphics.getHeight() * 0.2f);
        enemyHealthBar.setColor(Color.RED);
        enemyHealthBar.setVisible(false);
        stage.addActor(enemyHealthBar);

        enemyHealthBarText = new Label(String.valueOf(components.enemyMapper.get(enemyEntities.get(0)).maxHealth), skin);
        enemyHealthBarText.setSize(enemyHealthBar.getWidth()*0.3f, enemyHealthBar.getHeight()*0.3f);
        enemyHealthBarText.setPosition(enemyHealthBar.getX(), enemyHealthBar.getY() + enemyHealthBar.getHeight()*0.35f);
        enemyHealthBarText.setVisible(false);
        stage.addActor(enemyHealthBarText);


//        playerDamageTaken = new Label("TestTestTest", skin);
//        playerDamageTaken.setPosition(components.bodyMapper.get(playerEntity).body.getPosition().x, components.bodyMapper.get(playerEntity).body.getPosition().y);
//        playerDamageTaken.setColor(1f, 0f, 0f, 1f);
//        playerDamageTaken.addAction(Actions.sequence(Actions.delay(1f), Actions.fadeOut(2f)));
//        stage.addActor(playerDamageTaken);


        HashMap<String, Texture> skillSetTextures = new HashMap<>();


        if (playerClass == components.playerMapper.get(playerEntity).WARRIOR_CLASS) {
            engine.addSystem(new WarriorSystem(engine, skin));

            components.textureMapper.get(playerEntity).texture = parent.assetManager.manager.get("images/heroine.png", Texture.class);

            skillSetTextures.put("BASH", parent.assetManager.manager.get("images/chain_mace_w_backg.png"));
            skillSetTextures.put("REND", parent.assetManager.manager.get("images/dh_axe_w_backg.png"));
            skillSetTextures.put("EXECUTE", parent.assetManager.manager.get("images/sh_axe_w_backg.png"));
            skillSetTextures.put("ARMORUP", parent.assetManager.manager.get("images/shield_w_backg.png"));

            skillSetTextures.put("BASH_BG", parent.assetManager.manager.get("images/chain_mace_backg.png"));
            skillSetTextures.put("REND_BG", parent.assetManager.manager.get("images/dh_axe_backg.png"));
            skillSetTextures.put("EXECUTE_BG", parent.assetManager.manager.get("images/sh_axe_backg.png"));
            skillSetTextures.put("ARMORUP_BG", parent.assetManager.manager.get("images/shield_backg.png"));

            skillSet = engine.getSystem(WarriorSystem.class).updateUI(skillSetTextures);
        } else if (playerClass == components.playerMapper.get(playerEntity).ROGUE_CLASS) {
            engine.addSystem(new RogueSystem(engine, skin));

            components.textureMapper.get(playerEntity).texture = parent.assetManager.manager.get("images/hero.png", Texture.class);

            skillSetTextures.put("ENVENOM", parent.assetManager.manager.get("images/poison_w_backg.png"));
            skillSetTextures.put("STAB", parent.assetManager.manager.get("images/dagger_w_backg.png"));
            skillSetTextures.put("DOUBLE_STRIKE", parent.assetManager.manager.get("images/sword_w_backg.png"));
            skillSetTextures.put("VANISH", parent.assetManager.manager.get("images/chain_mace_w_backg.png"));

            skillSetTextures.put("ENVENOM_BG", parent.assetManager.manager.get("images/poison_backg.png"));
            skillSetTextures.put("STAB_BG", parent.assetManager.manager.get("images/dagger_backg.png"));
            skillSetTextures.put("DOUBLE_STRIKE_BG", parent.assetManager.manager.get("images/sword_backg.png"));
            skillSetTextures.put("VANISH_BG", parent.assetManager.manager.get("images/chain_mace_backg.png"));

            skillSet = engine.getSystem(RogueSystem.class).updateUI(skillSetTextures);
        }

        stage.addActor(skillSet);

        for (int i = 0; i <= 3; i++) {
            skillCDLabels.add(new Label("", skin));
            skillCDLabels.get(i).setPosition(Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/2f);
            skillCDLabels.get(i).setSize(20f, 20f);
            skillCDLabels.get(i).setFontScale(1.5f, 1.5f);

            stage.addActor(skillCDLabels.get(i));
        }


//        bagSet = new Table();
////        bagSet.setPosition();
//        bagSet.setSize(Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/2f);
//        bagSet.setDebug(true);
//
//        stage.addActor(bagSet);
//
//        bag = new ImageButton(skin);



        renderFamily = Family.all(TextureComponent.class).exclude(PlayerComponent.class).get();


        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(controller);
        Gdx.input.setInputProcessor(inputMultiplexer);

    }

    private void updatePlayerEntity (Entity playerCurrent, Entity player) {
        PlayerComponent playerComponent = components.playerMapper.get(player);
        PlayerComponent playerCurrentComponent = components.playerMapper.get(playerCurrent);

        playerComponent.hasStrengthBuff = playerCurrentComponent.hasStrengthBuff;
        playerComponent.strengthBuffDuration = playerCurrentComponent.strengthBuffDuration;
        playerComponent.strengthBuffValue = playerCurrentComponent.strengthBuffValue;
        playerComponent.hasArmorBuff = playerCurrentComponent.hasArmorBuff;
        playerComponent.armorBuffDuration = playerCurrentComponent.armorBuffDuration;
        playerComponent.armorBuffValue = playerCurrentComponent.armorBuffValue;
        playerComponent.health = playerCurrentComponent.health;
        playerComponent.maxHealth = playerCurrentComponent.maxHealth;
        playerComponent.strength = playerCurrentComponent.strength;
        playerComponent.level = playerCurrentComponent.level;
        playerComponent.xp = playerCurrentComponent.xp;
        playerComponent.xpForLevel = playerCurrentComponent.xpForLevel;
        playerComponent.playerClass = playerCurrentComponent.playerClass;
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0,0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        engine.update(delta);

        if (components.playerMapper.get(playerEntity).onExit) {
            updatePlayerEntity(playerEntity, playerContainer);
            newMap();
            updatePlayerEntity(playerContainer, playerEntity);
            components.playerMapper.get(playerEntity).onExit = false;
        }

        batch.begin();


        for (Entity object : engine.getEntitiesFor(renderFamily)) {
            batch.draw( components.textureMapper.get(object).texture,
                    components.textureMapper.get(object).flip ? components.bodyMapper.get(object).body.getPosition().x + 0.15f : components.bodyMapper.get(object).body.getPosition().x - 0.15f,
                    components.bodyMapper.get(object).body.getPosition().y - 0.15f,
                    components.textureMapper.get(object).flip ? - 0.3f : 0.3f, 0.3f);
        }
        batch.draw( components.textureMapper.get(playerEntity).texture,
                components.textureMapper.get(playerEntity).flip ? components.bodyMapper.get(playerEntity).body.getPosition().x + 0.15f : components.bodyMapper.get(playerEntity).body.getPosition().x - 0.15f,
                components.bodyMapper.get(playerEntity).body.getPosition().y - 0.15f,
                components.textureMapper.get(playerEntity).flip ? - 0.3f : 0.3f, 0.3f);

        batch.end();
//
        playerHealthBar.setRange(0f, components.playerMapper.get(playerEntity).maxHealth);
        playerHealthBar.setValue(components.playerMapper.get(playerEntity).health);
        playerHealthBarText.setText(components.playerMapper.get(playerEntity).health + "/" + components.playerMapper.get(playerEntity).maxHealth);


        for (Entity enemy : engine.getEntitiesFor(Family.all(EnemyComponent.class).get())) {
            if (components.enemyMapper.get(enemy).hitLast) {
                enemyHealthBar.setVisible(true);
                enemyHealthBarText.setVisible(true);
                enemyHealthBar.setRange(0f, components.enemyMapper.get(enemy).maxHealth);
                enemyHealthBar.setValue(components.enemyMapper.get(enemy).health);
                enemyHealthBarText.setText(components.enemyMapper.get(enemy).health + "/" + components.enemyMapper.get(enemy).maxHealth);
                break;
            } else {
                enemyHealthBar.setVisible(false);
                enemyHealthBarText.setVisible(false);
            }
        }

        playerPosition = camera.project(new Vector3(components.bodyMapper.get(playerEntity).body.getPosition(), 0));
//        playerDamageTaken.setPosition(playerPosition.x, playerPosition.y);

        // 1 for Warrior, 2 for Rogue
        if (playerClass == 1) {
            for (int i = 0; i <= skillCDLabels.size() - 1; i++) {
                skillCDLabels.set(i, engine.getSystem(WarriorSystem.class).skillLabel(skillCDLabels.get(i), i));
            }
        } else if (playerClass == 2) {
            for (int i = 0; i <= skillCDLabels.size() - 1; i++) {
                skillCDLabels.set(i, engine.getSystem(RogueSystem.class).skillLabel(skillCDLabels.get(i), i));
            }
        }

        stage.act();
        stage.draw();

        if (engine.getSystem(CombatSystem.class).checkPlayer(playerEntity)) {
            parent.changeScreen(Dungeon.GameScreen.GAME_OVER, 0);
        }

        camera = controller.getCamera();

        camera.position.set(playerEntity.getComponent(BodyComponent.class).body.getPosition(), 0);

        camera.update();

        world.step(delta, 6, 2);
        batch.setProjectionMatrix(camera.combined);


//        box2DDebugRenderer.render(world, camera.combined);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
//        camera.viewportWidth = width;
//        camera.viewportHeight = height;
//        camera.update();
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
        box2DDebugRenderer.dispose();
        world.dispose();
        stage.dispose();
    }
}
