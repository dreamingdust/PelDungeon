package com.ema.game.screens;

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
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ema.game.ComponentMapperWrapper;
import com.ema.game.Dungeon;
import com.ema.game.GameAssetManager;
import com.ema.game.MapBodyBuilder;
import com.ema.game.components.BodyComponent;
import com.ema.game.components.EnemyComponent;
import com.ema.game.components.TextureComponent;
import com.ema.game.controller.TouchController;
import com.ema.game.systems.CollisionSystem;
import com.ema.game.systems.CombatSystem;
import com.ema.game.systems.MovementSystem;

import java.util.Random;

public class MainScreen implements Screen {

    private Dungeon parent;
    private OrthographicCamera camera;
    private TiledMap map;
    private Stage stage;
    private final Box2DDebugRenderer box2DDebugRenderer;
    private final TouchController controller;
    private final ScreenViewport viewport;
    private final SpriteBatch batch;
    private final MapBodyBuilder mapBodyBuilder;
    private final World world;
    private Skin skin;

    private InputProcessor skillsController;
    private InputMultiplexer inputMultiplexer;
    private PooledEngine engine;
    private Entity playerEntity;
    private Array<Entity> enemyEntities;
    private Array<Entity> objectEntities;
    private Array<Entity> groundEntities;
    private Array<Body> mapDoors;
    private ComponentMapperWrapper components;
    private Family renderFamily;

    private Texture wallTexture;
    private Texture groundTexture;
    private Texture doorTexture;

    private Texture healthBarTexture;
    private ProgressBar playerHealthBar;
    private ImageButton playerImage;
    private ProgressBar enemyHealthBar;
    private ImageButton enemyImage;

    private Table skillSet;

