package xyz.cofe.game.tank.ui.cmd;

import xyz.cofe.game.tank.ui.MainFrame;
import xyz.cofe.game.tank.unt.Figure;
import xyz.cofe.game.tank.unt.LevelBrick;
import xyz.cofe.game.tank.unt.Scene;

import java.util.ArrayList;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class PlaySceneCommand implements Runnable {
    //region scene
    protected Supplier<Scene> scene;
    public void setScene(Supplier<Scene> scene){
        this.scene = scene;
    }
    public void setScene(Scene scene){
        this.scene = scene!=null ? ()->scene : null;
    }
    public Scene getScene(){
        var s = scene;
        return s!=null ? s.get() : null;
    }
    public PlaySceneCommand scene(Supplier<Scene> scene){
        setScene(scene);
        return this;
    }
    //endregion

    @Override
    public void run() {
        var scene = getScene();
        if( scene==null )return;

        MainFrame mf = new MainFrame();
        mf.setTitle("Play scene");
        mf.setDefaultCloseOperation(MainFrame.DISPOSE_ON_CLOSE);
        mf.setSize(800,600);
        mf.setLocationRelativeTo(null);

        var list =
            new ArrayList<Figure<?>>(
                scene.getFigures().stream()
                    .map( f -> {
                        f = f.clone();
                        f.setNotification(false);
                        return f;
                    } )
                    .collect(Collectors.toList())
            );

        mf.setScene(new Scene(scene,list));

        var levelBricks =
            list.stream()
                .filter( f -> f instanceof LevelBrick )
                .map( f -> (LevelBrick<?>)f )
                    .collect(Collectors.toList());

        var lst = new ArrayList<LevelBrick<?>>();
        for( LevelBrick<?> brick : levelBricks ){
            lst.clear();
            if( brick.isUpLeft() ){
                var b = brick.clone();
                b.state(LevelBrick.UL_BRICK);
                lst.add(b);
            }
            if( brick.isUpRight() ){
                var b = brick.clone();
                b.state(LevelBrick.UL_BRICK);
                b.location(
                    brick.left()+brick.sprite().image().getWidth(),
                    brick.top());
                lst.add(b);
            }
            if( brick.isBottomLeft() ){
                var b = brick.clone();
                b.state(LevelBrick.UL_BRICK);
                b.location(
                    brick.left(),
                    brick.top()+brick.sprite().image().getHeight());
                lst.add(b);
            }
            if( brick.isBottomRight() ){
                var b = brick.clone();
                b.state(LevelBrick.UL_BRICK);
                b.location(
                    brick.left()+brick.sprite().image().getWidth(),
                    brick.top()+brick.sprite().image().getHeight());
                lst.add(b);
            }
            if( !lst.isEmpty() ){
                var idx = list.indexOf(brick);
                if( idx>=0 ){
                    list.remove(idx);
                    list.addAll(idx,lst);
                }
            }
        }

        mf.setVisible(true);
        mf.toFront();
        mf.gameStart();
    }
}
