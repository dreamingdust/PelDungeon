package com.ema.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.ema.game.components.BodyComponent;
import com.ema.game.components.PlayerComponent;
import com.ema.game.components.TextureComponent;
import com.ema.game.components.TransformComponent;
import com.ema.game.components.TypeComponent;

public class MapBodyBuilder implements Disposable {


    private static final String MAP_WALL = "walls";
    private static final String MAP_PLAYER = "player";
    private static final String MAP_GROUND = "ground";
    private static final String MAP_DOOR = "doors";

    private World world;
    private TiledMap map;
    private PooledEngine engine;
    private BodyFactory bodyFactory;
    private Array<Body> mapTiles;
    private Array<Body> mapGround;
    private Array<Body> mapDoors;
    private Array<Entity> objects;

    public MapBodyBuilder(World world, PooledEngine engine) {
        this.world = world;
        this.engine = engine;
        map = new TmxMapLoader().load("map/test_map.tmx");
        bodyFactory = BodyFactory.getInstance(world);
        mapTiles = new Array<>();
        mapGround = new Array<>();
        mapDoors = new Array<>();

        Array<RectangleMapObject> walls = map.getLayers().get(MAP_WALL).getObjects().getByType(RectangleMapObject.class);
        for (RectangleMapObject rObject : new Array.ArrayIterator<RectangleMapObject>(walls)) {
            Rectangle rectangle = rObject.getRectangle();
            mapTiles.add(bodyFactory.makeBoxPolyBody(
                    new Vector2(rectangle.getX() + rectangle.getWidth() / 2, rectangle.getY() + rectangle.getHeight() / 2), // position
                    new Vector2(rectangle.getWidth() / 2, rectangle.getHeight() / 2), // size
                    BodyDef.BodyType.StaticBody, world,4, false));
        }

        Array<RectangleMapObject> ground = map.getLayers().get(MAP_GROUND).getObjects().getByType(RectangleMapObject.class);
        for (RectangleMapObject rObject : new Array.ArrayIterator<RectangleMapObject>(ground)) {
            Rectangle rectangle = rObject.getRectangle();
            mapGround.add(bodyFactory.makeBoxPolyBody(
                    new Vector2(rectangle.getX() + rectangle.getWidth() / 2, rectangle.getY() + rectangle.getHeight() / 2), // position
                    new Vector2(rectangle.getWidth() / 2, rectangle.getHeight() / 2), // size
                    BodyDef.BodyType.StaticBody, world,4, true));
        }

        Array<RectangleMapObject> doors = map.getLayers().get(MAP_DOOR).getObjects().getByType(RectangleMapObject.class);
        for (RectangleMapObject rObject : new Array.ArrayIterator<RectangleMapObject>(doors)) {
            Rectangle rectangle = rObject.getRectangle();
            mapDoors.add(bodyFactory.makeBoxPolyBody(
                    new Vector2(rectangle.getX() + rectangle.getWidth() / 2, rectangle.getY() + rectangle.getHeight() / 2), // position
                    new Vector2(rectangle.getWidth() / 2, rectangle.getHeight() / 2), // size
                    BodyDef.BodyType.StaticBody, world,4, false));
        }

    }

    public Body getPlayer() {
        final Rectangle rectangle = map.getLayers().get(MAP_PLAYER).getObjects().getByType(RectangleMapObject.class).get(0).getRectangle();
        return bodyFactory.makeBoxPolyBody(
                new Vector2(rectangle.getX() + rectangle.getWidth() / 2, rectangle.getY() + rectangle.getHeight() / 2), // position
                new Vector2(rectangle.getWidth() / 2, rectangle.getHeight() / 2), // size
                BodyDef.BodyType.DynamicBody, world, 0, false);
    }
















    public Entity createPlayer() {
        Entity entity = engine.createEntity();
        BodyComponent body = engine.createComponent(BodyComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        PlayerComponent player = engine.createComponent(PlayerComponent.class);
        TypeComponent type = engine.createComponent(TypeComponent.class);

        final Rectangle rectangle = map.getLayers().get(MAP_PLAYER).getObjects().getByType(RectangleMapObject.class).get(0).getRectangle();

        body.body = bodyFactory.makeBoxPolyBody(
                new Vector2(rectangle.getX() + rectangle.getWidth() / 2, rectangle.getY() + rectangle.getHeight() / 2), // position
                new Vector2(rectangle.getWidth() / 2, rectangle.getHeight() / 2), // size
                BodyDef.BodyType.DynamicBody, world, 0, false);

        body.body.setSleepingAllowed(false);

        position.position.set(rectangle.getX() + rectangle.getWidth() / 2, rectangle.getY() + rectangle.getHeight() / 2, 0);
        type.type = TypeComponent.PLAYER;

        entity.add(body);
        entity.add(position);
        entity.add(texture);
        entity.add(player);
        entity.add(type);

        engine.addEntity(entity);
        return entity;
    }

    public void addWalls () {

        Array<RectangleMapObject> walls = map.getLayers().get(MAP_WALL).getObjects().getByType(RectangleMapObject.class);
        for (RectangleMapObject rObject : new Array.ArrayIterator<RectangleMapObject>(walls)) {
            Rectangle rectangle = rObject.getRectangle();
            mapTiles.add(bodyFactory.makeBoxPolyBody(
                    new Vector2(rectangle.getX() + rectangle.getWidth() / 2, rectangle.getY() + rectangle.getHeight() / 2), // position
                    new Vector2(rectangle.getWidth() / 2, rectangle.getHeight() / 2), // size
                    BodyDef.BodyType.StaticBody, world,4, false));

            Entity entity = engine.createEntity();
            BodyComponent body = engine.createComponent(BodyComponent.class);
            TransformComponent position = engine.createComponent(TransformComponent.class);
            TextureComponent texture = engine.createComponent(TextureComponent.class);
            TypeComponent type = engine.createComponent(TypeComponent.class);

            body.body = bodyFactory.makeBoxPolyBody(
                    new Vector2(rectangle.getX() + rectangle.getWidth() / 2, rectangle.getY() + rectangle.getHeight() / 2), // position
                    new Vector2(rectangle.getWidth() / 2, rectangle.getHeight() / 2), // size
                    BodyDef.BodyType.StaticBody, world,4, false);

            objects.add(entity);
        }

    }

    public TiledMap getMap() {
        return map;
    }

    public Array<Body> getMapTiles() {
        return mapTiles;
    }

    public Array<Body> getGroundTiles() {
        return mapGround;
    }

    public Array<Body> getDoors() {
        return mapDoors;
    }

    @Override
    public void dispose() {
        map.dispose();
    }
}
