package xyz.cofe.game.tank.ui.cmd;

import xyz.cofe.game.tank.sprite.SpritesData;
import xyz.cofe.game.tank.unt.Water;

public class ConvertToWaterAction extends ConvertToAction<Water> {
    public ConvertToWaterAction(){
        super(SpritesData.lvl_water.images().get(0), Water::new);
    }
}
