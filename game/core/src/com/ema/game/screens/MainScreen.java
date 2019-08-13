package com.ema.game.screens;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.decals.SimpleOrthoGroupStrategy;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ema.game.BodyFactory;
import com.ema.game.Dungeon;
import com.ema.game.MapBodyBuilder;
import com.ema.game.controller.TouchController;

public class MainScreen implements Screen {

    private Dungeon parent;
    private OrthographicCamera camera;
    private TiledMap map;
    private TiledMapTileLayer tileLayer;
    private OrthogonalTiledMapRenderer renderer;
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
        camera = new OrthographicCamera();
        batch = new SpriteBatch();
        box2DDebugRenderer = new Box2DDebugRenderer();
        world = new World(new Vector2(0,0), true);
        viewport = new FitViewport(640/50f, 480/50f, camera);
        mapBodyBuilder = new MapBodyBuilder(world);
        camera.zoom = 0.4f;

        player = mapBodyBuilder.getPlayer();
        camera.position.set(player.getPosition(), 0);

        mapTiles = mapBodyBuilder.getMapTiles();
        groundTiles = mapBodyBuilder.getGroundTiles();
        mapDoors = mapBodyBuilder.getDoors();


        map = new TmxMapLoader().load("map/first_level.tmx");


        renderer = new OrthogonalTiledMapRenderer(map, Gdx.graphics.getWidth()/50f);
        tileLayer = (TiledMapTileLayer)map.getLayers().get("background");



        controller = new TouchController(camera, player, mapTiles);
        Gdx.input.setInputProcessor(controller);

//
//        batch = new SpriteBatch();
//        batch.setProjectionMatrix(camera.combined);
//

        parent.assetManager.queueAddImages();
        parent.assetManager.manager.finishLoading();
        playerTexture = parent.assetManager.manager.get("images/human_m.png");
        wallTexture = parent.assetManager.manager.get("images/brick_gray0.png");
        groundTexture = parent.assetManager.manager.get("images/cobble_blood3.png");
        doorTexture = parent.assetManager.manager.get("images/dngn_closed_door.png");

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
//
//        renderer = new OrthogonalTiledMapRenderer(map);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {


        Gdx.gl.glClearColor(0, 0,0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
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
        batch.draw(playerTexture, player.getPosition().x - 0.15f, player.getPosition().y - 0.15f, 0.3f, 0.3f);
        batch.end();

        camera = controller.getCamera();

        camera.position.set(player.getPosition(), 0);
        camera.update();
        renderer.setView(camera);

        world.step(delta, 6, 2);
        batch.setProjectionMatrix(camera.combined);

//        renderer.getBatch().begin();
//        renderer.renderTileLayer(tileLayer);
//        renderer.getBatch().end();

        renderer.render();
        //box2DDebugRenderer.render(world, camera.combined);
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
        //box2DDebugRenderer.dispose();
        //world.dispose();
    }
}
