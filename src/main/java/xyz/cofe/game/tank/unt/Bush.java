package xyz.cofe.game.tank.unt;

import xyz.cofe.game.tank.sprite.Sprite;
import xyz.cofe.game.tank.sprite.SpritesData;

/**
 * Куст
 */
public class Bush extends LevelBrick<Bush> {
    /**
     * Конструктор
     */
    public Bush(){}

    /**
     * Конструктор копирования
     * @param sample образец
     */
    public Bush(Figure<?> sample){super(sample);}

    /**
     * Конструктор копирования
     * @param sample образец
     */
    public Bush(LevelBrick<?> sample){
        super(sample);
    }

    /**
     * Конструктор копирования
     * @param sample образец
     */
    public Bush(Bush sample){
        super(sample);
    }

    /**
     * Клонирование объекта
     * @return клон
     */
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
