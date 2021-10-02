package xyz.cofe.game.tank.ui.tool;

import xyz.cofe.game.tank.sprite.SpritesData;
import xyz.cofe.game.tank.ui.Tool;
import xyz.cofe.game.tank.unt.Figure;
import xyz.cofe.game.tank.unt.PlayerOne;

/**
 * Инструмент для построения стен
 */
public class PlayerOneTool extends LevelBrickTool<PlayerOneTool> implements Tool {
    public PlayerOneTool(){
        super("Player one", SpritesData.player_one_right_0.images().get(0) );
    }

    @Override
    protected Figure<?> buildFigura() {
        return new PlayerOne();
    }
}
