package xyz.cofe.game.tank.ui.cmd;

import xyz.cofe.game.tank.sprite.SpritesData;
import xyz.cofe.game.tank.unt.Brick;

public class ConvertToBrickAction extends ConvertToAction<Brick> {
    public ConvertToBrickAction(){
        super(SpritesData.lvl_brick.images().get(0), Brick::new);
    }
}
