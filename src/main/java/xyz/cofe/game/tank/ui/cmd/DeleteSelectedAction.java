package xyz.cofe.game.tank.ui.cmd;

import xyz.cofe.game.tank.ui.SelectAction;
import xyz.cofe.game.tank.unt.Figura;
import xyz.cofe.game.tank.unt.Scene;

import java.util.Set;

/**
 * Удаление выделенных объектов со сцены
 */
public class DeleteSelectedAction implements SelectAction {
    @Override
    public String name() {
        return "Delete selected";
    }

    @Override
    public void execute(Scene scene, Set<Figura<?>> selection) {
        if( scene==null )throw new IllegalArgumentException( "scene==null" );
        if( selection==null )throw new IllegalArgumentException( "selection==null" );
        scene.getFigures().removeAll(selection);
    }
}
