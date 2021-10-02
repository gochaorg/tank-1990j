package xyz.cofe.game.tank.ui.tool;

import xyz.cofe.game.tank.geom.Point;
import xyz.cofe.game.tank.ui.GridBinding;
import xyz.cofe.game.tank.ui.MouseEv;
import xyz.cofe.game.tank.ui.SnapToGridProperty;
import xyz.cofe.game.tank.ui.canvas.Grid;
import xyz.cofe.game.tank.unt.SceneProperty;
import xyz.cofe.game.tank.ui.Tool;
import xyz.cofe.game.tank.unt.Figure;
import xyz.cofe.game.tank.unt.Scene;

import java.awt.image.BufferedImage;
import java.util.function.Supplier;

public abstract class LevelBrickTool<SELF extends LevelBrickTool<SELF>>
    extends AbstractTool implements Tool, SceneProperty, GridBinding, SnapToGridProperty {
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

    //region grid : Supplier<Grid>
    protected Supplier<Grid> grid;
    public Grid getGrid() {
        return grid!=null ? grid.get() : null;
    }

    public void setGrid(Supplier<Grid> grid) {
        this.grid = grid;
    }
    public void setGrid(Grid grid) {
        this.grid = ()->grid;
    }
    //endregion
    //region snapToGrid : boolean
    protected boolean snapToGrid = false;

    public boolean isSnapToGrid() {
        return snapToGrid;
    }

    public void setSnapToGrid(boolean snapToGrid) {
        this.snapToGrid = snapToGrid;
    }
    //endregion

    protected abstract Figure<?> buildFigura();

    @Override
    public void onMouseClicked(MouseEv pt) {
        var scene = getScene();
        if( scene!=null ){
            var fig = buildFigura();
            if( fig!=null ){
                Point loc = pt;

                var grid = getGrid();
                if( snapToGrid && grid!=null ){
                    loc = grid.nearestPoint(loc);
                }

                fig.location(loc);
                scene.getFigures().add(fig);
            }
        }
    }
}
