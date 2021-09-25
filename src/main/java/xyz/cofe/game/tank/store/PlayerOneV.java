package xyz.cofe.game.tank.store;

import xyz.cofe.game.tank.unt.Player;
import xyz.cofe.game.tank.unt.PlayerOne;

import java.util.function.Supplier;

public class PlayerOneV extends PlayerV<PlayerOne> implements ObjectMapper {
    public static final PlayerOneV instance = new PlayerOneV();
    @Override public void registry(ObjectMappers om) {
        ObjectMappers.mappers.put(instance.clazz, instance);
    }

    public PlayerOneV() {
        super(PlayerOne.class, PlayerOne::new);
    }
}
