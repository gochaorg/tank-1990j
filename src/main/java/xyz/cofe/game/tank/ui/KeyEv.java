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

    /**
     * Конструктор
     */
    public KeyEv(){
        keyCode = KeyCode.__Undefined;
    }

    /**
     * Конструктор копирования
     * @param ev образец
     */
    public KeyEv(KeyEvent ev){
        int c = ev.getKeyCode();
        keyCode = keyCodes.getOrDefault(c,KeyCode.__Undefined);

        shift = ev.isShiftDown();
        control = ev.isControlDown();
        alt = ev.isAltDown();
        meta = ev.isMetaDown();
    }

    protected KeyCode keyCode;

    /**
     * Возвращает код клавиши
     * @return код клавиши
     */
    public KeyCode keyCode(){ return keyCode; }

    protected boolean shift;

    /**
     * Возвращает признак нажатия SHIFT
     * @return true - клавиша SHIFT нажата
     */
    public boolean isShift(){ return shift; }

    protected boolean control;

    /**
     * Возвращает признак нажатия CONTROL
     * @return true - клавиша CONTROL нажата
     */
    public boolean isControl(){ return control; }

    protected boolean alt;

    /**
     * Возвращает признак нажатия ALT
     * @return true - клавиша ALT нажата
     */
    public boolean isAlt(){ return alt; }

    protected boolean meta;

    /**
     * Возвращает признак нажатия META (Win)
     * @return true - клавиша META (Win) нажата
     */
    public boolean isMeta(){ return meta; }
}
