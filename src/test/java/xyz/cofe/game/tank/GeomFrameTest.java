package xyz.cofe.game.tank;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import xyz.cofe.fn.Tuple3;
import xyz.cofe.game.tank.geom.Line;
import xyz.cofe.game.tank.geom.Point;
import xyz.cofe.gui.swing.GuiUtil;
import xyz.cofe.gui.swing.SwingListener;
import xyz.cofe.gui.swing.color.ColorModificator;

import javax.swing.*;

public class GeomFrameTest extends JFrame {
    public class RenderGeom extends JComponent {
        {
            setFocusable(true);
            Consumer mouseEv = ev -> {
                repaint();
                revalidate();
            };
            SwingListener.onMouseMoved(this,mouseEv);
            SwingListener.onMouseExited(this,mouseEv);
            SwingListener.onMouseDragged(this,mouseEv);
            //this.setCursor();

            // Transparent 16 x 16 pixel cursor image.
            BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
            // Create a new blank cursor.
            Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new java.awt.Point(0, 0), "blank cursor");
            setCursor(blankCursor);

            SwingListener.onKeyPressed(this, ev -> {
                if( ev.getKeyCode()==KeyEvent.VK_Q ){
                    toolRender = this::intersections;
                    tip("intersections");
                }else if( ev.getKeyCode()==KeyEvent.VK_W ){
                    toolRender = this::nearestPoint;
                    tip("nearestPoint");
                }else if( ev.getKeyCode()==KeyEvent.VK_1 ){
                    toolMouse = this::startCreateLine;
                    tip("set first point");
                }
            });

            SwingListener.onMouseClicked(this,e -> {
                if( this.toolMouse!=null ){
                    this.toolMouse.accept(e);
                }
            });
        }

        private Consumer<MouseEvent> toolMouse = null;

        private Consumer<Graphics2D> toolRender = null;

        private void nearestPoint( Graphics2D gs ){
            var e = getMousePosition(true);
            if( e==null )return;

            var mp = Point.of(e.getX(), e.getY());
            var points = lines.stream()
                .map( line -> {
                        var np = line.nearestPoint(mp,true);
                        return Tuple3.of(line, np, np.distance(mp));
                    }
                )
                .sorted( (a,b)->Double.compare(a.c(), b.c()))
                .collect(Collectors.toList());

            var min = !points.isEmpty() ? Optional.of(points.get(0).c()) : Optional.<Double>empty();

            points.forEach( p -> {
                circle(gs, p.b(), 5, Color.pink);
                if( p.c()==min.get() ){
                    circle(gs, p.b(), 10, Color.pink);
                }
            });
        }

        private void circle( Graphics2D gs, Point p, double d, Color color ){
            gs.setPaint(color);
            gs.drawOval(
                (int)(p.x()-d/2), (int)(p.y()-d/2),
                (int)(d), (int)(d)
            );
        }

        //region create line
        private MouseEvent firstPoint;

        private void startCreateLine(MouseEvent e){
            firstPoint = e;
            toolMouse = this::finishCreateLine;
            toolRender = this::finishCreateLine;

            tip("set second point");

        }

        private void finishCreateLine(Graphics2D gs){
            var e = getMousePosition(true);
            if( firstPoint!=null && e!=null ){
                var line = Line.of( firstPoint.getX(), firstPoint.getY(), e.getX(), e.getY() );

                gs.setPaint(Color.white);
                gs.drawLine((int)line.x0(), (int)line.y0(), (int)line.x1(), (int)line.y1());
            }
        }

        private void finishCreateLine(MouseEvent e){
            toolMouse = null;
            toolRender = null;
            tip("");
            if( firstPoint!=null ){
                lines.add(
                    Line.of( firstPoint.getX(), firstPoint.getY(), e.getX(), e.getY() )
                );
                repaint();
                revalidate();
            }
        }
        //endregion

        //region intersections
        private void intersections(Graphics2D gs){
            double d = 10;
            for( int i=0; i<lines.size()-1; i++ ){
                for( int j=i+1; j<lines.size(); j++ ){
                    var line0 = lines.get(i);
                    var line1 = lines.get(j);
                    var isec = line0.intersection(line1,true);
                    isec.ifPresent( pt -> {
                        gs.setPaint(Color.yellow);
                        gs.drawOval(
                            (int)(pt.x()-d/2), (int)(pt.y()-d/2),
                            (int)d, (int)d
                        );
                    });
                }
            }
        }
        //endregion

        private ArrayList<Line> lines = new ArrayList<>();

        {
            lines.add(Line.of(10,10,100,100));
            lines.add(Line.of(100,10,10,100));
        }

        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            Graphics2D gs = (Graphics2D) g;

            gs.setPaint(Color.black);
            gs.fillRect(0,0,getWidth(),getHeight());

            lines.forEach( line -> {
                gs.setPaint(Color.white);
                gs.drawLine(
                    (int)line.x0(), (int)line.y0(),
                    (int)line.x1(), (int)line.y1()
                );
            });

            if( toolRender!=null ){
                toolRender.accept(gs);
            }

            paintMouseCursor(gs);
        }

        private Stroke mouseCursorLine = new BasicStroke(
            1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL, 1,
            new float[]{ 3f, 1f, 3f, 4f }, 0f
            );

        private String cursorTip = "";
        private void tip(String tip){
            this.cursorTip = tip;
            repaint();
            revalidate();
        }
        private Font cursorFontTip = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
        private ColorModificator cursorTipBG = new ColorModificator().bright(0.2f);
        private ColorModificator cursorTipFG = new ColorModificator().bright(0.2f);

        protected void paintMouseCursor(Graphics2D gs){
            var mousePoint = getMousePosition(true);
            if( mousePoint!=null ){
                if( cursorTip!=null && cursorTip.length()>0 && cursorFontTip!=null ){
                    gs.setFont(cursorFontTip);

                    var fm = gs.getFontMetrics();

                    var strBnd = fm.getStringBounds(cursorTip,gs);

                    gs.setPaint( cursorTipBG.apply(Color.white) );
                    gs.fillRect(
                        mousePoint.x, mousePoint.y - (int)strBnd.getHeight(),
                        (int)strBnd.getWidth(), (int)strBnd.getHeight()
                    );

                    gs.setPaint(Color.white);
                    gs.drawString(cursorTip,
                        mousePoint.x,
                        mousePoint.y //+ (int)fm.getHeight()
                        -fm.getDescent()
                    );
                }

                gs.setStroke(mouseCursorLine);
                gs.setPaint(Color.red);
                gs.drawLine(0, mousePoint.y, getWidth(), mousePoint.y);
                gs.drawLine(mousePoint.x, 0, mousePoint.x, getHeight());
            }
        }
    }

    private final RenderGeom render = new RenderGeom();

    public static void main(String[] args){
        SwingUtilities.invokeLater(()->{
            GeomFrameTest frame = new GeomFrameTest();
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setTitle("geom test");

            frame.getContentPane().setLayout(new BorderLayout());
            frame.getContentPane().add(frame.render);

            SwingListener.onWindowOpened(
                frame,
                GuiUtil.windowReciver().relativeSize(0.7, 0.7).center().build());
            frame.setVisible(true);
        });
    }
}
