package xyz.cofe.game.tank;

import xyz.cofe.gui.swing.SwingListener;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class TestSpriteFrame extends JFrame {
    private final Render1 render1 = new Render1();
    private final Timer timer = new Timer(20, e -> {
        int w = render1.getWidth();
        int h = render1.getHeight();

        //RepaintManager rm = RepaintManager.currentManager(this);
        //rm.setDoubleBufferingEnabled(false);

        //render1.invalidate();
        //render1.revalidate();
        render1.paintImmediately(0,0,w,h);

        Toolkit.getDefaultToolkit().sync();
    });

    public static class Render1 extends JComponent {
//        @Override
//        public void paint(Graphics g){
//            //super.paint(g);
//            if( g instanceof Graphics2D ){
//                render((Graphics2D) g);
//            }
//        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if( g instanceof Graphics2D ){
                render((Graphics2D) g);
            }
        }

        private SpriteLine sprite = new SpriteLine().configure( s -> {
            var sd = SpritesData.player_one_right_0;
            s.setSprites( sd.images().stream().map(Sprite::new) );
            s.setDuration( 0.025 );
        });

        private long frame = -1;
        private long frame0started = 0;

        protected void render(Graphics2D gs){
            frame++;
            if( frame0started==0 ){
                frame0started = System.currentTimeMillis();
            }

            gs.setPaint(Color.white);
            gs.fillRect(0,0,getWidth(),getHeight());

            if( !sprite.isRunning() ){
                sprite.start();
            }

            sprite.draw(gs, 10, 70);

            gs.setPaint(Color.black);
            gs.drawString("frame="+Long.toString(frame)+" duration="+(System.currentTimeMillis()-frame0started),10,30);
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
