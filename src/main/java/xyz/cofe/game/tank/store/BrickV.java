package xyz.cofe.game.tank.store;

import xyz.cofe.game.tank.unt.Brick;

public class BrickV extends LevelBrickV<Brick> {
    public static final BrickV instance = new BrickV();
    private BrickV() {
        super(Brick.class, Brick::new);
    }
}
