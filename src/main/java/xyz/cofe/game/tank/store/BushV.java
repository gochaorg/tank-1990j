package xyz.cofe.game.tank.store;

import xyz.cofe.game.tank.unt.Brick;
import xyz.cofe.game.tank.unt.Bush;

import java.util.function.Supplier;

public class BushV extends LevelBrickV<Bush> implements ObjectMapper {
    public static final BushV instance = new BushV();
    @Override public void registry(ObjectMappers om) {
        ObjectMappers.mappers.put(instance.clazz, instance);
    }

    public BushV() {
        super(Bush.class, Bush::new);
    }
}
