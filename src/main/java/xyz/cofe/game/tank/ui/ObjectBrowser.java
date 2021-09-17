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

    protected Closeables sceneCloseables = new Closeables();
    protected TreeTableNodeBasic sceneNode;
    public void setScene(Scene scene){
        if( scene==null )throw new IllegalArgumentException( "scene==null" );

        var oldNode = sceneNode;
        if( oldNode!=null )root.delete(oldNode);
        sceneCloseables.close();

        sceneNode = new NamedNode(scene).name("scene");
        sceneNode.append(figuresNode(scene));
        root.insert(0,sceneNode);
    }

    public Scene getScene(){
        if( sceneNode!=null )return (Scene) sceneNode.getData();
        return null;
    }
    protected static class NamedNode extends TreeTableNodeBasic implements TreeTableNodeGetText {
        public NamedNode() {
            super();
        }

        public NamedNode(Object data) {
            super(data);
        }

        protected String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public NamedNode name(String name){
            setName(name);
            return this;
        }

        @Override
        public String treeTableNodeGetText() {
            return
                name!=null ? name :
                super.treeTableNodeGetText();
        }
    }
    protected TreeTableNodeBasic figuresNode( Scene scene ){
        var figs = scene.getFigures();

        NamedNode node = new NamedNode(figs);
        node.setName("figures");

        for( var f : figs ){
            node.append(new TreeTableNodeBasic(f));
        }

        var cl = figs.onChanged(false,(idx,old,cur)->{
            if( old!=null ){
                node.remove(idx);
            }
            if( cur!=null ){
                node.insert(idx,new TreeTableNodeBasic(cur));
            }
        });
        sceneCloseables.add(cl);

        return node;
    }
}
