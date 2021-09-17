package xyz.cofe.game.tank.ui.tool;

import xyz.cofe.game.tank.ui.MouseEv;
import xyz.cofe.game.tank.unt.SceneProperty;
import xyz.cofe.game.tank.ui.Tool;
import xyz.cofe.game.tank.unt.Figura;
import xyz.cofe.game.tank.unt.Scene;

import java.awt.image.BufferedImage;

public abstract class LevelBrickTool<SELF extends LevelBrickTool<SELF>> implements Tool, SceneProperty {
    public LevelBrickTool(String name, BufferedImage image){
        if( name==null )throw new IllegalArgumentException( "name==null" );
        if( image==null )throw new IllegalArgumentException( "image==null" );
        this.name = name;
        this.image = image;
    }

    //region name : String
    protected String name;

    @Override
    public String name() {
        return name;
    }
    //endregion
    //region image : BufferedImage
    protected BufferedImage image;

    @Override
    public BufferedImage image() {
        return image;
    }
    //endregion
    //region scene : Scene
    protected Scene scene;
    public Scene getScene() { return scene; }
    public void setScene(Scene scene) { this.scene = scene; }
    //endregion

    protected abstract Figura<?> buildFigura();

    @Override
    public void onMouseClicked(MouseEv pt) {
        var scene = getScene();
        if( scene!=null ){
            var fig = buildFigura();
            if( fig!=null ){
                fig.location(pt);
                scene.getFigures().add(fig);
            }
        }
    }
}
