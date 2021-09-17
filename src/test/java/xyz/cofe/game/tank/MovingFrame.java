package xyz.cofe.game.tank;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import xyz.cofe.game.tank.geom.MutableRect;
import xyz.cofe.game.tank.geom.Point;
import xyz.cofe.game.tank.geom.Rect;
import xyz.cofe.game.tank.job.MoveCollector;
import xyz.cofe.game.tank.job.Moving;
import xyz.cofe.game.tank.sprite.SpritesData;
import xyz.cofe.game.tank.unt.Brick;
import xyz.cofe.game.tank.unt.Directed;
import xyz.cofe.game.tank.unt.Direction;
import xyz.cofe.game.tank.unt.LevelBrick;
import xyz.cofe.game.tank.unt.PlayerOne;
import xyz.cofe.game.tank.unt.SpriteFigura;
import xyz.cofe.gui.swing.GuiUtil;
import xyz.cofe.gui.swing.SwingListener;
import xyz.cofe.gui.swing.properties.PropertySheet;
import xyz.cofe.gui.swing.table.TableEvent;
import xyz.cofe.gui.swing.table.TableListener;
import xyz.cofe.gui.swing.tree.TreeTable;
import xyz.cofe.gui.swing.tree.TreeTableNodeBasic;
import xyz.cofe.iter.Eterable;

import javax.imageio.ImageIO;
import javax.swing.*;

public class MovingFrame extends JFrame {
    //region tools
    protected final List<Tool> tools = new ArrayList<>();
    protected Tool tool = null;
    private final JLabel toolName = new JLabel("tool name");
    //endregion

    private final List<GameUnit<?>> gameUnits = new ArrayList<>();
    private final List<LevelBrick<?>> levelBricks = new ArrayList<>();
    private final Set<Moving<?>> movings = new LinkedHashSet<>();
    private final MoveCollector moveCollector = new MoveCollector();
    {
        moveCollector.setMovings(movings);

        var c1 = Eterable.<Rect>of(levelBricks);
        var c2 = Eterable.<Rect>of(gameUnits);
        moveCollector.setCollisions(c1.union(c2));
    }

    private final Timer timer = new Timer(20, (e)->{
        //this.view.paintImmediately(0,0,this.view.getWidth(),this.view.getHeight());

        moveCollector.estimate().apply(3, (mv, gu,rect)->{
            System.out.println("collision "+gu+" rect "+rect);
            mv.stop();
        });

        gameUnits.forEach(GameUnit::run);

        this.view.repaint();
        Toolkit.getDefaultToolkit().sync();

        long now = System.currentTimeMillis();
        movings.removeIf( m -> {
            if( !m.isStopped() )return false;
            return (now - m.getStoppedTime())>1000L;
        });
    });

    //region view
    private final View view = new View();
    public class View extends JComponent {

        @Override
        protected void paintComponent(Graphics g){
            Graphics2D gs = (Graphics2D) g;
            gs.setPaint(Color.black);
            gs.fillRect(0,0,getWidth(),getHeight());

            levelBricks.forEach( gu -> gu.draw(gs) );
            gameUnits.forEach( gu -> gu.draw(gs) );

            if( tool!=null ){
                tool.draw(View.this, gs);
            }
        }

        //region mouse/key listeners
        {
            JComponent self = this;

            MouseMotionListener mml = new MouseMotionListener() {
                @Override
                public void mouseDragged(MouseEvent e){
                    if( tool!=null )tool.mouseEvent(self, e);
                }

                @Override
                public void mouseMoved(MouseEvent e){
                    if( tool!=null )tool.mouseEvent(self, e);
                }
            };

            MouseListener ml = new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e){
                    if( tool!=null ){
                        self.requestFocus();
                        tool.mouseEvent(self, e);
                    }
                }

                @Override
                public void mousePressed(MouseEvent e){
                    if( tool!=null )tool.mouseEvent(self, e);
                }

                @Override
                public void mouseReleased(MouseEvent e){
                    if( tool!=null )tool.mouseEvent(self, e);
                }

                @Override
                public void mouseEntered(MouseEvent e){
                    if( tool!=null )tool.mouseEvent(self, e);
                }

                @Override
                public void mouseExited(MouseEvent e){
                    if( tool!=null )tool.mouseEvent(self, e);
                }
            };

