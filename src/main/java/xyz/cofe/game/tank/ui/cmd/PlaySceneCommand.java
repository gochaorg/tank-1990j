package xyz.cofe.game.tank.ui.cmd;

import xyz.cofe.game.tank.ui.MainFrame;
import xyz.cofe.game.tank.unt.Figura;
import xyz.cofe.game.tank.unt.Scene;

import javax.swing.WindowConstants;
import java.util.ArrayList;
import java.util.function.Function;
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

        mf.setScene(new Scene(scene,
            new ArrayList<Figura<?>>(
                scene.getFigures().stream()
                    .map( f -> {
                        f.setNotification(false);
                        return f;
                    } )
                    .collect(Collectors.toList())
            )
        ));

        mf.setVisible(true);
        mf.toFront();
        mf.gameStart();
    }
}
