package xyz.cofe.game.tank.ui;

import com.fasterxml.jackson.annotation.JsonIgnore;
import xyz.cofe.ecolls.ListenersHelper;

import javax.swing.KeyStroke;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@SuppressWarnings("UnusedReturnValue")
public class ActionSetting {
    //region listeners
    public interface Listener {
        void actionSetting(Event e);
    }
    public interface Event {
    }

    protected transient final ListenersHelper<Listener,Event> listenersHelper =
        new ListenersHelper<>(Listener::actionSetting);

    public boolean hasListener(Listener listener) {
        return listenersHelper.hasListener(listener);
    }

    @JsonIgnore
    public Set<Listener> getListeners() {
        return listenersHelper.getListeners();
    }

    public AutoCloseable addListener(Listener listener) {
        return listenersHelper.addListener(listener);
    }

    public AutoCloseable addListener(Listener listener, boolean weakLink) {
        return listenersHelper.addListener(listener, weakLink);
    }

    public void removeListener(Listener listener) {
        listenersHelper.removeListener(listener);
    }

    public void removeAllListeners() {
        listenersHelper.removeAllListeners();
    }

    public void fireEvent(Event event) {
        listenersHelper.fireEvent(event);
    }
    //endregion

    //region keyStroke : String
    public static class KeyStrokeChanged implements Event {
        public final ActionSetting actionSetting;
        public final String previous;
        public final String current;
        public KeyStrokeChanged(ActionSetting actionSetting, String previous, String current) {
            this.actionSetting = actionSetting;
            this.previous = previous;
            this.current = current;
        }
    }
    protected String keyStroke;
    public String getKeyStroke() { return keyStroke; }
    public void setKeyStroke(String keyStroke) {
        var old = this.keyStroke;
        this.keyStroke = keyStroke;
        if( !Objects.equals(old,keyStroke) ){
            fireEvent(new KeyStrokeChanged(this,old,keyStroke));
        }
    }
    public Optional<KeyStroke> toKeyStroke(){
        String ks = keyStroke;
        if( ks==null )return Optional.empty();
        return Optional.of(KeyStroke.getKeyStroke(ks));
    }
    //endregion
    //region text : String
    public static class TextChanged implements Event {
        public final ActionSetting actionSetting;
        public final String previous;
        public final String current;
        public TextChanged(ActionSetting actionSetting, String previous, String current) {
            this.actionSetting = actionSetting;
            this.previous = previous;
            this.current = current;
        }
    }
    protected String text;
    public String getText() { return text; }
    public void setText(String text) {
        var old = this.text;
        this.text = text;
        if( !Objects.equals(old,text) ){
            fireEvent(new TextChanged(this,old,text));
        }
    }
    //endregion
}
