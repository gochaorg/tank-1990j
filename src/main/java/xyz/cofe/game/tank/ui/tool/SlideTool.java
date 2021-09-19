package xyz.cofe.game.tank.ui.tool;

import xyz.cofe.game.tank.sprite.SpritesData;
import xyz.cofe.game.tank.ui.Tool;
import xyz.cofe.game.tank.unt.Bush;
import xyz.cofe.game.tank.unt.Figura;
import xyz.cofe.game.tank.unt.Slide;

/**
 * Инструмент для построения ...
 */
public class SlideTool extends LevelBrickTool<SlideTool> implements Tool {
    public SlideTool() {
        super("Slide", SpritesData.lvl_slide.images().get(0));
    }

    @Override
    protected Figura<?> buildFigura() {
        return new Slide();
    }
}
