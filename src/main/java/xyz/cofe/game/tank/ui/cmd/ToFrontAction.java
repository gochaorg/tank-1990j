package xyz.cofe.game.tank.ui.cmd;

import xyz.cofe.game.tank.ui.SelectAction;
import xyz.cofe.game.tank.unt.Figure;
import xyz.cofe.game.tank.unt.Scene;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ToFrontAction implements SelectAction {
    @Override
    public void execute(Scene scene, Set<Figure<?>> selection) {
        if( scene==null || selection==null || selection.isEmpty() )return;

        List<Figure<?>> selected = new ArrayList<>(selection);
        scene.getFigures().removeAll(selected);
        scene.getFigures().addAll(selected);
    }
}
