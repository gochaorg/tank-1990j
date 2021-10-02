package xyz.cofe.game.tank.ui.tool;

import xyz.cofe.collection.BasicEventSet;
import xyz.cofe.collection.EventSet;
import xyz.cofe.ecolls.Closeables;
import xyz.cofe.fn.Tuple3;
import xyz.cofe.game.tank.Observers;
import xyz.cofe.game.tank.geom.Point;
import xyz.cofe.game.tank.geom.Rect;
import xyz.cofe.game.tank.ui.*;
import xyz.cofe.game.tank.ui.canvas.Grid;
import xyz.cofe.game.tank.ui.canvas.Tooltip;
import xyz.cofe.game.tank.ui.cmd.ConvertToAction;
import xyz.cofe.game.tank.unt.SceneProperty;
import xyz.cofe.game.tank.unt.Figure;
import xyz.cofe.game.tank.unt.Scene;
import xyz.cofe.gui.swing.SwingListener;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.function.Supplier;

public class SelectTool extends AbstractTool implements Tool, SceneProperty, GridBinding, SnapToGridProperty {
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
    protected final Observers<Scene> onSceneChanged = new Observers<>();
    protected Scene scene;
    public Scene getScene() { return scene; }
    public void setScene(Scene scene) {
        this.scene = scene;
        onSceneChanged.fire(scene);
    }
    //endregion
    //region selection : Set<Figura<?>>
    protected final EventSet<Figure<?>> selection = new BasicEventSet<>(new LinkedHashSet<>());
    public EventSet<Figure<?>> getSelection(){ return selection; }
    //endregion
    //region lastSelected : Figura<?>
    protected Figure<?> lastSelected;
    public Figure<?> getLastSelected() { return lastSelected; }
    public void setLastSelected(Figure<?> lastSelected) {
        var ch = this.lastSelected != lastSelected;
        this.lastSelected = lastSelected;
        if( ch )fireEvent(new LastSelectChanged(lastSelected));
    }
    public static class LastSelectChanged implements ToolEvent {
        public final Figure<?> figura;
        public LastSelectChanged(Figure<?> figura) {
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

    protected Supplier<Double> scale;
    public double getScale(){
        var s = scale!=null ? scale.get() : null;
        return s!=null ? s : 0.0;
    }
    public void setScale(Supplier<Double> scale){
        this.scale = scale;
    }
    public void setScale(double scale){
        this.scale = ()->scale;
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

    //region selectActions : List<SelectAction>
    protected List<SelectAction> selectActions = List.of();
    public List<SelectAction> getSelectActions() {
        return selectActions;
    }

    public void setSelectActions(List<SelectAction> selectActions) {
        if( selectActions==null )throw new IllegalArgumentException( "selectActions==null" );
        this.selectActions = selectActions;
    }
    //endregion

    //region context menu
    protected JPopupMenu buildPopupMenu(){
        var scene = getScene();
        if( scene==null )return null;
        if( getSelection().isEmpty() )return null;

        JPopupMenu popupMenu = new JPopupMenu();
        JMenu convertMenu = new JMenu("Convert to");
        popupMenu.add(convertMenu);

        if( selectActions!=null ){
            for( var sa : selectActions ){
                JMenuItem callAction = new JMenuItem(sa.name());
                SwingListener.onActionPerformed(callAction,ev -> {
                    sa.execute(scene, getSelection());
                });
                sa.image().ifPresent( img -> {
                    var ico = new ImageIcon(img);
                    callAction.setIcon(ico);
                });

                if( sa instanceof ConvertToAction ){
                    convertMenu.add(callAction);
                }else{
                    popupMenu.add(callAction);
                }
            }
        }
        return popupMenu;
    }
    protected Rectangle2D.Double bounds(Rect f, @SuppressWarnings("SameParameterValue") double indent ){
        var scale = getScale();
        return new Rectangle2D.Double(
            f.left()*scale-indent,
            f.top()*scale-indent,
            f.width()*scale+indent*2,
            f.height()*scale+indent*2
        );
    }
    //endregion

    protected Stroke stroke = new BasicStroke(
        1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1.0f, new float[]{ 3f, 1f }, 0.0f);

    protected Color color = Color.gray;

    protected List<Tuple3<Figure<?>, Figure<?>, Rect>> intersections(Scene scene ){
        if( scene==null )throw new IllegalArgumentException( "scene==null" );
        List<Tuple3<Figure<?>, Figure<?>, Rect>> res = new ArrayList<>();
        for( var faIdx=0; faIdx<scene.getFigures().size(); faIdx++ ){
            for( var fbIdx=faIdx+1; fbIdx<scene.getFigures().size(); fbIdx++ ){
                var fa = scene.getFigures().get(faIdx);
                var fb = scene.getFigures().get(fbIdx);
                if( fa==null || fb==null || fa==fb )continue;

                var intersect = fa.intersection(fb);
                if( intersect.isEmpty() )continue;

                res.add(Tuple3.of(fa,fb,intersect.get()));
            }
        }

        return res;
    }

    private List<Tuple3<Figure<?>, Figure<?>, Rect>> intersections;
    {
        onSceneChanged.listen( ev -> {
            intersections=null;
            intersectionsRenders=null;
        });
    }

    private List<Tuple3<Figure<?>, Figure<?>, Rectangle2D.Double>> intersectionsRenders;
    private List<Tuple3<Figure<?>, Figure<?>, Rectangle2D.Double>> intersectionsRenders(){
        if( intersectionsRenders!=null )return intersectionsRenders;
        if( scene==null )return List.of();
        intersections = intersections(scene);

        if( intersections==null || intersections.isEmpty() ){
            intersectionsRenders = List.of();
            return intersectionsRenders;
        }
        intersectionsRenders = new ArrayList<>();
        for( var itr : intersections ){
            Tuple3 tuple =
                Tuple3.of(itr.a(), itr.b(), bounds(itr.c(),0));

            intersectionsRenders.add(tuple);
        }
        return intersectionsRenders;
    }

    private Closeables figMovLs = new Closeables();
    {
        onSceneChanged.listen( ev -> {
            figMovLs.close();
            if( ev.event!=null ) {
                figMovLs.add(
                ev.event.addListener( e -> {
                    if( e instanceof Scene.MovedFigure || e instanceof Scene.AddFigure || e instanceof Scene.RemoveFigure ){
                        intersections = null;
                        intersectionsRenders = null;
                    }
                }));
                intersections = null;
                intersectionsRenders = null;
            }
        });
    }

    protected Stroke intersectStroke1 = new BasicStroke(2f,
        BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,0f,
        new float[]{2f,2f},0f
    );
    protected Stroke intersectStroke2 = new BasicStroke(2f,
        BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,0f,
        new float[]{2f,2f},2f
    );
    protected Color intersectColor1 = Color.red;
    protected Color intersectColor2 = Color.green;
    protected Color intersectColor3;
    {
        intersectColor3 = new Color(255,128,128,128);
    }
    protected boolean intersectVisible = false;

    public boolean isIntersectVisible() {
        return intersectVisible;
    }

    public void setIntersectVisible(boolean intersectVisible) {
        this.intersectVisible = intersectVisible;
    }

    private Tooltip intersectCount;
    {
        intersectCount = new Tooltip();
        intersectCount.setText(()->{
            var ic = intersectionsRenders;
            if( ic!=null ){
                return "intersections "+ic.size();
            }
            return "intersections 0";
        });
    }
    public Tooltip getIntersectCountTooltip(){
        return intersectCount;
    }

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

        if( intersectVisible ){
            var intersect = intersectionsRenders();
            if( intersect!=null
                && intersectColor1 !=null && intersectStroke1!=null
                && intersectColor2 !=null && intersectStroke2!=null
            ){
                for( var intr : intersect ){
                    if( intr!=null && intr.c()!=null ){
                        gs.setPaint(intersectColor3);
                        gs.fill(intr.c());

                        gs.setPaint(intersectColor1);
                        gs.setStroke(intersectStroke1);
                        gs.draw(intr.c());

                        gs.setPaint(intersectColor2);
                        gs.setStroke(intersectStroke2);
                        gs.draw(intr.c());
                    }
                }
            }
        }

        /////////////////////////
        gs.setTransform(savedAt);
    }

    //region input events
    protected MouseEv dragStart;
    protected Map<Figure<?>,Point> dragInitial = new LinkedHashMap<>();

    @Override
    public void onMouseClicked(MouseEv p) {
        if( p==null )throw new IllegalArgumentException( "p==null" );

        var scene = getScene();
        if( scene==null )return;

        if( p.isLeftButton() ) {
            List<Figure<?>> hits = new ArrayList<>();
            if (p.isShift()) {
                scene.getFigures().stream().filter(f -> f.contains(p)).forEach(hits::add);
                getSelection().addAll(hits);
            } else {
                getSelection().clear();
                scene.getFigures().stream().filter(f -> f.contains(p)).forEach(hits::add);
                getSelection().addAll(hits);
            }
            if (!hits.isEmpty()) {
                setLastSelected(hits.get(hits.size() - 1));
            }
            if (p.getComponent() != null) {
                p.getComponent().repaint();
            }
        } else if( p.isRightButton() && selectActions!=null && !selectActions.isEmpty() ){
            var popup = buildPopupMenu();
            if( popup==null )return;

            var cmpt = p.getComponent();
            if( cmpt==null )return;

            var mp = cmpt.getMousePosition();
            if( mp!=null ){
                popup.show(cmpt, mp.x, mp.y);
            }
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

        List<Figure<?>> hits = new ArrayList<>();
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
    //endregion
}