    public MainScreen(Dungeon game) {

        // TODO: Skills. Select skill -> target enemy. Self-buffs used directly?
        // TODO: Change orientation? What happens? Can it work without any issues? Research needed.

        // TODO: Health bar for the enemy.

        parent = game;
        camera = new OrthographicCamera();
        box2DDebugRenderer = new Box2DDebugRenderer();
        batch = new SpriteBatch();
        world = new World(new Vector2(0,0), true);
//        viewport = new FitViewport(640/50f, 480/50f, camera);
        viewport = new ScreenViewport(camera);
        viewport.setUnitsPerPixel(0.01f);
        stage = new Stage();
        engine = new PooledEngine();
        mapBodyBuilder = new MapBodyBuilder(world, engine);
        camera.zoom = 0.4f;
        skin = parent.assetManager.manager.get("skin/craftacular-ui.json");

        inputMultiplexer = new InputMultiplexer();

        objectEntities = mapBodyBuilder.createWalls();
        groundEntities = mapBodyBuilder.createGround();
        playerEntity = mapBodyBuilder.createPlayer();
        enemyEntities = mapBodyBuilder.createEnemies();

        components = ComponentMapperWrapper.getInstance();






        Random rand = new Random(System.currentTimeMillis());

        mapDoors = mapBodyBuilder.getDoors();


        Entity startTile = groundEntities.get(rand.nextInt(groundEntities.size));

        components.bodyMapper.get(playerEntity).body.setTransform(startTile.getComponent(BodyComponent.class).body.getPosition(), 0);

        components.textureMapper.get(playerEntity).texture = parent.assetManager.manager.get("images/heroine.png", Texture.class);

        int i;
        for (Entity enemy : enemyEntities) {
            i = rand.nextInt(groundEntities.size);
//            components.bodyMapper.get(enemy).body.setTransform(groundEntities.get(rand.nextInt(groundEntities.size)).getComponent(BodyComponent.class).body.getPosition(), 0);
//            components.bodyMapper.get(enemy).body.setTransform(components.bodyMapper.get(groundEntities.get(rand.nextInt(groundEntities.size))).body.getPosition(), 0);
            components.bodyMapper.get(enemy).body.setTransform(components.bodyMapper.get(groundEntities.get(i)).body.getPosition(), 0);
            components.textureMapper.get(enemy).texture = parent.assetManager.manager.get("images/grey_rat.png");
        }


        for (Entity object : objectEntities) {
            components.textureMapper.get(object).texture = parent.assetManager.manager.get("images/brick_gray0.png");
        }

        for (Entity ground : groundEntities) {
            components.textureMapper.get(ground).texture = parent.assetManager.manager.get("images/cobble_blood3.png");
        }



        healthBarTexture = parent.assetManager.manager.get("images/health_bar.png");

        camera.position.set(components.bodyMapper.get(playerEntity).body.getPosition(), 0);


        map = mapBodyBuilder.getMap();



//        controller = new TouchController(camera, playerEntity, mapTiles);
        controller = new TouchController(camera, playerEntity, engine);

        engine.addSystem(new CollisionSystem(engine));

        engine.addSystem(new MovementSystem(controller, engine));
        engine.addSystem(new CombatSystem(engine, world));

//
//        doorTexture = parent.assetManager.manager.get("images/dngn_closed_door.png");
//        for (Body door : mapDoors) {
//            door.setUserData(doorTexture);
//        }



        playerHealthBar = new ProgressBar(0f, components.playerMapper.get(playerEntity).maxHealth, 0.01f, false, skin);
        playerHealthBar.setPosition(Gdx.graphics.getHeight()*0.02f, Gdx.graphics.getHeight() - Gdx.graphics.getHeight() * 0.15f);
        playerHealthBar.setSize(Gdx.graphics.getWidth() * 0.3f, Gdx.graphics.getHeight() * 0.2f);
        playerHealthBar.setColor(Color.RED);
//        playerHealthBar.getStyle().knobAfter = new TextureRegionDrawable(healthBarTexture);
        stage.addActor(playerHealthBar);

//        PLAYER_IMAGE = new ImageButton(new TextureRegionDrawable(new TextureRegion(components.textureMapper.get(playerEntity).texture)));
//        PLAYER_IMAGE.setTouchable(Touchable.disabled);
//        PLAYER_IMAGE.setPosition(2, Gdx.graphics.getHeight() - Gdx.graphics.getHeight() * 0.15f);
//        PLAYER_IMAGE.setSize(Gdx.graphics.getWidth() * 0.3f, Gdx.graphics.getWidth() * 0.3f);
//        stage.addActor(PLAYER_IMAGE);


        enemyHealthBar = new ProgressBar(0f, components.playerMapper.get(playerEntity).maxHealth, 0.01f, false, skin);
        enemyHealthBar.setPosition(Gdx.graphics.getWidth() - (Gdx.graphics.getWidth() * 0.35f ), Gdx.graphics.getHeight() - (Gdx.graphics.getHeight() * 0.15f));
        enemyHealthBar.setSize(Gdx.graphics.getWidth() * 0.3f, Gdx.graphics.getHeight() * 0.2f);
        enemyHealthBar.setColor(Color.RED);
//        playerHealthBar.getStyle().knobAfter = new TextureRegionDrawable(healthBarTexture);
        enemyHealthBar.setDisabled(true);
        stage.addActor(enemyHealthBar);


        skillSet = new Table();
        skillSet.setSize(Gdx.graphics.getHeight()*0.4f, Gdx.graphics.getHeight()*0.1f);
        skillSet.setPosition(Gdx.graphics.getHeight()*0.02f, Gdx.graphics.getHeight()*0.02f);
        skillSet.left();

        ImageButton bash = new ImageButton(skin);
        ImageButton rend = new ImageButton(skin);
        ImageButton execute = new ImageButton(skin);
        ImageButton armorUp = new ImageButton(skin);

        bash.addListener(new ClickListener() {

        });

        System.out.println(bash.getStyle().imageChecked);

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageChecked = new SpriteDrawable(skin.getSprite("button-hover"));
        System.out.println(style.imageChecked);

        skillSet.add(bash).size(skillSet.getWidth()/4, skillSet.getHeight());
        skillSet.add(rend).size(skillSet.getWidth()/4, skillSet.getHeight());
        skillSet.add(execute).size(skillSet.getWidth()/4, skillSet.getHeight());
        skillSet.add(armorUp).size(skillSet.getWidth()/4, skillSet.getHeight());

        stage.addActor(skillSet);






        renderFamily = Family.all(TextureComponent.class).get();


//        enemyImage = new ImageButton(new TextureRegionDrawable(new TextureRegion(components.textureMapper.get(enemyEntities.get(0)).texture)));
//        enemyImage.setTouchable(Touchable.disabled);
//        enemyImage.setPosition(Gdx.graphics.getWidth() - (Gdx.graphics.getWidth() * 0.40f), Gdx.graphics.getHeight() - (Gdx.graphics.getHeight() * 0.15f));
//        enemyImage.setSize(Gdx.graphics.getWidth() * 0.3f, Gdx.graphics.getWidth() * 0.3f);
//        stage.addActor(enemyImage);

        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(controller);
        Gdx.input.setInputProcessor(inputMultiplexer);

    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0,0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        engine.update(delta);

        batch.begin();


        for (Entity object : engine.getEntitiesFor(renderFamily)) {
            batch.draw(components.textureMapper.get(object).texture,
                    components.bodyMapper.get(object).body.getPosition().x - 0.15f,
                    components.bodyMapper.get(object).body.getPosition().y - 0.15f, 0.3f, 0.3f);
        }


        batch.end();

         playerHealthBar.setRange(0f, components.playerMapper.get(playerEntity).maxHealth);
         playerHealthBar.setValue(components.playerMapper.get(playerEntity).health);

         for (Entity enemy : engine.getEntitiesFor(Family.all(EnemyComponent.class).get())) {
             if (components.enemyMapper.get(enemy).hitLast) {
                 enemyHealthBar.setDisabled(false);
                 enemyHealthBar.setRange(0f, components.enemyMapper.get(enemy).maxHealth);
                 enemyHealthBar.setValue(components.enemyMapper.get(enemy).health);
             }
         }

        stage.act();
        stage.draw();

        camera = controller.getCamera();

//        camera.position.set(player.getPosition(), 0);
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
        map.dispose();
        box2DDebugRenderer.dispose();
        world.dispose();
        stage.dispose();
    }
}
