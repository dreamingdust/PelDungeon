package com.ema.game.screens;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ema.game.Dungeon;
import com.ema.game.GameAssetManager;
import com.ema.game.MapBodyBuilder;
import com.ema.game.components.BodyComponent;
import com.ema.game.components.EnemyComponent;
import com.ema.game.components.MapGroundComponent;
import com.ema.game.components.TextureComponent;
import com.ema.game.components.TypeComponent;
import com.ema.game.controller.TouchController;
import com.ema.game.systems.CollisionSystem;
import com.ema.game.systems.MovementSystem;

import java.util.Random;

public class MainScreen implements Screen {

    private Dungeon parent;
    private OrthographicCamera camera;
    private TiledMap map;
    private TiledMapTileLayer tileLayer;
    private OrthogonalTiledMapRenderer renderer;
    public GameAssetManager assetManager;
    private final Box2DDebugRenderer box2DDebugRenderer;
    private final TouchController controller;
    private final Viewport viewport;
    private final SpriteBatch batch;
    private final MapBodyBuilder mapBodyBuilder;
    private final World world;
//    private Array<Body> mapTiles;
//    private Array<Body> groundTiles;
    private Array<Body> mapDoors;

    private PooledEngine engine;
    private Entity playerEntity;
    private Array<Entity> enemyEntities;
    private Array<Entity> objectEntities;
    private Array<Entity> groundEntities;
    private ComponentMapper<BodyComponent> bodyMapper;
    private ComponentMapper<TextureComponent> textureMapper;

    private Texture wallTexture;
    private Texture groundTexture;
    private Texture doorTexture;

    public MainScreen(Dungeon game) {

        parent = game;
        assetManager = new GameAssetManager();
        camera = new OrthographicCamera();
        box2DDebugRenderer = new Box2DDebugRenderer();
        batch = new SpriteBatch();
        world = new World(new Vector2(0,0), true);
        viewport = new FitViewport(640/50f, 480/50f, camera);
        engine = new PooledEngine();
        mapBodyBuilder = new MapBodyBuilder(world, engine);
        camera.zoom = 0.4f;

        assetManager.queueAddImages();
        assetManager.manager.finishLoading();

        objectEntities = mapBodyBuilder.createWalls();
        groundEntities = mapBodyBuilder.createGround();
        playerEntity = mapBodyBuilder.createPlayer();
        enemyEntities = mapBodyBuilder.createEnemies();

        bodyMapper = ComponentMapper.getFor(BodyComponent.class);
        textureMapper = ComponentMapper.getFor(TextureComponent.class);






        Random rand = new Random(System.currentTimeMillis());





//        mapTiles = mapBodyBuilder.getMapTiles();
//        groundTiles = mapBodyBuilder.getGroundTiles();
        mapDoors = mapBodyBuilder.getDoors();


        Entity startTile = groundEntities.get(rand.nextInt(groundEntities.size));

//        playerEntity.getComponent(BodyComponent.class).body.setTransform(startTile.getComponent(BodyComponent.class).body.getPosition(), 0);
        bodyMapper.get(playerEntity).body.setTransform(startTile.getComponent(BodyComponent.class).body.getPosition(), 0);

        playerEntity.getComponent(TextureComponent.class).texture = assetManager.manager.get("images/human_m.png");

        for (Entity enemy : enemyEntities) {
//            enemy.getComponent(BodyComponent.class).body.setTransform(groundEntities.get(rand.nextInt(groundEntities.size)).getComponent(BodyComponent.class).body.getPosition(), 0);
            bodyMapper.get(enemy).body.setTransform(groundEntities.get(rand.nextInt(groundEntities.size)).getComponent(BodyComponent.class).body.getPosition(), 0);
            enemy.getComponent(TextureComponent.class).texture = assetManager.manager.get("images/grey_rat.png");
        }

        for (Entity object : objectEntities) {
            textureMapper.get(object).texture = assetManager.manager.get("images/brick_gray0.png");
        }
        for (Entity ground : groundEntities) {
            textureMapper.get(ground).texture = assetManager.manager.get("images/cobble_blood3.png");
        }

        camera.position.set(playerEntity.getComponent(BodyComponent.class).body.getPosition(), 0);


        map = mapBodyBuilder.getMap();



//        controller = new TouchController(camera, playerEntity, mapTiles);
        controller = new TouchController(camera, playerEntity, engine);
        Gdx.input.setInputProcessor(controller);

        engine.addSystem(new CollisionSystem(engine));

        engine.addSystem(new MovementSystem(controller, engine));


        doorTexture = assetManager.manager.get("images/dngn_closed_door.png");
        for (Body door : mapDoors) {
            door.setUserData(doorTexture);
        }
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
//        mapTiles.forEach((tile) -> batch.draw(wallTexture, tile.getPosition().x - 0.15f, tile.getPosition().y - 0.15f, 0.3f, 0.3f));
//        for (Body tile : mapTiles) {
//            batch.draw(wallTexture, tile.getPosition().x - 0.15f, tile.getPosition().y - 0.15f, 0.3f, 0.3f);
////            batch.draw(wallTexture, tile.getPosition().x - 0.15f, tile.getPosition().y - 0.15f, tile.getFixtureList().get(0).getUserData(), 0.3f);
//        }
//        for (Body tile : groundTiles) {
//            batch.draw(groundTexture, tile.getPosition().x - 0.15f, tile.getPosition().y - 0.15f, 0.3f, 0.3f);
//        }
//        for (Body tile : mapDoors) {
//            batch.draw(doorTexture, tile.getPosition().x - 0.15f, tile.getPosition().y - 0.15f, 0.3f, 0.3f);
//        }
//        for (Entity object : objectEntities) {
//            batch.draw(textureMapper.get(object).texture,
//                    object.getComponent(BodyComponent.class).body.getPosition().x - 0.15f,
//                    object.getComponent(BodyComponent.class).body.getPosition().y - 0.15f, 0.3f, 0.3f);
//        }
//        for (Entity ground : groundEntities) {
//            batch.draw(ground.getComponent(TextureComponent.class).texture,
//                    ground.getComponent(BodyComponent.class).body.getPosition().x - 0.15f,
//                    ground.getComponent(BodyComponent.class).body.getPosition().y - 0.15f, 0.3f, 0.3f);
//        }
//        batch.draw(playerEntity.getComponent(TextureComponent.class).texture,
//                playerEntity.getComponent(BodyComponent.class).body.getPosition().x - 0.15f,
//                playerEntity.getComponent(BodyComponent.class).body.getPosition().y - 0.15f, 0.3f, 0.3f);
//
//        for (Entity enemy : enemyEntities) {
//            batch.draw(enemy.getComponent(TextureComponent.class).texture,
//                    enemy.getComponent(BodyComponent.class).body.getPosition().x - 0.15f,
//                    enemy.getComponent(BodyComponent.class).body.getPosition().y - 0.15f, 0.3f, 0.3f);
//        }

        Family renderFamily = Family.all(TextureComponent.class).get();

        for (Entity object : engine.getEntitiesFor(renderFamily)) {
            batch.draw(textureMapper.get(object).texture,
                    bodyMapper.get(object).body.getPosition().x - 0.15f,
                    bodyMapper.get(object).body.getPosition().y - 0.15f, 0.3f, 0.3f);
        }




        batch.end();

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
        renderer.dispose();
        box2DDebugRenderer.dispose();
        world.dispose();
    }
}
