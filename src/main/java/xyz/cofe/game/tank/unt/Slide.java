package xyz.cofe.game.tank.unt;

import xyz.cofe.game.tank.Sprite;
import xyz.cofe.game.tank.SpritesData;

/**
 * Лед/скользящаяя поверхность
 */
public class Slide extends LevelBrick<Brick> {
    private static final Sprite sprite;
    static {
        var sd = SpritesData.lvl_slide;
        if( sd.images().size()<1 )throw new IllegalStateException("can't init, no image of slide");
        sprite = new Sprite(sd.images().get(0));
    }

    @Override
    public Sprite sprite(){
        return sprite;
    }
}
