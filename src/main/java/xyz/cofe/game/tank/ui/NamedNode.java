package xyz.cofe.game.tank.ui;

import xyz.cofe.gui.swing.tree.TreeTableNodeBasic;
import xyz.cofe.gui.swing.tree.TreeTableNodeGetText;

import java.util.function.Supplier;

class NamedNode extends TreeTableNodeBasic implements TreeTableNodeGetText {
    public NamedNode() {
        super();
    }
    public NamedNode(Object data) {
        super(data);
    }

    protected Supplier<String> name;
    public String getName() {
        return name!=null ? name.get() : null;
    }
    public void setName(String name) {
        this.name = ()->name;
    }
    public void setName(Supplier<String> name) {
        this.name = name;
    }

    public NamedNode name(String name) {
        setName(name);
        return this;
    }

    @Override
    public String treeTableNodeGetText() {
        var name = getName();
        return name != null ? name : super.treeTableNodeGetText();
    }
}
