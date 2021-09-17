package xyz.cofe.game.tank.ui.canvas;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;

public class LineMark {
    private float width = 1f;
    public LineMark width( float w ){
        if( w<0.1f )throw new IllegalArgumentException( "w<0.1f" );
        width = w;
        stroke = null;
        return this;
    }
    public float width(){
        return width;
    }

    private Stroke stroke;
    public Stroke stroke(){
        if( stroke!=null )return stroke;
        if( strokePattern==null || strokePattern.length<2 ){
            stroke = new BasicStroke(width);
        } else {
            stroke = new BasicStroke(
                width,
                BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_BEVEL,
                1f,
                strokePattern,
                0f
                );
        }
        return stroke;
    }

    private float[] strokePattern;
    public float[] strokePattern(){ return strokePattern; }
    public LineMark strokePattern( float ... pattern ){
        strokePattern = pattern;
        return this;
    }

    private double length = 0.5;
    public LineMark length( double l ){
        if( l<0 )throw new IllegalArgumentException( "l<0" );
        if( l>1 )throw new IllegalArgumentException( "l>1" );
        length = l;
        return this;
    }
    public double length(){ return length; }

    private Color color;
    public Color color(){ return color; }
    public LineMark color( Color col ){ color = col; return this; }

    private boolean annotate = false;
    public LineMark annotate( boolean v ){ annotate=v; return this; }
    public boolean annotate(){ return annotate; }
}
