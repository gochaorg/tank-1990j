package xyz.cofe.game.tank.ui.canvas;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.NavigableMap;
import java.util.TreeMap;
import xyz.cofe.game.tank.Drawing;
import xyz.cofe.gui.swing.color.ColorModificator;

public abstract class Ruler<SELF extends Ruler<SELF>> extends CanvasHost<SELF> implements Drawing {
    //region visible : boolean
    private boolean visible = true;
    public boolean isVisible(){ return visible; }
    public void setVisible(boolean v){ visible = v; }
    //endregion

    //region font : Font
    private Font ruleFont = new Font(Font.SANS_SERIF, Font.PLAIN, 10);
    public Font font(){ return ruleFont; }
    public SELF font( Font font ){
        if( font==null )throw new IllegalArgumentException( "font==null" );
        ruleFont = font;
        //noinspection unchecked
        return (SELF)this;
    }
    //endregion
    //region background : Color
    private Color ruleBg;
    {
        ColorModificator cm = new ColorModificator();
        ruleBg = cm.bright(0.99f).hue(45).saturation(0.2f).apply(Color.yellow);
    }
    public Color background(){ return ruleBg; }
    public SELF background(Color color){
        if( color==null )throw new IllegalArgumentException( "color==null" );
        ruleBg = color;
        //noinspection unchecked
        return (SELF)this;
    }
    //endregion
    //region stroke : Stroke
    private Stroke ruleStroke = new BasicStroke(1f);
    public Stroke stroke(){ return ruleStroke; }
    public SELF stroke(Stroke s){
        if( s==null )throw new IllegalArgumentException( "s==null" );
        ruleStroke = s;
        //noinspection unchecked
        return (SELF) this;
    }
    //endregion
    //region size : int
    private int size = 16;
    public int size(){ return size; }
    public SELF size(int s){
        if( s<1 )throw new IllegalArgumentException( "s<1" );
        size = s;
        //noinspection unchecked
        return (SELF) this;
    }
    //endregion
    //region regularMarks : NavigableMap<Double, LineMark>
    private final NavigableMap<Double, LineMark> regularMarks = new TreeMap<>();
    {
        regularMarks.put(32d, new LineMark().color(Color.gray).length(0.3).width(1f) );
        regularMarks.put(32d*8d, new LineMark().color(Color.black).length(0.5).width(1f).annotate(true) );
    }
    public NavigableMap<Double, LineMark> regularMarks(){ return regularMarks; }
    //endregion

    public abstract void draw(Graphics2D gs);
}
