package com.ema.game.screens;

import com.badlogic.ashley.core.Entity;
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
import com.ema.game.components.PlayerComponent;
import com.ema.game.components.TextureComponent;
import com.ema.game.controller.TouchController;
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
    private Body player;
    private Array<Body> mapTiles;
    private Array<Body> groundTiles;
    private Array<Body> mapDoors;

    private PooledEngine engine;
    private Entity playerEntity;


    private Texture playerTexture;
    private Texture wallTexture;
    private Texture groundTexture;
    private Texture doorTexture;

    private int tileWidth, tileHeight,
            mapWidthInTiles, mapHeightInTiles,
            mapWidthInPixels, mapHeightInPixels;

    public MainScreen(Dungeon game) {
//
//        parent = game;
//
//
//
//        camera = new OrthographicCamera();
//
//        map = new TmxMapLoader().load("map/first_level.tmx");
//
//        MapProperties properties = map.getProperties();
//        tileWidth         = properties.get("tilewidth", Integer.class);
//        tileHeight        = properties.get("tileheight", Integer.class);
//        mapWidthInTiles   = properties.get("width", Integer.class);
//        mapHeightInTiles  = properties.get("height", Integer.class);
//        mapWidthInPixels  = mapWidthInTiles  * tileWidth;
//        mapHeightInPixels = mapHeightInTiles * tileHeight;
////
////        camera.position.x = mapWidthInPixels * .5f;
////        camera.position.y = mapHeightInPixels * .35f;
//
//        camera.zoom = 20f;
//
//        renderer = new OrthogonalTiledMapRenderer(map);


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


        playerEntity = mapBodyBuilder.createPlayer();







        Random rand = new Random(System.currentTimeMillis());





        assetManager.queueAddImages();
        assetManager.manager.finishLoading();


        player = mapBodyBuilder.getPlayer();
//        camera.position.set(player.getPosition(), 0);

        mapTiles = mapBodyBuilder.getMapTiles();
        groundTiles = mapBodyBuilder.getGroundTiles();
        mapDoors = mapBodyBuilder.getDoors();


        Body startTile = groundTiles.get(rand.nextInt(groundTiles.size));
        playerEntity.getComponent(BodyComponent.class).body.setTransform(startTile.getPosition().x, startTile.getPosition().y, 0);
        playerEntity.getComponent(TextureComponent.class).texture = assetManager.manager.get("images/human_m.png");

        camera.position.set(playerEntity.getComponent(BodyComponent.class).body.getPosition(), 0);


        map = mapBodyBuilder.getMap();



        controller = new TouchController(camera, playerEntity, mapTiles);
        Gdx.input.setInputProcessor(controller);


        engine.addSystem(new MovementSystem(controller));



        playerTexture = assetManager.manager.get("images/human_m.png");
        wallTexture = assetManager.manager.get("images/brick_gray0.png");
        groundTexture = assetManager.manager.get("images/cobble_blood3.png");
        doorTexture = assetManager.manager.get("images/dngn_closed_door.png");

        player.setUserData(playerTexture);
        for (Body tile : mapTiles) {
            tile.setUserData(wallTexture);
        }
        for (Body ground : groundTiles) {
            ground.setUserData(groundTexture);
        }
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
        for (Body tile : mapTiles) {
            batch.draw(wallTexture, tile.getPosition().x - 0.15f, tile.getPosition().y - 0.15f, 0.3f, 0.3f);
//            batch.draw(wallTexture, tile.getPosition().x - 0.15f, tile.getPosition().y - 0.15f, tile.getFixtureList().get(0).getUserData(), 0.3f);
        }
        for (Body tile : groundTiles) {
            batch.draw(groundTexture, tile.getPosition().x - 0.15f, tile.getPosition().y - 0.15f, 0.3f, 0.3f);
        }
        for (Body tile : mapDoors) {
            batch.draw(doorTexture, tile.getPosition().x - 0.15f, tile.getPosition().y - 0.15f, 0.3f, 0.3f);
        }
        batch.draw(playerEntity.getComponent(TextureComponent.class).texture,
                playerEntity.getComponent(BodyComponent.class).body.getPosition().x - 0.15f,
                playerEntity.getComponent(BodyComponent.class).body.getPosition().y - 0.15f, 0.3f, 0.3f);

        batch.end();

        camera = controller.getCamera();

//        camera.position.set(player.getPosition(), 0);
        camera.position.set(playerEntity.getComponent(BodyComponent.class).body.getPosition(), 0);

        camera.update();

        world.step(delta, 6, 2);
        batch.setProjectionMatrix(camera.combined);

        box2DDebugRenderer.render(world, camera.combined);
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
