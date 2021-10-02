package xyz.cofe.game.tank.ui.tool;

import xyz.cofe.game.tank.sprite.SpritesData;
import xyz.cofe.game.tank.ui.Tool;
import xyz.cofe.game.tank.unt.Figure;
import xyz.cofe.game.tank.unt.Water;

/**
 * Инструмент для построения ...
 */
public class WaterTool extends LevelBrickTool<WaterTool> implements Tool {
    public WaterTool() {
        super("Water", SpritesData.lvl_water.images().get(0));
    }

    @Override
    protected Figure<?> buildFigura() {
        return new Water();
    }
}
