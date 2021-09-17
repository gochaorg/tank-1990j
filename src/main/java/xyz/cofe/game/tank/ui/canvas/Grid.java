package xyz.cofe.game.tank.ui.canvas;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.*;

import xyz.cofe.fn.Tuple2;
import xyz.cofe.game.tank.Drawing;
import xyz.cofe.game.tank.geom.Point;
import xyz.cofe.game.tank.geom.Rect;

public class Grid extends CanvasHost<Grid> implements Drawing {
    //region visible : boolean
    private boolean visible = true;
    public boolean isVisible(){ return visible; }
    public void setVisible(boolean v){ visible = v; }
    //endregion
    //region regularMarks : NavigableMap<Double, LineMark>
    private final NavigableMap<Double, LineMark> regularMarks = new TreeMap<>();
    {
        regularMarks.put(32d,
            new LineMark()
                .color(Color.lightGray)
                .length(1.0f)
                .width(1f)
                .strokePattern(1f,2f)
        );

        regularMarks.put(32d*4,
            new LineMark()
                .color(Color.gray)
                .length(1.0f)
                .width(1f)
                .strokePattern(4f, 2f)
        );
    }
    public NavigableMap<Double, LineMark> regularMarks(){ return regularMarks; }
    //endregion

    /**
     * Возвращает ближайшую точку на сетке от указанной
     * @param p точка
     * @return ближайшая к ней точка
     */
    public Point nearestPoint( Point p ){
        if( p==null )throw new IllegalArgumentException( "p==null" );
        List<Point> points = new ArrayList<>();
        for( var step : regularMarks().keySet() ){
            var x0 = ((int)(p.x() / step)) * step;
            var x1 = x0 + step;
            var y0 = ((int)(p.y() / step)) * step;
            var y1 = y0 + step;
            points.add(Point.of(x0,y0));
            points.add(Point.of(x0,y1));
            points.add(Point.of(x1,y0));
            points.add(Point.of(x1,y1));
        }
        var pdist = new TreeMap<Double,Point>();
        for( var pt : points ){
            var d = pt.distance(p);
            pdist.put(d, pt);
        }
        if( pdist.isEmpty() )return Point.of(0,0);
        return pdist.firstEntry().getValue();
    }

    private final Map<Double, Tuple2<LineMark, Rect>> hmarkers = new HashMap<>();
    private final Map<Double, Tuple2<LineMark, Rect>> vmarkers = new HashMap<>();
    private final Line2D line = new Line2D.Double();

    @Override
    public void draw(Graphics2D gs){
        if( gs==null )return;

        hmarkers.clear();
        vmarkers.clear();

        var cmpt = component();
        if( cmpt==null )return;

        regularMarks.forEach( (step,mark) -> {
            if( step<=0 )return;

            var vport = viewPortRect(step);
            var p = Tuple2.of(mark,vport);
            for(double x = vport.left(); x<vport.right(); x+=step ){
                vmarkers.put(x,p);
            }
            for(double y = vport.top(); y<vport.bottom(); y+=step ){
                hmarkers.put(y,p);
            }
        });

        hmarkers.forEach( (y,markp) -> {
            var yOut = translateY(y);
            var rect = markp.b();
            var mark = markp.a();

            gs.setPaint(mark.color());
            gs.setStroke(mark.stroke());

            line.setLine(
                0, yOut,
                cmpt.getWidth(), yOut);
            gs.draw(line);
        });

        vmarkers.forEach( (x,markp) -> {
            var xOut = translateX(x);
            var rect = markp.b();
            var mark = markp.a();

            gs.setPaint(mark.color());
            gs.setStroke(mark.stroke());

            line.setLine(
                xOut, 0,
                xOut, cmpt.getHeight());
            gs.draw(line);
        });
    }
}
