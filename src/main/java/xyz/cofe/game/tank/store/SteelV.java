package xyz.cofe.game.tank.store;

import xyz.cofe.game.tank.unt.Slide;
import xyz.cofe.game.tank.unt.Steel;

public class SteelV extends LevelBrickV<Steel> implements ObjectMapper {
    public static final SteelV instance = new SteelV();
    @Override public void registry(ObjectMappers om) {
        ObjectMappers.mappers.put(instance.clazz, instance);
    }

    public SteelV() {
        super(Steel.class, Steel::new);
    }
}