            KeyListener kl = new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e){
                    if( tool!=null )tool.keyEvent(self,e);
                }

                @Override
                public void keyPressed(KeyEvent e){
                    if( tool!=null )tool.keyEvent(self,e);
                }

                @Override
                public void keyReleased(KeyEvent e){
                    if( tool!=null )tool.keyEvent(self,e);
                }
            };

            self.addMouseListener(ml);
            self.addMouseMotionListener(mml);
            self.addKeyListener(kl);
        }
        //endregion
    }
    //endregion
    //region control panel
    private final Control control = new Control();
    public class Control extends JPanel {
        {
            setLayout(new BoxLayout(Control.this, BoxLayout.Y_AXIS));

            SwingUtilities.invokeLater(()->{
                for( var tool : MovingFrame.this.tools ){
                    JButton createTank = new JButton(tool.getToolName());
                    createTank.addActionListener(e -> {
                        setTool(tool);
                    });
                    add(createTank);
                }

                repaint();
                invalidate();
                revalidate();
            });

            add(toolName);
            toolName.setHorizontalAlignment(SwingConstants.CENTER);
        }
    }

    protected void setTool(Tool tool){
        this.tool = tool;
        if( tool!=null ){
            toolName.setText(tool.getToolName());
            view.requestFocus();
        }
    }
    //endregion

    //region select tool
    protected final Set<GameUnit<?>> selection = new LinkedHashSet<>();
    protected final Map<GameUnit<?>, Rect> selectionInitialLocation = new LinkedHashMap<>();
    protected Point dragStartingPoint = null;
    protected final Tool selectTool = new Tool() {
        @Override
        public String getToolName(){
            return "select";
        }

        protected final Stroke stroke = new BasicStroke(
            1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL, 1,
            new float[]{3f, 3f, }, 0
        );

        @Override
        protected void onDraw(Graphics2D gs){
            gs.setStroke(stroke);
            gs.setPaint(Color.green);
            selection.forEach( sel -> {
                if( sel!=null ){
                    //System.out.println("selection paint");
                    gs.drawRect( (int)sel.left(), (int)sel.top(), (int)sel.width(), (int)sel.height() );
                }
            });
        }

        @Override
        protected void onClicked(MouseEvent e){
            Point pt = Point.of(e.getX(), e.getY());

            boolean shiftPresses = (e.getModifiersEx() & MouseEvent.SHIFT_DOWN_MASK) == MouseEvent.SHIFT_DOWN_MASK;
            boolean ctrlPresses = (e.getModifiersEx() & MouseEvent.CTRL_DOWN_MASK) == MouseEvent.CTRL_DOWN_MASK;

            if( e.getButton()==MouseEvent.BUTTON1 ){
                if( !ctrlPresses ){
                    if( !shiftPresses ){
                        selection.clear();
                    }
                    selection.addAll(gameUnits.stream().filter(gu -> gu.contains(pt)).collect(Collectors.toList()));
                }
                propsOfSelection();
            }
        }

        @Override
        protected void onKeyPressed(KeyEvent e){
            var mp = canvas.getMousePosition(true);
            switch( e.getKeyCode() ){
                case KeyEvent.VK_Q:
                    rotateSelection(true);
                    break;
                case KeyEvent.VK_E:
                    rotateSelection(false);
                    break;
                case KeyEvent.VK_M:
                    moveSelectionTo(Point.of(mp.getX(), mp.getY()));
                    break;
                case KeyEvent.VK_A:
                    if( e.isControlDown() && !e.isAltDown() && !e.isShiftDown() ){
                        selectAll();
                    }
                    break;
                case KeyEvent.VK_DELETE:
                    if( !e.isControlDown() && !e.isAltDown() && !e.isShiftDown() ){
                        deleteSelected();
                    }
                    break;
            }
        }

        protected void propsOfSelection(){
            if( selection.isEmpty() ){
                propertySheet.edit(null);
            }else{
                if( selection.size()==1 ){
                    var first =selection.iterator().next();
                    propertySheet.edit()
                        .bean(first)
                        .properties().add("x", Double.class, first::left, v->{
                            first.location((Double)v, first.top());
                            return first.left();
                        })
                        .properties().add("y", Double.class, first::top, v->{
                            first.location(first.left(),(Double)v);
                            return first.top();
                        })
                        .apply();
                }
            }
        }

        @Override
        protected void onPressed(MouseEvent e){
            if( e.getButton()==MouseEvent.BUTTON1 ){
                selectionInitialLocation.clear();
                selection.forEach( s -> {
                    selectionInitialLocation.put(s, new MutableRect(s));
                });
                dragStartingPoint = Point.of(e.getX(), e.getY());
            }
        }

        @Override
        protected void onDragged(MouseEvent e){
            var p = Point.of(e.getX(), e.getY());
            if( dragStartingPoint!=null && !selection.isEmpty() ){
                selection.forEach( s -> {
                    var from = selectionInitialLocation.getOrDefault(s,null);
                    if( from==null )return;

                    var xOffset = p.x() - dragStartingPoint.x();
                    var yOffset = p.y() - dragStartingPoint.y();

                    var xTo = from.left()+xOffset;
                    var yTo = from.top()+yOffset;

                    s.location(xTo, yTo);
                });
            }
        }

        @Override
        protected void onReleased(MouseEvent e){
        }
    };
    { tools.add(selectTool); }
    protected void rotateSelection(boolean ccw){
        selection.forEach( s -> {
            if( s instanceof Directed ){
                var d = ((Directed<?>)s).direction();
                ((Directed<?>)s).direction( rotate(d,ccw) );
            }
        });
    }
    protected Direction rotate(Direction d,boolean ccw){
        if( ccw ){
            switch( d ){
                case UP:
                    return Direction.LEFT;
                case LEFT:
                    return Direction.DOWN;
                case DOWN:
                    return Direction.RIGHT;
                case RIGHT:
                    return Direction.UP;
            }
        }else{
            switch( d ){
                case UP:
                    return Direction.RIGHT;
                case RIGHT:
                    return Direction.DOWN;
                case DOWN:
                    return Direction.LEFT;
                case LEFT:
                    return Direction.UP;
            }
        }
        return d;
    }
    //endregion
    //region create unit tools
    //region createBuilderTool()
    protected Tool createBuilderTool(String name, SpriteFigura spriteFigura, Consumer<Point> building ){
        return new Tool() {
            private final SpriteFigura sprite = spriteFigura;

            @Override
            public String getToolName(){
                return name;
            }

            @Override
            protected void onMoved(MouseEvent e){
                sprite.location(e.getX(),e.getY());
            }

            @Override
            protected void onDraw(Graphics2D gs){
                sprite.draw(gs);
            }

            @Override
            protected void onClicked(MouseEvent e){
                building.accept(Point.of(e.getX(), e.getY()));
                setTool(selectTool);
            }
        };
    }
    //endregion
    protected final Tool createTankTool = createBuilderTool(
        "Create tank", SpritesData.player_one_right_0.toSpriteLine().toFigure(), pt -> {
            PlayerOne tank = new PlayerOne().location(pt.x(),pt.y());
            gameUnits.add(tank);
        });
    protected final Tool createBrickTool = createBuilderTool(
        "Create brick", SpritesData.lvl_brick.toSpriteLine().toFigure(), pt -> {
            levelBricks.add(
                new Brick().location(pt.x(),pt.y())
            );
        });
    {
        tools.add(createTankTool);
        tools.add(createBrickTool);
    }
    //endregion

    //region операции на выбранными объектами
    protected class MovingToolOptions {
        protected double speed = 20.0;
        public double getSpeed(){ return speed; }
        public void setSpeed(double speed){ this.speed = speed; }
        @Override public String toString(){ return "Moving tool"; }
    }
    protected final MovingToolOptions movingToolOptions = new MovingToolOptions();

    protected void moveSelectionTo( Point p ){
        selection.forEach( s -> moveTo((GameUnit)s, p));
    }
    protected <U extends GameUnit<U>> void moveTo( U gu, Point p ){
        Point from = Point.of(gu.left() + gu.width()/2, gu.top()+gu.height()/2 );
        if( from.distance(p)<5 )return;

        double xdiff = p.x() - from.x();
        double ydiff = p.y() - from.y();

        List<Moving> jobs = new ArrayList<>();
        if( Math.abs(xdiff)>=2 ){
            var mHorz = Moving.create(gu);
            mHorz.speed(movingToolOptions.getSpeed());

            mHorz.onExecuted(e -> {
                var gu1 = e.getJob().getGameUnit();
                if( xdiff<0 ){
                    if( gu1.left() < p.x() ){
                        e.getJob().stop();
                    }
                }else{
                    if( gu1.left() > p.x() ){
                        e.getJob().stop();
                    }
                }
            });

            if( xdiff<0 ){
                mHorz.direction(Direction.LEFT);
            }else{
                mHorz.direction(Direction.RIGHT);
            }

            jobs.add(mHorz);
        }
        if( Math.abs(ydiff)>=2 ){
            var mVert = Moving.create(gu);
            mVert.speed(movingToolOptions.getSpeed());
            mVert.onExecuted(e -> {
                var gu1 = e.getJob().getGameUnit();
                if( ydiff<0 ){
                    if( gu1.top() < p.y() ){
                        e.getJob().stop();
                    }
                }else{
                    if( gu1.top() > p.y() ){
                        e.getJob().stop();
                    }
                }
            });

            if( ydiff<0 ){
                mVert.direction(Direction.UP);
            }else{
                mVert.direction(Direction.DOWN);
            }

            jobs.add(mVert);
        }

        if( jobs.size()>1 ){
            for( int i=0; i<(jobs.size()-1);i++ ){
                var job = jobs.get(i);
                var nextJobIdx = i+1;
                job.onStopped( e -> {
                    System.out.println("start "+nextJobIdx+" job");
                    var nextJob = jobs.get(nextJobIdx);
                    gu.setJob(nextJob);
                    nextJob.start();
                });
            }
        }

        jobs.forEach( job -> {
            movings.add(job);
            job.setEstimationOnly(true);
        });

        if( jobs.size()>0 ){
            var job = jobs.get(0);
            gu.setJob(job);
            job.start();
        }
    }

    protected void deleteSelected(){
        selection.forEach(gameUnits::remove);
        selection.clear();
    }
    protected void selectAll(){
        selection.addAll( gameUnits );
    }
    //endregion

    protected final PropertySheet propertySheet = new PropertySheet();

    //region browser
    protected final TreeTableNodeBasic root = new TreeTableNodeBasic("root" );
    protected final TreeTableNodeBasic toolsConfig = new TreeTableNodeBasic("tools");
    { root.append(toolsConfig); }
    { toolsConfig.append(new TreeTableNodeBasic(movingToolOptions)); }
    protected TreeTable browser = new TreeTable();
    {
        browser.setRoot(root);
        browser.setRootVisible(false);
        root.expand();
        browser.addTableListener(new TableListener() {
            @Override
            public void tableEvent(TableEvent tableEvent){
                if( tableEvent instanceof TableEvent.FocusedRowChanged  ){
                    var frc = (TableEvent.FocusedRowChanged)tableEvent;
                    var node = browser.getNodeOf(frc.getCurrentRow());
                    if( node!=null ){
                        var ref = node.getData();
                        if( !(ref instanceof String) ){
                            propertySheet.edit(ref);
                        }
                    }
                }
            }
        });
    }
    //endregion

    //region construct frame
    public static void main(String[] args){
        try {
            for ( UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }

        SwingUtilities.invokeLater(()->{
            MovingFrame frame = new MovingFrame();
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setTitle("moving");

            try{
                frame.setIconImage(ImageIO.read(MovingFrame.class.getResource("/frameIco-01.png")));
            } catch( IOException e ) {
                e.printStackTrace();
            }

            JSplitPane splitPane = new JSplitPane();
            splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
            splitPane.setResizeWeight(1.0d);

            frame.getContentPane().setLayout(new BorderLayout());

            JSplitPane pnl2 = new JSplitPane();
            pnl2.setOrientation(JSplitPane.VERTICAL_SPLIT);
            pnl2.setLeftComponent(frame.control);
            pnl2.setRightComponent(frame.browser);
            frame.control.setMinimumSize(new Dimension(250,250));
            frame.control.setPreferredSize(new Dimension(250,250));

            JPanel pnl1 = new JPanel();
            pnl1.setLayout(new BorderLayout());
            pnl1.add(frame.view);
            pnl1.add(pnl2, BorderLayout.LINE_START);

            frame.getContentPane().add(splitPane);
            splitPane.setLeftComponent( pnl1 );
            splitPane.setRightComponent( frame.propertySheet );

            SwingListener.onWindowOpened(frame, e->{ frame.timer.start(); });
            SwingListener.onWindowClosing(frame, e->{ frame.timer.stop(); });

            frame.view.setFocusable(true);
            frame.view.setRequestFocusEnabled(true);

            SwingListener.onWindowOpened(
                frame,
                GuiUtil.windowReciver().relativeSize(0.7, 0.7).center().build());
            frame.setVisible(true);
        });
    }
    //endregion
}
