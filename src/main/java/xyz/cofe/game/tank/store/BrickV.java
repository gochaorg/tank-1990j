package xyz.cofe.game.tank.store;

import xyz.cofe.game.tank.unt.Brick;

public class BrickV extends LevelBrickV<Brick> implements ObjectMapper {
    public static final BrickV instance = new BrickV();
    public BrickV() {
        super(Brick.class, Brick::new);
    }
    @Override public void registry(ObjectMappers om) {
        ObjectMappers.mappers.put(instance.clazz, instance);
    }
}
