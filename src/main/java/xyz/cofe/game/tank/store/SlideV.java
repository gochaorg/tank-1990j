package xyz.cofe.game.tank.store;

import xyz.cofe.game.tank.unt.Bush;
import xyz.cofe.game.tank.unt.Slide;

public class SlideV extends LevelBrickV<Slide> implements ObjectMapper {
    public static final SlideV instance = new SlideV();
    @Override public void registry(ObjectMappers om) {
        ObjectMappers.mappers.put(instance.clazz, instance);
    }

    public SlideV() {
        super(Slide.class, Slide::new);
    }
}
