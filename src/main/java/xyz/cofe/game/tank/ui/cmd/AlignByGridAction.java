package xyz.cofe.game.tank.ui.cmd;

import xyz.cofe.game.tank.ui.canvas.Grid;
import xyz.cofe.game.tank.ui.SelectAction;
import xyz.cofe.game.tank.unt.Figura;
import xyz.cofe.game.tank.unt.Scene;

import java.util.Set;

public class AlignByGridAction implements SelectAction {
    //region grid : Grid
    protected Grid grid;

    public Grid getGrid() {
        return grid;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }
    //endregion

    @Override
    public void execute(Scene scene, Set<Figura<?>> selection) {
        if( scene==null )throw new IllegalArgumentException( "scene==null" );
        if( selection==null )throw new IllegalArgumentException( "selection==null" );
        if( grid==null )return;

        for( var f : selection ){
            f.location( grid.nearestPoint(f.leftTopPoint()) );
        }
    }
}
