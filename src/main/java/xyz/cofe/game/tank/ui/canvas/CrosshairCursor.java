package xyz.cofe.game.tank.ui.canvas;

import xyz.cofe.game.tank.Drawing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.Optional;

public class CrosshairCursor extends CanvasHost<CrosshairCursor> implements Drawing {
    //region visible : boolean
    private boolean visible = true;
    public boolean isVisible(){ return visible; }
    public void setVisible(boolean v){ visible = v; }
    //endregion

    private Color color = Color.BLACK;
    private Stroke stroke = new BasicStroke(1f);

    @Override
    public void draw(Graphics2D gs) {
        if( gs==null )return;

        var cmpt = component();
        var col = color;
        var stroke = this.stroke;
        if( cmpt==null || col==null || stroke==null )return;

        var mpt = cmpt.getMousePosition();
        if( mpt==null )return;

        gs.setPaint(col);
        gs.setStroke(stroke);

        gs.drawLine(mpt.x, 0, mpt.x, cmpt.getHeight());
        gs.drawLine(0, mpt.y, cmpt.getWidth(), mpt.y);
    }

    public Optional<xyz.cofe.game.tank.geom.Point> location(){
        var cmpt = component();
        var mpt = cmpt!=null ? cmpt.getMousePosition() : null;
        return mpt!=null
            ? Optional.of(xyz.cofe.game.tank.geom.Point.of(mpt.x, mpt.y))
            : Optional.empty();
    }
}
