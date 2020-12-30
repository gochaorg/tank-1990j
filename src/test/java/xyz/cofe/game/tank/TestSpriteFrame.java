package xyz.cofe.game.tank;

import xyz.cofe.fn.Tuple3;
import xyz.cofe.game.tank.unt.*;
import xyz.cofe.gui.swing.SwingListener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TestSpriteFrame extends JFrame {
    private final Render1 render1 = new Render1();
    private final Timer timer = new Timer(20, e -> {
        int w = render1.getWidth();
        int h = render1.getHeight();

        render1.paintImmediately(0,0,w,h);

        Toolkit.getDefaultToolkit().sync();
    });

    public static class Render1 extends JComponent {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if( g instanceof Graphics2D ){
                render((Graphics2D) g);
            }
        }

        private SpritesData sd1 = SpritesData.player_one_right_1;
        private SpriteLine sprite = new SpriteLine().images(sd1.images()).duration(sd1.duration);
        private SpriteFigura figura = sprite.toFigure().location(10,50 );

        private List<Tuple3<Integer,Integer,String>> labels = new ArrayList<>();
        private List<SpriteFigura> figures = new ArrayList<>(){{
            SpritesData[] items = new SpritesData[]{
                SpritesData.item_granade,
                SpritesData.item_invincibility,
                SpritesData.item_life,
                SpritesData.item_protect,
                SpritesData.item_star,
                SpritesData.item_time,
                SpritesData.effect_explode,
                SpritesData.effect_spawn,
                SpritesData.bullet,
                SpritesData.chars,
                SpritesData.enemy_fast_bonus_down,
                SpritesData.signs,
            };
            var labelsStr = ("granade, invincibility, life, protect, star, time, explode").split("\\s*,\\s*");
            AtomicInteger idx = new AtomicInteger(-1);
            Arrays.stream(items).forEach( itm -> {
                int i = idx.incrementAndGet();
                int x = i % 5;
                int y = i / 5;
                String label = labelsStr[i % labelsStr.length];
                var sl = new SpriteLine().images(itm.images()).duration(itm.duration);
                var fig = sl.toFigure().location(50+100*x, 50+40*y);
                add(fig.startAnimation());
                labels.add(Tuple3.of(
                    (Integer) (int) fig.left(),
                    (Integer) (int) fig.top(),
                    label
                ));
            });
        }};

        private long frame = -1;
        private long frame0started = 0;

        //private Brick brick = new Brick().location(10,100);
        private List<LevelBrick<?>> bricks = List.of(
            new Bush().location(10,100),
            new Slide().location(10,140),
            new Steel().location(10,180),
            new Water().location(10,220),
            new Brick().location(10,260)
        );

        protected void render(Graphics2D gs){
            frame++;
            if( frame0started==0 ){
                frame0started = System.currentTimeMillis();
            }

            gs.setPaint(Color.black);
            gs.fillRect(0,0,getWidth(),getHeight());

            if( !sprite.isAnimationRunning() ){
                sprite.startAnimation();
            }

            figura.draw(gs);
            bricks.forEach(b->b.draw(gs));

            gs.setPaint(Color.black);
            gs.drawString("frame="+Long.toString(frame)+" duration="+(System.currentTimeMillis()-frame0started),10,30);

            figures.forEach( fig -> fig.draw(gs) );
            labels.forEach( label -> {
                gs.setPaint(Color.black);
                gs.drawString(label.c(), label.a()+33, label.b()+20);

                gs.setPaint(Color.white);
                gs.drawString(label.c(), label.a()+34, label.b()+19);
            });
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
