package xyz.cofe.game.tank.store;

import xyz.cofe.game.tank.unt.PlayerTwo;

import java.util.function.Supplier;

public class PlayerTwoV extends PlayerV<PlayerTwo> implements ObjectMapper {
    public static final PlayerTwoV instance = new PlayerTwoV();
    @Override public void registry(ObjectMappers om) {
        ObjectMappers.mappers.put(instance.clazz, instance);
    }

    public PlayerTwoV() {
        super(PlayerTwo.class, PlayerTwo::new);
    }
}
