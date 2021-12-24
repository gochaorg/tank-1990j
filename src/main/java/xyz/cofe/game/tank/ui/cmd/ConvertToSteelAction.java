package xyz.cofe.game.tank.ui.cmd;

import xyz.cofe.game.tank.sprite.SpritesData;
import xyz.cofe.game.tank.unt.Steel;
import xyz.cofe.game.tank.unt.Water;

public class ConvertToSteelAction extends ConvertToAction<Steel> {
    public ConvertToSteelAction(){
        super(SpritesData.lvl_white.images().get(0), Steel::new);
    }
}
