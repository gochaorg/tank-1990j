package xyz.cofe.game.tank.unt;

import xyz.cofe.game.tank.sprite.Sprite;
import xyz.cofe.game.tank.sprite.SpritesData;

/**
 * Кирпичная стена
 */
public class Brick extends LevelBrick<Brick> {
    /**
     * Конструктор
     */
    public Brick(){}

    /**
     * Конструктор копирования
     * @param sample образец
     */
    public Brick(Figure<?> sample){super(sample);}

    /**
     * Конструктор копирования
     * @param sample образец
     */
    public Brick(LevelBrick<?> sample){
        super(sample);
    }

    /**
     * Конструктор копирования
     * @param sample образец
     */
    public Brick(Brick sample){
        super(sample);
    }

    /**
     * Клонирование
     * @return клон
     */
    public Brick clone(){ return new Brick(this); }

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
