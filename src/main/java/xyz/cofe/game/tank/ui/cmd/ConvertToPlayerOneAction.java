package xyz.cofe.game.tank.ui.cmd;

import xyz.cofe.game.tank.unt.Bullet;
import xyz.cofe.game.tank.unt.PlayerOne;

public class ConvertToPlayerOneAction extends ConvertToAction<PlayerOne> {
    public ConvertToPlayerOneAction(){
        super(PlayerOne::new);
    }
}
