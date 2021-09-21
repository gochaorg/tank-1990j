package xyz.cofe.game.tank.ui;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import xyz.cofe.collection.BasicEventMap;

@JsonIgnoreProperties({
    "readLock","writeLock","collectionListeners","collectionEventQueue"
})
public class ActionsSettings extends BasicEventMap<String,ActionSetting> {
}
