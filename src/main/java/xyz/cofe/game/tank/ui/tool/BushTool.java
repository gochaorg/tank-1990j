package xyz.cofe.game.tank.ui.tool;

import xyz.cofe.game.tank.sprite.SpritesData;
import xyz.cofe.game.tank.ui.Tool;
import xyz.cofe.game.tank.unt.Bush;
import xyz.cofe.game.tank.unt.Figure;

/**
 * Инструмент для построения ...
 */
public class BushTool extends LevelBrickTool<BushTool> implements Tool {
    public BushTool() {
        super("Bush", SpritesData.lvl_bush.images().get(0));
    }

    @Override
    protected Figure<?> buildFigura() {
        return new Bush();
    }
}
