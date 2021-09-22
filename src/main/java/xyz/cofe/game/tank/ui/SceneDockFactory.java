package xyz.cofe.game.tank.ui;

import bibliothek.gui.dock.common.MultipleCDockableFactory;
import xyz.cofe.game.tank.Observers;

import java.util.*;

public class SceneDockFactory implements MultipleCDockableFactory<SceneDock, SceneDockState> {
    public final Observers<SceneDock> onSceneDockCreated = new Observers<>();

    @Override
    public SceneDockState write(SceneDock dockable) {
        var l = dockable.createStoreLayout();
        hist.computeIfAbsent(dockable, x -> new LinkedHashSet<>()).add(l);
        return l;
    }

    private WeakHashMap<SceneDock, Set<SceneDockState>> hist = new WeakHashMap<>();

    @Override
    public SceneDock read(SceneDockState layout) {
        if( layout!=null ){
            var sd = new SceneDock(this, layout);
            hist.computeIfAbsent(sd,x -> new LinkedHashSet<>()).add(layout);
            onSceneDockCreated.fire(sd);
            return sd;
        }
        return null;
    }

    @Override
    public boolean match(SceneDock dockable, SceneDockState layout) {
        if( dockable!=null && layout!=null ){
            var s = hist.get(dockable);
            if( s!=null && s.contains(layout) ){
                return true;
            }
        }
        return false;
    }

    @Override
    public SceneDockState create() {
        return new SceneDockState();
    }
}
