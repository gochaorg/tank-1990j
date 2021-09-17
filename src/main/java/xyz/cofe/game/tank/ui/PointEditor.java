package xyz.cofe.game.tank.ui;

import xyz.cofe.game.tank.geom.Point;
import xyz.cofe.gui.swing.properties.PropertyDB;
import xyz.cofe.gui.swing.properties.PropertyDBService;
import xyz.cofe.gui.swing.properties.editor.TextFieldEditor;

public class PointEditor extends TextFieldEditor implements PropertyDBService {
    public PointEditor() {
        super(false);
    }

    @Override
    public void register(PropertyDB pdb) {
        if( pdb!=null ){
            pdb.registerTypeEditor(Point.class, this, 1.0);
        }
    }

    @Override
    protected Object getTextFieldValue() {
        return Point.parse(getTextField().getText());
    }

    @Override
    protected void setTextFieldValue(Object value) {
        if( value instanceof Point ){
            getTextField().setText(Point.toString((Point) value));
        }
    }

    @Override
    public String getAsText() {
        return super.getAsText();
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        super.setAsText(text);
    }
}
