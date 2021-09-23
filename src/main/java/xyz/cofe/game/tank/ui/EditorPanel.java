package xyz.cofe.game.tank.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import xyz.cofe.collection.BasicEventList;
import xyz.cofe.collection.EventList;
import xyz.cofe.ecolls.Closeables;
import xyz.cofe.game.tank.Observers;
import xyz.cofe.game.tank.geom.Point;
import xyz.cofe.game.tank.sprite.Sprite;
import xyz.cofe.game.tank.ui.canvas.*;
import xyz.cofe.game.tank.unt.Scene;
import xyz.cofe.game.tank.unt.SpriteFigura;
import xyz.cofe.gui.swing.SwingListener;

import javax.imageio.ImageIO;
import javax.swing.*;

public class EditorPanel extends JPanel implements OriginProperty {
    public EditorPanel(){
        setBackground(Color.WHITE);
        setOpaque(true);

        setFocusable(true);

        SwingListener.onMouseMoved( this, ev -> {
            if( ccursor.isVisible() )repaint();
        });
        SwingListener.onMouseDragged( this, ev -> {
            if( ccursor.isVisible() )repaint();
        });

        SwingListener.onKeyPressed( this, ev -> {
            //System.out.println("onKeyPressed code="+ev.getKeyCode());
        });
        SwingListener.onKeyReleased( this, ev -> {
            //System.out.println("onKeyReleased code="+ev.getKeyCode());
        });
    }

    private Consumer<MouseEvent> getMoveCanvasDragger(MouseEvent ev) {
        return new Consumer<MouseEvent>() {
            private MouseEvent init = ev;
            private Point initOrigin = getOrigin();

            @Override
            public void accept(MouseEvent me){
                double dx = me.getPoint().getX() - init.getPoint().getX();
                double dy = me.getPoint().getY() - init.getPoint().getY();

                setOrigin( Point.of( initOrigin.x()-dx, initOrigin.y()-dy ) );
            }
        };
    }

    //region scene : Scene
    public Observers<Scene> onSceneChanged = new Observers<>();
    private Scene scene;
    public Scene getScene(){
        if( scene!=null ){
            return scene;
        }
        scene = new Scene();
        return scene;
    }
    public void setScene( Scene scene ){
        if( scene==null )throw new IllegalArgumentException( "scene==null" );
        this.scene = scene;
        listen(scene);
        onSceneChanged.fire(scene);
    }
    protected final Closeables sceneListeners = new Closeables();
    protected void listen( Scene scene ){
        sceneListeners.close();
        if( scene!=null ){
            sceneListeners.add(scene.addListener(ev -> {
                if( ev instanceof Scene.AddFigure )repaint();
                else if( ev instanceof Scene.RemoveFigure )repaint();
                else if( ev instanceof Scene.MovedFigure )repaint();
                else if( ev instanceof Scene.SizeChanged )repaint();
            }));
        }
    }
    //endregion
    //region origin : Point
    private double xOrigin = 0;
    private double yOrigin = 0;
    public Point getOrigin(){ return Point.of(xOrigin, yOrigin); }
    public void setOrigin(Point p){
        if( p==null )throw new IllegalArgumentException( "p==null" );
        xOrigin = p.x();
        yOrigin = p.y();
        hrule.origin(p);
        vrule.origin(p);
        grid.origin(p);
        ccursor.origin(p);
        repaint();
    }
    //endregion

    private Consumer<MouseEvent> dragger = null;

