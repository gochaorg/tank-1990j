package xyz.cofe.game.tank.unt;

import xyz.cofe.game.tank.sprite.Sprite;
import xyz.cofe.game.tank.sprite.SpritesData;

/**
 * Куст
 */
public class Bush extends LevelBrick<Bush> {
    public Bush(){}
    public Bush(Figura<?> sample){super(sample);}
    public Bush(LevelBrick<?> sample){
        super(sample);
    }
    public Bush(Bush sample){
        super(sample);
    }
    public Bush clone(){ return new Bush(this); }

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
