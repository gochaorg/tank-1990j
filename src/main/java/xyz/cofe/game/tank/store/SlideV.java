package xyz.cofe.game.tank.store;

import xyz.cofe.game.tank.unt.Bush;
import xyz.cofe.game.tank.unt.Slide;

public class SlideV extends LevelBrickV<Slide> {
    public static final SlideV instance = new SlideV();
    private SlideV() {
        super(Slide.class, Slide::new);
    }
}
