package xyz.cofe.game.tank;

public class MutableRect implements Rect {
    public MutableRect(){
    }

    public MutableRect(Rect sample){
        if( sample==null )throw new IllegalArgumentException( "sample==null" );
        left = Math.min(sample.left(), sample.right());
        right = Math.max(sample.left(), sample.right());
        top = Math.min(sample.top(), sample.bottom());
        bottom = Math.max(sample.top(), sample.bottom());
    }

    protected double left;
    protected double top;
    protected double right;
    protected double bottom;

    public MutableRect set( double x0, double y0, double x1, double y1 ){
        left = Math.min(x0,x1);
        right = Math.max(x0,x1);
        top = Math.min(y0,y1);
        bottom = Math.max(y0,y1);
        return this;
    }

    public MutableRect size( double width, double height ){
        var l = Math.min(left, right);
        var t = Math.min(top, bottom);
        var r = l + Math.max(width,0);
        var b = t + Math.max(height,0);
        left = l;
        right = r;
        top = t;
        bottom = b;
        return this;
    }

    public MutableRect location( double x0, double y0 ){
        var w = Math.abs(left-right);
        var h = Math.abs(top-bottom);
        left = x0;
        top = y0;
        right = left + w;
        bottom = top + h;
        return this;
    }

    @Override
    public double left(){
        return left;
    }

    @Override
    public double top(){
        return top;
    }

    @Override
    public double right(){
        return right;
    }

    @Override
    public double bottom(){
        return bottom;
    }

    @Override
    public double width(){
        return Math.abs(left - right);
    }

    @Override
    public double height(){
        return Math.abs(top - bottom);
    }
}
