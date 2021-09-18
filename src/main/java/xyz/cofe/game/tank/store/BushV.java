package xyz.cofe.game.tank.store;

import xyz.cofe.game.tank.unt.Brick;
import xyz.cofe.game.tank.unt.Bush;

import java.util.function.Supplier;

public class BushV extends LevelBrickV<Bush> {
    public static final BushV instance = new BushV();
    private BushV() {
        super(Bush.class, Bush::new);
    }
}
