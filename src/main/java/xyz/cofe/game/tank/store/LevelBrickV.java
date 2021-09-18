package xyz.cofe.game.tank.store;

import xyz.cofe.game.tank.geom.Point;
import xyz.cofe.game.tank.unt.Figura;
import xyz.cofe.game.tank.unt.LevelBrick;

import java.util.function.Supplier;

public abstract class LevelBrickV<B extends LevelBrick<B>> extends OBJ<B> {
    public LevelBrickV(Class<B> cls, Supplier<B> newInst) {
        super(cls, newInst);
    }

    static {
        // init nested type view
        var x = new Object[]{ PointV.instance };
    }

    public final ObjKey<Point> location = new ObjKey<Point>("location", Figura::getLocation, Figura::location);
    public final IntKey state = new IntKey("state", LevelBrick::state, LevelBrick::state);
}
