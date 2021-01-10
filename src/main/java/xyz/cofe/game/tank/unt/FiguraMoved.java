package xyz.cofe.game.tank.unt;

import xyz.cofe.game.tank.geom.Point;

public class FiguraMoved<SELF extends Figura<SELF>> extends FiguraEvent<SELF> {
    public FiguraMoved(SELF self, Point from, Point to){
        super(self);
        if( from==null )throw new IllegalArgumentException( "from==null" );
        if( to==null )throw new IllegalArgumentException( "to==null" );
        this.from = from;
        this.to = to;
    }

    protected Point from;
    public Point getFrom(){ return from; }

    protected Point to;
    public Point getTo(){ return to; }
}
