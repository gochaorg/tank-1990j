package xyz.cofe.game.tank.unt;

import xyz.cofe.game.tank.sprite.Sprite;
import xyz.cofe.game.tank.sprite.SpritesData;

/**
 * Лед/скользящаяя поверхность
 */
public class Slide extends LevelBrick<Slide> {
    /**
     * Конструктор
     */
    public Slide(){}

    /**
     * Конструктор копирования
     * @param sample образец
     */
    public Slide(Figure<?> sample){super(sample);}

    /**
     * Конструктор копирования
     * @param sample образец
     */
    public Slide(LevelBrick<?> sample){
        super(sample);
    }

    /**
     * Конструктор копирования
     * @param sample образец
     */
    public Slide(Slide sample){
        super(sample);
    }

    /**
     * Клонирование объекта
     * @return клон
     */
    public Slide clone(){ return new Slide(this); }
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
