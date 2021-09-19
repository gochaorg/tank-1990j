package xyz.cofe.game.tank.store;

import xyz.cofe.game.tank.unt.Steel;
import xyz.cofe.game.tank.unt.Water;

public class WaterV extends LevelBrickV<Water> implements ObjectMapper {
    public static final WaterV instance = new WaterV();
    public WaterV() {
        super(Water.class, Water::new);
    }

    @Override
    public void registry(ObjectMappers om) {
        ObjectMappers.mappers.put(instance.clazz, instance);
    }
}
