package xyz.cofe.game.tank.ui;

import xyz.cofe.gui.swing.SwingListener;
import xyz.cofe.gui.swing.properties.PropertyDB;
import xyz.cofe.gui.swing.properties.PropertyDBService;
import xyz.cofe.gui.swing.properties.editor.CustomEditor;
import xyz.cofe.gui.swing.properties.editor.TextFieldEditor;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Font;

public class FontEditor extends CustomEditor implements PropertyDBService {
    public FontEditor(){
        initUi();
    }

    public FontEditor(FontEditor sample){
        initUi();
    }

    private JComponent component;
    private JLabel fontLabel;
    private JButton editButton;

    private void initUi(){
        var panel = new JPanel();
        panel.setLayout(new BorderLayout());

        fontLabel = new JLabel("");
        editButton = new JButton("edit");

        panel.add(fontLabel);
        panel.add(editButton,BorderLayout.EAST);

        component = panel;

        var self = this;

        SwingListener.onActionPerformed(editButton, ev -> {
            FontChooser.choose(font).ifPresentOrElse(newFont->{
                font = newFont;
                fontLabel.setText(newFont.toString());
                fireEditingStopped(self);
            },()->{
                fireEditingCanceled(self);
            });
        });
    }

    @Override
    public void register(PropertyDB pdb) {
        if( pdb!=null ){
            pdb.registerTypeEditor(Font.class, this);
        }
    }

    @Override
    public FontEditor clone() {
        return new FontEditor(this);
    }

    @Override
    protected JComponent createComponent() {
        return component;
    }

    private Font font;

    @Override
    public void setValue(Object value) {
        if( value instanceof Font ){
            font = (Font) value;
            fontLabel.setText(font.toString());
        }
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
