package xyz.cofe.game.tank.ui;

import xyz.cofe.game.tank.geom.Point;

import java.awt.Component;
import java.awt.event.MouseEvent;

/**
 * События мыши
 */
public class MouseEv implements Point {
    public MouseEv(){}
    public MouseEv(double x, double y){
        this.x = x;
        this.y = y;
    }
    public MouseEv(MouseEvent ev){
        this( ev, null );
    }
    public MouseEv(MouseEvent ev, Component component){
        if( ev==null )throw new IllegalArgumentException( "ev==null" );
        this.x = ev.getX();
        this.y = ev.getY();
        button = ev.getButton();
        shift = ev.isShiftDown();
        control = ev.isControlDown();
        alt = ev.isAltDown();
        meta = ev.isMetaDown();
        clickCount = ev.getClickCount();
        this.component = component;
    }

    protected int button;
    public int getButton(){ return button; }

    public boolean isLeftButton(){ return button == MouseEvent.BUTTON1; }
    public boolean isRightButton(){ return button == MouseEvent.BUTTON2; }

    protected boolean shift;
    public boolean isShift(){ return shift; }

    protected boolean control;
    public boolean isControl(){ return control; }

    protected boolean alt;
    public boolean isAlt(){ return alt; }

    protected boolean meta;
    public boolean isMeta(){ return meta; }

    protected int clickCount;
    public int getClickCount(){ return clickCount; }

    protected Component component;
    public Component getComponent(){ return component; }

    protected double x;
    @Override public double x() { return x; }
    public MouseEv x( double v ){
        this.x = v;
        return this;
    }

    protected double y;
    @Override public double y() { return y; }
    public MouseEv y( double v ){
        this.y = v;
        return this;
    }

    public MouseEv shift( Point p ){
        if( p==null )throw new IllegalArgumentException( "p==null" );
        this.x += p.x();
        this.y += p.y();
        return this;
    }
}
