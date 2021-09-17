package xyz.cofe.game.tank.ui.canvas;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class VRuler extends Ruler<VRuler> {
    @Override
    public void draw(Graphics2D gs){
        if( gs==null )return;

        var component = component();
        if( component==null )return;

        double viewWidth = component.getWidth();
        double viewHeight = component.getHeight();

        gs.setStroke(stroke());

        gs.setPaint(background());
        gs.fillRect(0,0,(int)size(), (int)viewHeight);

        gs.setPaint(Color.darkGray);
        gs.drawRect(0,0,(int)size(), (int)viewHeight);

        regularMarks().forEach( (step,mark) -> {
            if( step<=0 )return;
            gs.setPaint(mark.color());
            gs.setStroke(mark.stroke());
            gs.setFont(font());

            var vpRect = viewPortRect(step);

            double yBegin = vpRect.top();
            double yEnd = vpRect.bottom();

            for( double y = yBegin; y<yEnd; y += step ){
                int yOut = (int)translateY(y);

                gs.drawLine(
                    (int)(size() - size() * mark.length()),
                    yOut,
                    size(),
                    yOut);

                if( mark.annotate() ){
                    var transformSaved = (AffineTransform)gs.getTransform().clone();

                    String txt = Double.toString(y);
                    if( txt.endsWith(".0") ){
                        txt = txt.substring(0,txt.length()-2);
                    }

                    var rect = gs.getFontMetrics().getStringBounds(txt, gs);

                    double xRot = 0;
                    double yRot = yOut;
                    gs.rotate(-Math.PI/2, xRot, yRot);

                    gs.drawString(
                        txt,
                        (int)(0 - rect.getWidth()/2),
                        (int) (yOut-rect.getY())
                    );

                    gs.setTransform(transformSaved);
                }
            }
        });
    }
}
