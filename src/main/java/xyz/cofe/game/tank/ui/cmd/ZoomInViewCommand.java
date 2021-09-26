package xyz.cofe.game.tank.ui.cmd;

import xyz.cofe.game.tank.ui.OriginProperty;

import java.util.function.Supplier;

public class ZoomInViewCommand implements Runnable {
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
    public ZoomInViewCommand origin(Supplier<OriginProperty> origin){
        setOrigin(origin);
        return this;
    }
    public ZoomInViewCommand origin(OriginProperty origin){
        setOrigin(origin);
        return this;
    }
    //endregion

    @Override
    public void run() {
        var org = getOrigin();
        if( org!=null ){
            org.setScale(org.getScale()*0.5);
        }
    }
}
