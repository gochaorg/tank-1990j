package xyz.cofe.game.tank.unt;

import xyz.cofe.game.tank.sprite.Sprite;
import xyz.cofe.game.tank.sprite.SpritesData;

/**
 * Куст
 */
public class Bush extends LevelBrick<Brick> {
    private static final Sprite sprite;
    static {
        var sd = SpritesData.lvl_bush;
        if( sd.images().size()<1 )throw new IllegalStateException("can't init, no image of bush");
        sprite = new Sprite(sd.images().get(0));
    }

    @Override
    public Sprite sprite(){
        return sprite;
    }
}
