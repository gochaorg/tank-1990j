package xyz.cofe.game.tank.job;

import xyz.cofe.game.tank.GameUnit;
import xyz.cofe.game.tank.geom.Rect;
import xyz.cofe.game.tank.unt.Figura;

public class CollisionDetected<UNT extends GameUnit<UNT>> implements JobEvent<Moving<UNT>> {
    public CollisionDetected(Moving<UNT> job, Figura<?> withObject, Rect rect){
        if( job==null )throw new IllegalArgumentException( "job==null" );
        if( withObject==null )throw new IllegalArgumentException( "withObject==null" );
        if( rect==null )throw new IllegalArgumentException( "rect==null" );
        this.job = job;
        this.withObject = withObject;
        this.rect = rect;
    }

    protected final Moving<UNT> job;

    @Override
    public Moving<UNT> getJob(){
        return job;
    }

    public UNT getGameUnit(){ return job.getGameUnit(); }

    protected final Figura<?> withObject;
    public Figura<?> getWithFigura(){ return withObject; }

    protected final Rect rect;
    public Rect getRect(){ return rect; }
}
