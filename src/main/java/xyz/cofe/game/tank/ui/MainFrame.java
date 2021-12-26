package xyz.cofe.game.tank.ui;

import xyz.cofe.game.tank.stat.TCounter;
import xyz.cofe.game.tank.gcycle.GameCycle;
import xyz.cofe.game.tank.gcycle.UserFire;
import xyz.cofe.game.tank.gcycle.UserMoveStart;
import xyz.cofe.game.tank.gcycle.UserMoveStop;
import xyz.cofe.game.tank.unt.Direction;
import xyz.cofe.game.tank.unt.Note;
import xyz.cofe.game.tank.unt.Scene;
import xyz.cofe.gui.swing.SwingListener;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.GeneralPath;

public class MainFrame extends JFrame {
    public MainFrame(){
        setTitle("main game frame");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        SwingListener.onWindowLostFocus(this,ev -> gameStop());
        SwingListener.onWindowGainedFocus(this, ev -> gameStart());
        getContentPane().setLayout(new BorderLayout());

        var mainRender = new MainRender();
        getContentPane().add(mainRender);

        SwingListener.onWindowOpened(this, ev -> {mainRender.requestFocus();});

        SwingListener.onWindowOpened(this, ev -> {
            renderTimer = new Timer(50, tev -> {
                onTimer();
            });
            renderTimer.start();
        });
        SwingListener.onWindowClosing(this, ev -> {
            if( renderTimer!=null ){
                renderTimer.stop();
            }
        });

        var holdDirection = new Direction[]{ null };

        SwingListener.onKeyPressed(mainRender, ev -> {
            // 38 - up
            // 40 - down
            // 37 - left
            // 39 - right
            // 32 - space
            // 87 - w
            // 83 - s
            // 65 - a
            // 68 - d
            // 81 - q
            // 69 - e
            // 49 - 1
            // 50 - 2
            // 51 - 3
            // 10 - enter
            // 8  - backspace
            // 27 - escape
            switch ( ev.getKeyCode() ) {
                case KeyEvent.VK_RIGHT:
                    if( holdDirection[0]==null ) {
                        gameCycle.userInput(new UserMoveStart(Direction.RIGHT));
                        holdDirection[0] = Direction.RIGHT;
                    }
                    break;
                case KeyEvent.VK_LEFT:
                    if( holdDirection[0]==null ) {
                        gameCycle.userInput(new UserMoveStart(Direction.LEFT));
                        holdDirection[0] = Direction.LEFT;
                    }
                    break;
                case KeyEvent.VK_UP:
                    if( holdDirection[0]==null ) {
                        gameCycle.userInput(new UserMoveStart(Direction.UP));
                        holdDirection[0] = Direction.UP;
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if( holdDirection[0]==null ) {
                        gameCycle.userInput(new UserMoveStart(Direction.DOWN));
                        holdDirection[0] = Direction.DOWN;
                    }
                    break;
                case KeyEvent.VK_SPACE:
                    gameCycle.userInput(new UserFire());
                    break;
            }
        });
        SwingListener.onKeyReleased(mainRender, ev -> {
            switch ( ev.getKeyCode() ) {
                case KeyEvent.VK_RIGHT:
                    if( holdDirection[0]==Direction.RIGHT ) {
                        gameCycle.userInput(new UserMoveStop());
                        holdDirection[0] = null;
                    }
                    break;
                case KeyEvent.VK_LEFT:
                    if( holdDirection[0]==Direction.LEFT ) {
                        gameCycle.userInput(new UserMoveStop());
                        holdDirection[0] = null;
                    }
                    break;
                case KeyEvent.VK_UP:
                    if( holdDirection[0]==Direction.UP ) {
                        gameCycle.userInput(new UserMoveStop());
                        holdDirection[0] = null;
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if( holdDirection[0]==Direction.DOWN ) {
                        gameCycle.userInput(new UserMoveStop());
                        holdDirection[0] = null;
                    }
                    break;
                case KeyEvent.VK_SPACE:
                    break;
            }
        });
        SwingListener.onFocusLost(mainRender, ev -> {
            gameCycle.userInput(new UserMoveStop());
            holdDirection[0] = null;
        });
    }

    //region scene : Scene
    protected Scene scene;
    public Scene getScene() { return scene; }
    public void setScene(Scene scene) {
        if( gameCycle!=null )gameCycle = null;
        this.scene = scene;
        if( scene!=null ){
            gameCycle = new GameCycle(scene);
        }
    }
    //endregion

    /**
     * Игровой цикл
     */
    protected GameCycle gameCycle;

    private TCounter fullCycleTC = new TCounter(1000);
    private TCounter gameCycleTC = new TCounter(1000);
    private TCounter repaintForceTC = new TCounter(1000);
    private TCounter paintTC = new TCounter(1000);
    private boolean timeCounters = false;

    private int timesIdx = 0;
    private long lastEcho = 0;
    protected void onTimer(){
        fullCycleTC.start();

        if( gameCycle!=null ){
            if( timeCounters )gameCycleTC.start();
            gameCycle.next();
            if( timeCounters )gameCycleTC.stop();
        }

        if( timeCounters )repaintForceTC.start();
        repaintForce();
        if( timeCounters )repaintForceTC.stop();

        if( timeCounters )fullCycleTC.stop();

        ////////////
        if( timeCounters ) {
            fullCycleTC.echo(2000, fc -> fc.print("full "));
            gameCycleTC.echo(2000, fc -> fc.print("game "));
            repaintForceTC.echo(2000, fc -> fc.print("repaint "));
            paintTC.echo(2000, fc -> fc.print("paint "));
        }
    }

    /**
     * Форсирование перерисовки окна
     */
    protected void repaintForce(){
        this.repaint();
        Toolkit.getDefaultToolkit().sync();
    }

    protected Timer renderTimer;

    /**
     * Запуск цикла игры
     */
    public void gameStart(){
        var gc = gameCycle;
        if( gc!=null )gc.start();
    }

    /**
     * Остановка цикла игры
     */
    public void gameStop(){
        var gc = gameCycle;
        if( gc!=null )gc.stop();
    }

    /**
     * Основной компонент рендера сцены
     */
    public class MainRender extends JComponent {
        public MainRender(){
            setFocusable(true);
            setBackground(Color.black);
            setDoubleBuffered(true);
        }

        private Shape sceneBorder;
        private Shape getSceneBorder(){
            if( sceneBorder!=null )return sceneBorder;

            double b = scene.getBorderWidth();
            if( b<=0 )return null;

            double w = scene.getWidth();
            double h = scene.getHeight();

            GeneralPath p = new GeneralPath();
            p.moveTo(0,0);
            p.lineTo(w,0);
            p.lineTo(w,h);
            p.lineTo(0,h);
            p.lineTo(0,0);
            p.lineTo(0-b,0);
            p.lineTo(0-b,h+b);
            p.lineTo(w+b,h+b);
            p.lineTo(w+b,0-b);
            p.lineTo(0-b,0-b);
            p.lineTo(0-b,0);
            p.closePath();
            return p;
        }

        @Override
        protected void paintComponent(Graphics g) {
            try {
                if( timeCounters )paintTC.start();

                if (!(g instanceof Graphics2D)) {
                    super.paintComponent(g);
                    return;
                }

                Graphics2D gs = (Graphics2D) g;
                gs.setPaint(getBackground());
                gs.fillRect(0, 0, getWidth(), getHeight());

                Color sceneBorderColor = scene.getBorderColor();
                Shape sceneBorder = getSceneBorder();
                if( sceneBorder!=null && sceneBorderColor!=null ){
                    gs.setPaint(sceneBorderColor);
                    gs.fill(sceneBorder);
                }

                if (scene == null) return;
                for (var figure : scene.getFigures()) {
                    if (figure == null) continue;
                    if (figure instanceof Note) continue;
                    figure.draw(gs);
                }
            } finally {
                if( timeCounters )paintTC.stop();
            }
        }
    }
}
