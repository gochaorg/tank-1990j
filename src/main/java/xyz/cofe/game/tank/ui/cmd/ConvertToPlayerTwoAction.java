package xyz.cofe.game.tank.ui.cmd;

import xyz.cofe.game.tank.sprite.SpritesData;
import xyz.cofe.game.tank.unt.PlayerOne;
import xyz.cofe.game.tank.unt.PlayerTwo;

public class ConvertToPlayerTwoAction extends ConvertToAction<PlayerTwo> {
    public ConvertToPlayerTwoAction(){
        super(SpritesData.player_two_right_0.images().get(0), PlayerTwo::new);
    }
}
