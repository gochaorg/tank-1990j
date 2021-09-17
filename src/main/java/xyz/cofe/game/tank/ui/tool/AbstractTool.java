package xyz.cofe.game.tank.ui.tool;

import xyz.cofe.ecolls.ListenersHelper;
import xyz.cofe.game.tank.ui.Tool;

import java.util.Set;

public abstract class AbstractTool implements Tool {
    protected final ListenersHelper<ToolListener,ToolEvent> listeners = new ListenersHelper<>(ToolListener::toolEvent);

    public boolean hasListener(ToolListener listener) {
        return listeners.hasListener(listener);
    }

    public Set<ToolListener> getListeners() {
        return listeners.getListeners();
    }

    public AutoCloseable addListener(ToolListener listener) {
        return listeners.addListener(listener);
    }

    public AutoCloseable addListener(ToolListener listener, boolean weakLink) {
        return listeners.addListener(listener, weakLink);
    }

    public void removeListener(ToolListener listener) {
        listeners.removeListener(listener);
    }

    public void removeAllListeners() {
        listeners.removeAllListeners();
    }

    protected void fireEvent(ToolEvent event) {
        listeners.fireEvent(event);
    }
}
