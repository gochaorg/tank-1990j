package xyz.cofe.game.tank.stat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Series {
    protected double sum = 0;
    protected List<Double> values = new ArrayList<>();
    public void add( double v ){
        values.add(v);
        sum += v;
    }

    public int count(){ return values.size(); }
    public double sum(){ return sum; }
    public Optional<Double> avg(){
        if( count()<1 )return Optional.empty();
        return Optional.of( sum() / count() );
    }
}
