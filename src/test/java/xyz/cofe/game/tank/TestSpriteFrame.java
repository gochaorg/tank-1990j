package xyz.cofe.game.tank;

import xyz.cofe.game.tank.geom.Rect;
import xyz.cofe.game.tank.unt.*;
import xyz.cofe.gui.swing.SwingListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class TestSpriteFrame extends JFrame {
    private final Render1 render1 = new Render1();
    private final Timer timer = new Timer(20, e -> {
        int w = render1.getWidth();
        int h = render1.getHeight();

        render1.paintImmediately(0,0,w,h);

        Toolkit.getDefaultToolkit().sync();
    });

    public static class Render1 extends JComponent {
        {
            setFocusable(true);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if( g instanceof Graphics2D ){
                render((Graphics2D) g);
            }
        }

        List<Bullet> bullets = new ArrayList<>();

        private PlayerOne playerOne = new PlayerOne().location(10,50 ).startAnimation();

        {
            SwingListener.onKeyPressed(this, ev -> {
                switch( ev.getKeyCode() ){
                    case KeyEvent.VK_RIGHT:
                        playerOne.move(Direction.RIGHT, playerOne.width());
                        break;
                    case KeyEvent.VK_LEFT:
                        playerOne.move(Direction.LEFT, playerOne.width());
                        break;
                    case KeyEvent.VK_UP:
                        playerOne.move(Direction.UP, playerOne.height());
                        break;
                    case KeyEvent.VK_DOWN:
                        playerOne.move(Direction.DOWN, playerOne.height());
                        break;
                    case KeyEvent.VK_1: playerOne.setPlayerState(PlayerState.Level0); break;
                    case KeyEvent.VK_2: playerOne.setPlayerState(PlayerState.Level1); break;
                    case KeyEvent.VK_3: playerOne.setPlayerState(PlayerState.Level2); break;
                    case KeyEvent.VK_4: playerOne.setPlayerState(PlayerState.Level3); break;
                    case KeyEvent.VK_Z: playerOne.stop(); break;
                    case KeyEvent.VK_SPACE:
                        bullets.add(playerOne.createBullet());
                        break;
                    default:
                }
            });
        }

        private List<LevelBrick<?>> bricks = List.of(
            new Bush().location(10,100),
            new Slide().location(10,140),
            new Steel().location(10,180),
            new Water().location(10,220),
            new Brick().location(10,260),
            new Brick().location(300,50),
            new Brick().location(300,100),
            new Brick().location(300,200)
        );

//        { playerOne.collision(bricks); }

        protected void render(Graphics2D gs){
            gs.setPaint(Color.black);
            gs.fillRect(0,0,getWidth(),getHeight());

            playerOne.run();
            playerOne.draw(gs);
            bricks.forEach(b->b.draw(gs));

            bullets.forEach(b->b.draw(gs));
            //draw(gs,playerOne,Color.red);
//            playerOne.currentSpriteLine().sprite().ifPresent( sp -> {
//                var r = new MutableRect(sp.bounds());
//                r.location(r.left()+playerOne.left(), r.top()+playerOne.top());
//                draw(gs,r,Color.green);
//            });
        }

        public static void draw(Graphics2D gs, Rect rect, Paint color){
            if( gs==null )throw new IllegalArgumentException( "gs==null" );
            if( rect==null )throw new IllegalArgumentException( "rect==null" );
            if( color==null )throw new IllegalArgumentException( "color==null" );
            gs.setPaint(color);
            gs.drawRect( (int)rect.left(), (int)rect.top(), (int)rect.width(), (int)rect.height() );
        }
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(()->{
            TestSpriteFrame frame = new TestSpriteFrame();
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setTitle("test sprint");
            frame.setSize(600,600);
            frame.setLocationRelativeTo(null);

            frame.getContentPane().setLayout(new BorderLayout());
            frame.getContentPane().add(frame.render1);

            frame.setVisible(true);
            frame.timer.start();
            SwingListener.onWindowClosing(frame, e -> frame.timer.stop());
        });
    }
}
