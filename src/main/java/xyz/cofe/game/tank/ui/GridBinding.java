package xyz.cofe.game.tank.ui;

import xyz.cofe.game.tank.ui.canvas.Grid;

import java.util.function.Supplier;

public interface GridBinding {
    void setGrid(Supplier<Grid> grid);
}
