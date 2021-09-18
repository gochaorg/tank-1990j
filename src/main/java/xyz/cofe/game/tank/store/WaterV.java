package xyz.cofe.game.tank.store;

import xyz.cofe.game.tank.unt.Steel;
import xyz.cofe.game.tank.unt.Water;

public class WaterV extends LevelBrickV<Water> {
    public static final WaterV instance = new WaterV();
    private WaterV() {
        super(Water.class, Water::new);
    }
}
