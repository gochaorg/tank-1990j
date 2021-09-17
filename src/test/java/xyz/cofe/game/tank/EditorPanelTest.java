package xyz.cofe.game.tank;

import xyz.cofe.game.tank.ui.EditorPanel;

import javax.swing.*;

public class EditorPanelTest {
    public static void main(String[] args){
        SwingUtilities.invokeLater(()->{
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setTitle("EditorPanelTest");
            frame.getContentPane().add(new EditorPanel());
            frame.setSize(600,600);
            frame.setVisible(true);
            frame.setLocationRelativeTo(null);
        });
    }
}
