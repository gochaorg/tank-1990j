package xyz.cofe.game.tank.unt;

import xyz.cofe.game.tank.sprite.Sprite;
import xyz.cofe.game.tank.sprite.SpritesData;

/**
 * Стальная стена
 */
public class Steel extends LevelBrick<Steel> {
    /**
     * Конструктор
     */
    public Steel(){}

    /**
     * Конструктор копирования
     * @param sample образец
     */
    public Steel(Steel sample){
        super(sample);
    }

    /**
     * Конструктор копирования
     * @param sample образец
     */
    public Steel(Figure<?> sample){
        super(sample);
    }

    /**
     * Клонирование объекта
     * @return клон
     */
    public Steel clone(){
        return new Steel(this);
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
