package xyz.cofe.game.tank.unt;

import xyz.cofe.game.tank.sprite.Sprite;
import xyz.cofe.game.tank.sprite.SpritesData;

/**
 * Стальная стена
 */
public class Steel extends LevelBrick<Steel> {
    public Steel(){}
    public Steel(Steel sample){
        super(sample);
    }
    private static final Sprite sprite;
    static {
        var sd = SpritesData.lvl_white;
        if( sd.images().size()<1 )throw new IllegalStateException("can't init, no image of steel");
        sprite = new Sprite(sd.images().get(0));
    }

    @Override
    public Sprite sprite(){
        return sprite;
    }
}
