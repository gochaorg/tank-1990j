package xyz.cofe.game.tank.unt;

import xyz.cofe.game.tank.geom.Point;

/**
 * Уведомление о перемещении
 * @param <SELF> фигура
 */
public class FiguraMoved<SELF extends Figure<SELF>> extends FiguraEvent<SELF> {
    /**
     * Конструктор
     * @param self фигура
     * @param from откуда передвинута фигура
     * @param to куда была передвинута фигура
     */
    public FiguraMoved(SELF self, Point from, Point to){
        super(self);
        if( from==null )throw new IllegalArgumentException( "from==null" );
        if( to==null )throw new IllegalArgumentException( "to==null" );
        this.from = from;
        this.to = to;
    }

    protected Point from;

    /**
     * откуда передвинута фигура
     * @return откуда передвинута фигура
     */
    public Point getFrom(){ return from; }

    protected Point to;

    /**
     * куда была передвинута фигура
     * @return куда была передвинута фигура
     */
    public Point getTo(){ return to; }
}
