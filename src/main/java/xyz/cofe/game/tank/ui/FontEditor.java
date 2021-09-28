package xyz.cofe.game.tank.ui;

import xyz.cofe.gui.swing.properties.editor.CustomEditor;
import xyz.cofe.gui.swing.properties.editor.TextFieldEditor;

import javax.swing.JComponent;
import java.awt.Font;

public class FontEditor extends CustomEditor {
    public FontEditor(){
    }

    public FontEditor(FontEditor sample){
    }

    @Override
    public FontEditor clone() {
        return new FontEditor(this);
    }

    @Override
    protected JComponent createComponent() {
        return null;
    }

    private Font font;

    @Override
    public void setValue(Object value) {

    }

    @Override
    public Object getValue() {
        return font;
    }

    @Override
    public String getJavaInitializationString() {
        return null;
    }

    @Override
    public String getAsText() {
        return null;
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
    }
}
