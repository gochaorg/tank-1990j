package xyz.cofe.game.tank.ui;

import java.awt.event.KeyEvent;
import java.util.HashMap;

/**
 * События клавиши
 */
public class KeyEv {
    private static final HashMap<Integer,KeyCode> keyCodes;
    static {
        keyCodes = new HashMap<>();
        for( var k : KeyCode.values() ){
            keyCodes.put(k.code, k);
        }
    }

    public KeyEv(){
        keyCode = KeyCode.__Undefined;
    }

    public KeyEv(KeyEvent ev){
        int c = ev.getKeyCode();
        keyCode = keyCodes.getOrDefault(c,KeyCode.__Undefined);

        shift = ev.isShiftDown();
        control = ev.isControlDown();
        alt = ev.isAltDown();
        meta = ev.isMetaDown();
    }

    protected KeyCode keyCode;
    public KeyCode keyCode(){ return keyCode; }

    protected boolean shift;
    public boolean isShift(){ return shift; }

    protected boolean control;
    public boolean isControl(){ return control; }

    protected boolean alt;
    public boolean isAlt(){ return alt; }

    protected boolean meta;
    public boolean isMeta(){ return meta; }
}
