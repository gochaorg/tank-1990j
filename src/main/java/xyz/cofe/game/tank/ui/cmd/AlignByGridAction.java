package xyz.cofe.game.tank.ui.cmd;

import xyz.cofe.game.tank.ui.GridBinding;
import xyz.cofe.game.tank.ui.canvas.Grid;
import xyz.cofe.game.tank.ui.SelectAction;
import xyz.cofe.game.tank.unt.Figura;
import xyz.cofe.game.tank.unt.Scene;

import java.util.Set;
import java.util.function.Supplier;

public class AlignByGridAction implements SelectAction, GridBinding {
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


    @Override
    public void execute(Scene scene, Set<Figura<?>> selection) {
        if( scene==null )throw new IllegalArgumentException( "scene==null" );
        if( selection==null )throw new IllegalArgumentException( "selection==null" );
        var grid = getGrid();
        if( grid==null )return;

        for( var f : selection ){
            f.location( grid.nearestPoint(f.leftTopPoint()) );
        }
    }
}
