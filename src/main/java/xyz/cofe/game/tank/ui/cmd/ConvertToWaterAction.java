package xyz.cofe.game.tank.ui.cmd;

import xyz.cofe.game.tank.sprite.SpritesData;
import xyz.cofe.game.tank.ui.SelectAction;
import xyz.cofe.game.tank.ui.tool.SelectTool;
import xyz.cofe.game.tank.unt.Brick;
import xyz.cofe.game.tank.unt.Figura;
import xyz.cofe.game.tank.unt.Scene;
import xyz.cofe.game.tank.unt.Water;

import java.util.LinkedHashSet;
import java.util.Set;

public class ConvertToWaterAction extends ConvertToAction<Water> {
    public ConvertToWaterAction(){
        super(SpritesData.lvl_water.images().get(0), Water::new);
    }
}
