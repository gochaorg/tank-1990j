package xyz.cofe.game.tank.ui.cmd;

import xyz.cofe.game.tank.ui.SelectAction;
import xyz.cofe.game.tank.ui.tool.SelectTool;
import xyz.cofe.game.tank.unt.Figura;
import xyz.cofe.game.tank.unt.Scene;

import java.util.ArrayList;
import java.util.Set;

public class DuplicateAction implements SelectAction {
    private SelectTool selectTool;
    public SelectTool getSelectTool() { return selectTool; }
    public void setSelectTool(SelectTool selectTool) { this.selectTool = selectTool; }

    @Override
    public void execute(Scene scene, Set<Figura<?>> selection) {
        System.out.println("duplicate");

        if( scene==null )return;
        if( selection==null || selection.isEmpty() )return;

        var clones = new ArrayList<Figura<?>>();
        for( var selected : selection ){
            var clone = selected.clone();
            clones.add(clone);
        }

        scene.getFigures().addAll(clones);
        var selTool = selectTool;
        if( selTool!=null ){
            selTool.getSelection().clear();
            selTool.getSelection().addAll(clones);
        }
    }
}
