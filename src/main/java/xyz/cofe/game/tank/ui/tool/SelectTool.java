package xyz.cofe.game.tank.ui.tool;

import xyz.cofe.collection.BasicEventSet;
import xyz.cofe.collection.EventSet;
import xyz.cofe.game.tank.geom.Point;
import xyz.cofe.game.tank.geom.Rect;
import xyz.cofe.game.tank.ui.KeyCode;
import xyz.cofe.game.tank.ui.KeyEv;
import xyz.cofe.game.tank.ui.MouseEv;
import xyz.cofe.game.tank.ui.canvas.Grid;
import xyz.cofe.game.tank.unt.SceneProperty;
import xyz.cofe.game.tank.ui.Tool;
import xyz.cofe.game.tank.unt.Figura;
import xyz.cofe.game.tank.unt.Scene;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.function.Supplier;

public class SelectTool extends AbstractTool implements Tool, SceneProperty {
    //region image, name
    @Override
    public String name() {
        return "Select";
    }

    private BufferedImage image;
    {
        try {
            image = ImageIO.read(Objects.requireNonNull(SelectTool.class.getResource("/select-tool-16.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public BufferedImage image() {
        return image;
    }
    //endregion
    //region scene : Scene
    protected Scene scene;
    public Scene getScene() { return scene; }
    public void setScene(Scene scene) { this.scene = scene; }
    //endregion
    //region selection : Set<Figura<?>>
    protected final EventSet<Figura<?>> selection = new BasicEventSet<>(new LinkedHashSet<>());
    public EventSet<Figura<?>> getSelection(){ return selection; }
    //endregion
    //region lastSelected : Figura<?>
    protected Figura<?> lastSelected;
    public Figura<?> getLastSelected() { return lastSelected; }
    public void setLastSelected(Figura<?> lastSelected) {
        var ch = this.lastSelected != lastSelected;
        this.lastSelected = lastSelected;
        if( ch )fireEvent(new LastSelectChanged(lastSelected));
    }
    public static class LastSelectChanged implements ToolEvent {
        public final Figura<?> figura;
        public LastSelectChanged(Figura<?> figura) {
            this.figura = figura;
        }
    }
    //endregion
    //region origin : Point
    protected Supplier<Point> origin;
    public Point getOrigin(){
        var o = origin;
        return o!=null ? o.get() : Point.of(0,0);
    }
    public void setOrigin( Point origin ){
        if( origin==null )throw new IllegalArgumentException( "origin==null" );
        this.origin = ()->origin;
    }
    public void setOrigin( Supplier<Point> origin ){
        if( origin==null )throw new IllegalArgumentException( "origin==null" );
        this.origin = origin;
    }
    @SuppressWarnings("UnusedReturnValue")
    public SelectTool origin(Supplier<Point> origin ){
        if( origin==null )throw new IllegalArgumentException( "origin==null" );
        this.origin = origin;
        return this;
    }
    //endregion

    //region grid : Supplier<Grid>
    protected Supplier<Grid> grid;
    public Grid getGrid() {
        return grid!=null ? grid.get() : null;
    }

    public void setGrid(Supplier<Grid> grid) {
        this.grid = grid;
    }
    public void setGrid(Grid grid) {
        this.grid = ()->grid;
    }
    //endregion
    //region snapToGrid : boolean
    protected boolean snapToGrid = false;

    public boolean isSnapToGrid() {
        return snapToGrid;
    }

    public void setSnapToGrid(boolean snapToGrid) {
        this.snapToGrid = snapToGrid;
    }
    //endregion

    protected Shape bounds(Figura<?> f, double indent ){
        return new Rectangle2D.Double(
            f.left()-indent, f.top()-indent, f.width()+indent*2, f.height()+indent*2
        );
    }

    protected Stroke stroke = new BasicStroke(
        1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1.0f, new float[]{ 3f, 1f }, 0.0f);

    protected Color color = Color.gray;

    @Override
    public void onPaint(Graphics2D gs) {
        if( gs==null )return;

        var scene = getScene();

        var orig = getOrigin();
        var savedAt = gs.getTransform();
        var at = (AffineTransform)savedAt.clone();

        at.translate(-orig.x(), -orig.y());

        gs.setTransform(at);
        ////////////////////////////
        if( stroke!=null )gs.setStroke(stroke);
        for( var fig : getSelection() ){
            if( scene!=null ){
                if( !scene.getFigures().contains(fig) )continue;
            }
            var s = bounds(fig,2);
            gs.setPaint(color);
            gs.draw(s);
        }

        if( boxStart!=null && boxEnd!=null ){
            Rect rect = Rect.rect(boxStart,boxEnd);
            var rrct = new Rectangle2D.Double(rect.left(), rect.top(), rect.width(), rect.height());
            gs.setPaint(color);
            gs.draw(rrct);
        }

        /////////////////////////
        gs.setTransform(savedAt);
    }

    protected MouseEv dragStart;
    protected Map<Figura<?>,Point> dragInitial = new LinkedHashMap<>();

    @Override
    public void onMouseClicked(MouseEv p) {
        if( p==null )throw new IllegalArgumentException( "p==null" );

        var scene = getScene();
        if( scene==null )return;

        List<Figura<?>> hits = new ArrayList<>();

        if( p.isShift() ) {
            scene.getFigures().stream().filter(f -> f.contains(p)).forEach( hits::add );
            getSelection().addAll(hits);
        }else {
            getSelection().clear();
            scene.getFigures().stream().filter(f -> f.contains(p)).forEach( hits::add );
            getSelection().addAll(hits);
        }

        if( !hits.isEmpty() ){
            setLastSelected(hits.get(hits.size()-1));
        }

        if( p.getComponent()!=null ){
            p.getComponent().repaint();
        }
    }

    protected MouseEv boxStart;
    protected MouseEv boxEnd;

    @Override
    public void onMousePressed(MouseEv pt) {
        if( boxSelectionMode && pt.isLeftButton() ){
            boxStart = pt;
            boxEnd = pt;
            if( pt.getComponent()!=null ){
                pt.getComponent().repaint();
            }
            return;
        }

        if( pt.isLeftButton() ){
            if( !getSelection().isEmpty() ){
                getSelection().stream().filter( f -> f.contains(pt) ).findAny().ifPresentOrElse( _any_ -> {
                    dragStart = pt;
                    dragInitial.clear();
                    getSelection().forEach( f -> {
                        dragInitial.put(f, Point.of(f.left(), f.top()));
                    });
                }, ()->{
                    dragInitial.clear();
                    dragStart = null;
                });
            }
        }
    }

    @Override
    public void onMouseDragged(MouseEv pt) {
        if( boxStart!=null ){
            boxEnd = pt;
            if( pt.getComponent()!=null ){
                pt.getComponent().repaint();
            }
            return;
        }

        if( dragStart!=null ){
            double dx = pt.x() - dragStart.x();
            double dy = pt.y() - dragStart.y();

            dragInitial.forEach( (f,p)->{
                var loc = p.translate(Point.of(dx,dy));
                var grid = getGrid();
                if( snapToGrid && grid!=null ){
                    loc = grid.nearestPoint(loc);
                }
                f.location( loc );
            });

            if( pt.getComponent()!=null ){
                pt.getComponent().repaint();
            }
        }
    }

    @Override
    public void onMouseReleased(MouseEv pt) {
        if( boxStart!=null ){
            boxEnd = pt;
            boxSelect(boxStart,boxEnd);
            if( pt.getComponent()!=null ){
                pt.getComponent().repaint();
            }
        }

        boxStart = null; boxEnd = null;
        dragStart = null;
        dragInitial.clear();
    }

    @Override
    public void onMouseExited(MouseEv pt) {
        onMouseReleased(pt);
    }

    protected boolean boxSelectionMode = false;

    @Override
    public void onKeyPressed(KeyEv keyEv) {
        if( keyEv.keyCode()== KeyCode.B ){
            boxSelectionMode = true;
        }
    }

    @Override
    public void onKeyReleased(KeyEv keyEv) {
        if( keyEv.keyCode()== KeyCode.B ){
            boxSelectionMode = false;
        }
    }

    protected void boxSelect( MouseEv start, MouseEv end ){
        Rect rect = Rect.rect(start,end);

        var scene = getScene();
        if( scene==null )return;

        List<Figura<?>> hits = new ArrayList<>();
        for( var f : scene.getFigures() ){
            rect.intersection(f).ifPresent( i -> {
                hits.add(f);
            });
        }

        if( end.isShift() ) {
            getSelection().addAll(hits);
        }else {
            getSelection().clear();
            getSelection().addAll(hits);
        }

        if( !hits.isEmpty() ){
            setLastSelected(hits.get(hits.size()-1));
        }
    }
}
