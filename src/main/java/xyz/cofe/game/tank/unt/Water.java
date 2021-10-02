package xyz.cofe.game.tank.unt;

import xyz.cofe.game.tank.sprite.Sprite;
import xyz.cofe.game.tank.sprite.SpritesData;

/**
 * Вода
 */
public class Water extends LevelBrick<Water> {
    public Water(){}
    public Water(Figure<?> sample){super(sample);}
    public Water(LevelBrick<?> sample){
        super(sample);
    }
    public Water(Water sample){
        super(sample);
    }
    public Water clone(){ return new Water(this); }

    private static final Sprite sprite;
    static {
        var sd = SpritesData.lvl_water;
        if( sd.images().size()<1 )throw new IllegalStateException("can't init, no image of water");
        sprite = new Sprite(sd.images().get(0));
    }

    @Override
    public Sprite sprite(){
        return sprite;
    }
}
