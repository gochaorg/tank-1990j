package xyz.cofe.game.tank.ui.cmd;

import xyz.cofe.game.tank.ui.SelectAction;
import xyz.cofe.game.tank.ui.tool.SelectTool;
import xyz.cofe.game.tank.unt.Brick;
import xyz.cofe.game.tank.unt.Bush;
import xyz.cofe.game.tank.unt.Figura;
import xyz.cofe.game.tank.unt.Scene;

import java.util.LinkedHashSet;
import java.util.Set;

public class ConvertToBushAction extends ConvertToAction<Bush> {
    public ConvertToBushAction(){
        super(Bush::new);
    }
}
