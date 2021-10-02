package xyz.cofe.game.tank.unt;

/**
 * Событие фигуры
 * @param <SELF> фигура
 */
public class FiguraEvent<SELF extends Figure<SELF>> {
    public FiguraEvent(SELF self){
        this.figura = self;
    }

    /**
     * фигура с которой связано событие
     */
    protected SELF figura;

    /**
     * Возвращает фигуру с которой связано событие
     * @return фигура
     */
    public SELF getFigura(){ return figura; }
}
