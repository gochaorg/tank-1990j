package xyz.cofe.game.tank.ui.cmd;

import xyz.cofe.game.tank.geom.Point;
import xyz.cofe.game.tank.ui.OriginProperty;

import java.util.function.Supplier;

public class ResetViewCommand implements Runnable {
    //region origin : Supplier<OriginProperty>
    private Supplier<OriginProperty> origin;
    public OriginProperty getOrigin(){
        return origin!=null ? origin.get() : null;
    }
    public void setOrigin(OriginProperty origin){
        this.origin = origin!=null ? ()->origin : null;
    }
    public void setOrigin(Supplier<OriginProperty> origin){
        this.origin = origin;
    }
    public ResetViewCommand origin(Supplier<OriginProperty> origin){
        setOrigin(origin);
        return this;
    }
    public ResetViewCommand origin(OriginProperty origin){
        setOrigin(origin);
        return this;
    }
    //endregion

    @Override
    public void run() {
        var org = getOrigin();
        if( org!=null ){
            org.setOrigin(Point.of(0,0));
            org.setScale(1.0);
        }
    }
}
