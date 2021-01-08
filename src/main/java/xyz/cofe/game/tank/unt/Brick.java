package xyz.cofe.game.tank.unt;

import xyz.cofe.game.tank.sprite.Sprite;
import xyz.cofe.game.tank.sprite.SpritesData;

/**
 * Кирпичная стена
 */
public class Brick extends LevelBrick<Brick> {
    private static final Sprite sprite;
    static {
        var sd = SpritesData.lvl_brick;
        if( sd.images().size()<1 )throw new IllegalStateException("can't init brickSprite, no image of brick");
        sprite = new Sprite(sd.images().get(0));
    }

    @Override
    public Sprite sprite(){
        return sprite;
    }
}
