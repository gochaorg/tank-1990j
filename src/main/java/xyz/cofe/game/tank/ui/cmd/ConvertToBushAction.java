package xyz.cofe.game.tank.ui.cmd;

import xyz.cofe.game.tank.sprite.SpritesData;
import xyz.cofe.game.tank.unt.Bush;

public class ConvertToBushAction extends ConvertToAction<Bush> {
    public ConvertToBushAction(){
        super(SpritesData.lvl_bush.images().get(0), Bush::new);
    }
}
