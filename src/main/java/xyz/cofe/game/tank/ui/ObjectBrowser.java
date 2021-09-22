package xyz.cofe.game.tank.ui;

import xyz.cofe.ecolls.Closeables;
import xyz.cofe.game.tank.unt.Scene;
import xyz.cofe.gui.swing.tree.*;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;

public class ObjectBrowser extends JPanel {
    public final TreeTableNodeBasic root = new TreeTableNodeBasic("root");
    public final JScrollPane scrollPane;

    public final TreeTable treeTable = new TreeTable();
    {
        treeTable.setRoot(root);
        root.setExpanded(true);
        treeTable.setRootVisible(false);

        setLayout(new BorderLayout());

        scrollPane = new JScrollPane(treeTable);
        add(scrollPane);
    }
}
