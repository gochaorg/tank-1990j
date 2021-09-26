package xyz.cofe.game.tank.ui.canvas;

import java.awt.Color;
import java.awt.Graphics2D;

public class HRuler extends Ruler<HRuler> {
    public void draw(Graphics2D gs){
        if( gs==null )return;
        var component = component();
        if( component==null )return;

        double viewWidth = component.getWidth();
        double viewHeight = component.getHeight();

        gs.setStroke(stroke());

        gs.setPaint(background());
        gs.fillRect(0,0,(int)viewWidth, size());

        gs.setPaint(Color.darkGray);
        gs.drawRect(0,0,(int)viewWidth, size());

        var scale = scale();

        regularMarks().forEach( (step,mark) -> {
            if( step<=0 )return;
            gs.setPaint(mark.color());
            gs.setStroke(mark.stroke());
            gs.setFont(font());

            var vpRect = viewPortRect(step);
            double xBegin = vpRect.left();
            double xEnd = vpRect.right();

            for( double x = xBegin; x<xEnd; x += step*scale ){
                int xOut = (int)translateX(x);
                gs.drawLine(
                    xOut,
                    (int)(size() - size() * mark.length()),
                    xOut,
                    size());

                if( mark.annotate() ){
                    String txt = Double.toString(x/scale);
                    if( txt.endsWith(".0") ){
                        txt = txt.substring(0,txt.length()-2);
                    }

                    var rect = gs.getFontMetrics().getStringBounds(txt, gs);

                    gs.drawString(
                        txt,
                        (int)(xOut - rect.getWidth()/2),
                        //(int)rect.getHeight()
                        -(int)rect.getY()
                    );
                }
            }
        });
    }
}
