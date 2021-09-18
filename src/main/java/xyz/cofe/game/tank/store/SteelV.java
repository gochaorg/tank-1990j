package xyz.cofe.game.tank.store;

import xyz.cofe.game.tank.unt.Slide;
import xyz.cofe.game.tank.unt.Steel;

public class SteelV extends LevelBrickV<Steel> {
    public static final SteelV instance = new SteelV();
    private SteelV() {
        super(Steel.class, Steel::new);
    }
}
