package xyz.cofe.game.tank.ui.tool;

import xyz.cofe.game.tank.sprite.SpritesData;
import xyz.cofe.game.tank.ui.Tool;
import xyz.cofe.game.tank.unt.Brick;
import xyz.cofe.game.tank.unt.Figure;

/**
 * Инструмент для построения стен
 */
public class PlayerTwoTool extends LevelBrickTool<PlayerTwoTool> implements Tool {
    public PlayerTwoTool(){
        super("Brick", SpritesData.lvl_brick.images().get(0) );
    }

    @Override
    protected Figure<?> buildFigura() {
        return new Brick();
    }
}
