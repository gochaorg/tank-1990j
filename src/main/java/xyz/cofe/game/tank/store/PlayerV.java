package xyz.cofe.game.tank.store;

import xyz.cofe.game.tank.geom.Point;
import xyz.cofe.game.tank.unt.Direction;
import xyz.cofe.game.tank.unt.Figure;
import xyz.cofe.game.tank.unt.Player;
import xyz.cofe.game.tank.unt.PlayerState;

import java.util.function.Supplier;

public abstract class PlayerV<CLS extends Player<?>> extends OBJ<CLS> {
    public PlayerV(Class<CLS> cls, Supplier<CLS> newInst) {
        super(cls, newInst);
    }

    public final ObjKey<Point> location = new ObjKey<Point>("location",
        Figure::getLocation,
        (ob,v) -> {
            ob.setLocation(v);
            return ob;
        }
    );

    public final StringKey direction = new StringKey("direction",
        ob -> ob.direction().name(),
        (ob,v)->{
            ob.direction(Direction.valueOf(v));
            return ob;
        }
    );

    public final StringKey playerState = new StringKey("playerState",
        ob -> ob.getPlayerState().name(),
        (ob,v)->{
            ob.setPlayerState(PlayerState.valueOf(v));
            return ob;
        }
    );
}
