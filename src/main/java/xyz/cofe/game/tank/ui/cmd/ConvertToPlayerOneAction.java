package xyz.cofe.game.tank.ui.cmd;

import xyz.cofe.game.tank.sprite.SpritesData;
import xyz.cofe.game.tank.unt.Bullet;
import xyz.cofe.game.tank.unt.PlayerOne;

public class ConvertToPlayerOneAction extends ConvertToAction<PlayerOne> {
    public ConvertToPlayerOneAction(){
        super(SpritesData.player_one_right_0.images().get(0), PlayerOne::new);
    }
}
