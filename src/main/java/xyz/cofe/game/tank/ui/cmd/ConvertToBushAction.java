package xyz.cofe.game.tank.ui.cmd;

import xyz.cofe.game.tank.ui.SelectAction;
import xyz.cofe.game.tank.ui.tool.SelectTool;
import xyz.cofe.game.tank.unt.Brick;
import xyz.cofe.game.tank.unt.Bush;
import xyz.cofe.game.tank.unt.Figura;
import xyz.cofe.game.tank.unt.Scene;

import java.util.LinkedHashSet;
import java.util.Set;

public class ConvertToBushAction implements SelectAction {
    private SelectTool selectTool;
    public SelectTool getSelectTool() { return selectTool; }
    public void setSelectTool(SelectTool selectTool) { this.selectTool = selectTool; }

    @Override
    public void execute(Scene scene, Set<Figura<?>> selection) {
        if( selection==null || selection.isEmpty() || scene==null )return;

        Set<Figura<?>> set = new LinkedHashSet<>(selection);
        Set<Figura<?>> clones = new LinkedHashSet<>();
        for( Figura<?> f : set ){
            var c = new Bush(f);
            clones.add(c);
            scene.getFigures().add(c);
        }
        scene.getFigures().removeAll(set);

        if( selectTool!=null ){
            selectTool.getSelection().clear();
            selectTool.getSelection().addAll(clones);
        }
    }
}