    private SpriteFigura moveCanvasSprite;
    {
        var url = EditorPanel.class.getResource("/move-canvas.png");
        if( url!=null ){
            try{
                moveCanvasSprite = new SpriteFigura(
                    new Sprite(ImageIO.read(url))
                );
            } catch( IOException e ) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void processMouseMotionEvent(MouseEvent e) {
        if( e.getID()==MouseEvent.MOUSE_DRAGGED ){
            if( dragger!=null ){
                dragger.accept(e);
                return;
            }
        }
        super.processMouseMotionEvent(e);
    }

    @Override
    protected void processMouseEvent(MouseEvent ev) {
        // System.out.println("processMouseEvent "+ev);
        if( ev.getID()==MouseEvent.MOUSE_PRESSED ){
            if( ev.getButton()==MouseEvent.BUTTON1 ){
                if( moveCanvasSprite!=null && moveCanvasSprite.contains(ev) ){
                    dragger = getMoveCanvasDragger(ev);
                    return;
                }
            }
        }else if( ev.getID()==MouseEvent.MOUSE_RELEASED ){
            if( dragger!=null ){
                dragger = null;
                return;
            }
        }
        super.processMouseEvent(ev);
    }

    private final List<Consumer<Graphics2D>> onPaintComponent = new ArrayList<>();
    public AutoCloseable onPaintComponent( Consumer<Graphics2D> painter ){
        if( painter==null )throw new IllegalArgumentException( "painter==null" );
        onPaintComponent.add(painter);
        return ()->{ onPaintComponent.remove(painter); };
    }

    @Override
    protected void paintComponent(Graphics g){
        if( g==null ){
            super.paintComponent(g);
            return;
        }

        if( !(g instanceof Graphics2D) ){
            super.paintComponent(g);
            return;
        }

        super.paintComponent(g);

        Graphics2D gs = (Graphics2D) g;

        if( grid.isVisible() )grid.draw(gs);

        var scene = getScene();
        if( scene!=null ){
            AffineTransform savedAt = gs.getTransform();
            AffineTransform at = (AffineTransform)savedAt.clone();

            var orig = getOrigin();
            at.translate(-orig.x(), -orig.y());
            gs.setTransform(at);

            var size = scene.getSize();
            if( size.width()>0 && size.height()>0 ){
                double w = Math.min(32,scene.getBorderWidth());
                GeneralPath p = new GeneralPath();
                p.moveTo(0,0);
                p.lineTo(size.width()+w,0);
                p.lineTo(size.width()+w,-w);
                p.lineTo(-w,-w);
                p.lineTo(-w,size.height()+w);
                p.lineTo(size.width()+w,size.height()+w);
                p.lineTo(size.width()+w,0);
                p.lineTo(size.width(),0);
                p.lineTo(size.width(),size.height());
                p.lineTo(size.width(),size.height());
                p.lineTo(0,size.height());
                p.closePath();

                Color c = scene.getBorderColor();
                gs.setPaint(c!=null ? c : Color.gray);
                gs.fill(p);
            }

            for( var fig : scene.getFigures() ){
                if( fig!=null ){
                    fig.draw(gs);
                }
            }

            gs.setTransform(savedAt);
        }

        for( var painter : onPaintComponent ){
            if( painter!=null )painter.accept(gs);
        }

        if( hrule.isVisible() )hrule.draw(gs);
        if( vrule.isVisible() )vrule.draw(gs);
        if( ccursor.isVisible() )ccursor.draw(gs);
        for( var tt : tooltips ){
            if( tt!=null ){
                tt.draw(gs);
            }
        }

        if( moveCanvasSprite!=null ){
            //gs.drawImage(moveCanvas,null,getWidth() - moveCanvas.getWidth()-5, 30);
            moveCanvasSprite.location( getWidth()-moveCanvasSprite.width()-3, 20 );
            moveCanvasSprite.draw(gs);
        }
    }

    public final HRuler hrule = new HRuler().component(this);
    public final VRuler vrule = new VRuler().component(this);
    public final Grid grid = new Grid().component(this);
    public final CrosshairCursor ccursor = new CrosshairCursor().component(this);
    public final EventList<Tooltip> tooltips = new BasicEventList<>();
    {
        tooltips.onChanged((i,o,n)->{
            if( n!=null )n.component(this);
        });
        tooltips.onChanged((i,o,n)->repaint());
        //tooltips.add(new Tooltip("bla bla").relative(ccursor,-3,-3));
    }

    public final Tooltip ccursorPointer = new Tooltip();
    {
        ccursorPointer.relative(ccursor,3,-3);
        ccursorPointer.text( ()->{
            var pt = getMousePosition();
            if( pt==null )return null;

            var x = pt.x + getOrigin().x();
            var y = pt.y + getOrigin().y();
            return "("+x+","+y+")";
        });
        tooltips.add(ccursorPointer);
    }
}
