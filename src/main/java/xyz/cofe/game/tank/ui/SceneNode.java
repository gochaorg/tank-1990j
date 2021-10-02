package xyz.cofe.game.tank.ui;

import xyz.cofe.collection.EventList;
import xyz.cofe.ecolls.Closeables;
import xyz.cofe.game.tank.unt.Figure;
import xyz.cofe.game.tank.unt.Note;
import xyz.cofe.game.tank.unt.Scene;
import xyz.cofe.gui.swing.tree.TreeTableNodeBasic;

public class SceneNode extends NamedNode implements AutoCloseable {
    public SceneNode(Scene scene) {
        super(scene);
        if( scene==null )throw new IllegalArgumentException( "data==null" );
        setName("scene");
        append(figuresNode(scene));

        setDataTextReader( node -> {
            if( node == null )return "null";
            if( node instanceof Note ){
                var title = ((Note) node).getTitle();
                if( title!=null )return "note "+title;
            }
            return node.toString();
        });
    }

    protected Closeables sceneCloseables = new Closeables();

    protected TreeTableNodeBasic figuresNode(Scene scene ){
        var figs = scene.getFigures();

        NamedNode node = new NamedNode(figs);
        node.setName("figures");

        for( var f : figs ){
            node.append(new TreeTableNodeBasic(f));
        }

        if( figs instanceof EventList ){
            var elist = (EventList<Figure<?>>)figs;
            var cl = elist.onChanged(false,(idx,old,cur)->{
                if( old!=null ){
                    node.remove(idx);
                }
                if( cur!=null ){
                    node.insert(idx,new TreeTableNodeBasic(cur));
                }
            });
            sceneCloseables.add(cl);
        }
        return node;
    }

    @Override
    public void close() {
        sceneCloseables.closeAll(true);
        setData(null);
    }
}
