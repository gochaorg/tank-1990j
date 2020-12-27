package xyz.cofe.game.tank;

import java.awt.*;

/**
 * Отображение объекта
 */
public class Figura implements Drawing {
    public Figura(SpriteLine spriteLine){
        if( spriteLine==null )throw new IllegalArgumentException( "spriteLine==null" );
        this.spriteLine = spriteLine;
    }
    public Figura(SpriteLine spriteLine, double left, double top){
        if( spriteLine==null )throw new IllegalArgumentException( "spriteLine==null" );
        this.spriteLine = spriteLine;
        this.left = left;
        this.top = top;
    }
    public Figura( Figura sample ){
        if( sample==null )throw new IllegalArgumentException( "sample==null" );
        this.spriteLine = sample.getSpriteLine().clone();
        this.left = sample.getLeft();
        this.top = sample.getTop();
    }
    public Figura clone(){
        return new Figura(this);
    }

    //region left : double - Верхний левый угол
    protected double left;
    public double left(){ return left; }
    public double getLeft(){
        return left;
    }
    public void setLeft(double left){
        this.left = left;
    }
    public Figura left(double left){
        setLeft(left);
        return this;
    }
    //endregion
    //region top : double - Верхний левый угол
    protected double top;
    public double top(){ return top; }
    public double getTop(){
        return top;
    }
    public void setTop(double top){
        this.top = top;
    }
    public Figura top(double top){
        setTop(top);
        return this;
    }
    //endregion
    //region location(left,top)
    public Figura location(double left, double top ){
        setLeft(left);
        setTop(top);
        return this;
    }
    //endregion
    //region spriteLine - Последовательность кадров
    protected final SpriteLine spriteLine;
    public SpriteLine getSpriteLine(){
        return spriteLine;
    }
    //endregion

    @Override
    public void draw(Graphics2D gs){
        if( gs==null )throw new IllegalArgumentException( "gs==null" );
        spriteLine.draw(gs, getLeft(), getTop());
    }
}
