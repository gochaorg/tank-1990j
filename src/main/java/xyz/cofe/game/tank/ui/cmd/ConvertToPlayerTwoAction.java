package xyz.cofe.game.tank.ui.cmd;

import xyz.cofe.game.tank.unt.PlayerOne;
import xyz.cofe.game.tank.unt.PlayerTwo;

public class ConvertToPlayerTwoAction extends ConvertToAction<PlayerTwo> {
    public ConvertToPlayerTwoAction(){
        super(PlayerTwo::new);
    }
}
