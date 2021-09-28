package xyz.cofe.game.tank.ui;

import javax.swing.SwingUtilities;
import java.awt.Font;

public class FontTest {
    public static void main(String[] args){
        SwingUtilities.invokeLater(()->{
            FontChooser.choose(new Font(Font.SANS_SERIF,Font.PLAIN,24), null);
        });
    }
}
