package com.ema.game;

import com.badlogic.ashley.core.ComponentMapper;
import com.ema.game.components.BodyComponent;
import com.ema.game.components.CollisionComponent;
import com.ema.game.components.CombatComponent;
import com.ema.game.components.EnemyComponent;
import com.ema.game.components.MapExitComponent;
import com.ema.game.components.MapGroundComponent;
import com.ema.game.components.MapObjectComponent;
import com.ema.game.components.MovementComponent;
import com.ema.game.components.PlayerComponent;
import com.ema.game.components.RogueComponent;
import com.ema.game.components.TextureComponent;
import com.ema.game.components.TransformComponent;
import com.ema.game.components.TypeComponent;
import com.ema.game.components.WarriorComponent;

public class ComponentMapperWrapper {
    private static ComponentMapperWrapper components;

    public ComponentMapper<BodyComponent> bodyMapper;
    public ComponentMapper<CollisionComponent> collisionMapper;
    public ComponentMapper<CombatComponent> combatMapper;
    public ComponentMapper<EnemyComponent> enemyMapper;
    public ComponentMapper<MapGroundComponent> mapGroundMapper;
    public ComponentMapper<MapObjectComponent> mapObjectMapper;
    public ComponentMapper<MovementComponent> movementMapper;
    public ComponentMapper<PlayerComponent> playerMapper;
    public ComponentMapper<TextureComponent> textureMapper;
    public ComponentMapper<TransformComponent> transformMapper;
    public ComponentMapper<TypeComponent> typeMapper;
    public ComponentMapper<WarriorComponent> warriorMapper;
    public ComponentMapper<RogueComponent> rogueMapper;
    public ComponentMapper<MapExitComponent> mapExitMapper;


    private ComponentMapperWrapper() {
        bodyMapper = ComponentMapper.getFor(BodyComponent.class);
        collisionMapper = ComponentMapper.getFor(CollisionComponent.class);
        combatMapper = ComponentMapper.getFor(CombatComponent.class);
        enemyMapper = ComponentMapper.getFor(EnemyComponent.class);
        mapGroundMapper = ComponentMapper.getFor(MapGroundComponent.class);
        mapObjectMapper = ComponentMapper.getFor(MapObjectComponent.class);
        movementMapper = ComponentMapper.getFor(MovementComponent.class);
        playerMapper = ComponentMapper.getFor(PlayerComponent.class);
        textureMapper = ComponentMapper.getFor(TextureComponent.class);
        transformMapper = ComponentMapper.getFor(TransformComponent.class);
        typeMapper = ComponentMapper.getFor(TypeComponent.class);
        warriorMapper = ComponentMapper.getFor(WarriorComponent.class);
        rogueMapper = ComponentMapper.getFor(RogueComponent.class);
        mapExitMapper = ComponentMapper.getFor(MapExitComponent.class);
    }

    public static ComponentMapperWrapper getInstance() {
        if (components == null) {
            components = new ComponentMapperWrapper();
        }
        return components;
    }

    public void updateComponents() {
        components = new ComponentMapperWrapper();
    }
}
