package xyz.cofe.game.tank.ui;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.swing.*;

public class MenuBuilder {
    public interface JMenuBuilder extends Consumer<JMenu> {
        void build(JMenu menu);

        @Override
        default void accept(JMenu jMenu) {
            build(jMenu);
        }
    }
    public static class ActionItem implements JMenuBuilder {
        //region name : String
        protected String name;
        public String name(){ return name; }
        public ActionItem name(String name){
            if( name==null )throw new IllegalArgumentException( "name==null" );
            this.name = name;
            return this;
        }
        //endregion

        public ActionItem action( ActionListener al ){
            if( al==null )throw new IllegalArgumentException( "al==null" );
            jMenuItems.add( l -> {
                l.addActionListener(al);
            });
            return this;
        }

        public ActionItem item( Consumer<JMenuItem> mi ){
            if( mi==null )throw new IllegalArgumentException( "mi==null" );
            jMenuItems.add(mi);
            return this;
        }

        public ActionItem checked( boolean initial, Consumer<Boolean> checkChanged ){
            if( checkChanged==null )throw new IllegalArgumentException( "checkChanged==null" );
            menuItemBuilder = () -> {
                var m = new JCheckBoxMenuItem();
                m.setSelected(initial);
                m.addActionListener( e -> {
                    checkChanged.accept(m.isSelected());
                });
                return m;
            };
            return this;
        }

        //region build()
        protected final List<Consumer<JMenuItem>> jMenuItems = new ArrayList<>();

        protected Supplier<? extends JMenuItem> menuItemBuilder = JMenuItem::new;
        public void build(JMenu menu){
            if( menu==null )throw new IllegalArgumentException( "menu==null" );
            var mi = menuItemBuilder.get();
            if( name!=null ) {
                mi.setText(name);
            }
            for( var i : jMenuItems ){
                if( i!=null ){
                    i.accept(mi);
                }
            }
            menu.add(mi);
        }
        //endregion
    }

    public interface JMenuBarBuilder {
        void build(JMenuBar bar);
    }
    public static class MenuItem implements JMenuBarBuilder, Consumer<JMenu> {
        //region name : String
        protected String name;
        public String name(){ return name; }
        public MenuItem name(String name){
            if( name==null )throw new IllegalArgumentException( "name==null" );
            this.name = name;
            return this;
        }
        //endregion
        //region buildThisMenu()
        protected void buildThisMenu( JMenu menu ){
            if( menu!=null ) {
                if (name != null) {
                    menu.setText(name);
                }
                for( var j : configure ){
                    if( j!=null ){
                        j.accept(menu);
                    }
                }
            }
        }
        //endregion

        //region action()
        public MenuItem action( String name, Consumer<ActionItem> action ){
            if( name==null )throw new IllegalArgumentException( "name==null" );
            if( action==null )throw new IllegalArgumentException( "action==null" );
            var ai = new ActionItem().name(name);
            action.accept(ai);
            configure.add(ai);
            return this;
        }
        public MenuItem action( String name, Runnable run ){
            if( name==null )throw new IllegalArgumentException( "name==null" );
            if( run==null )throw new IllegalArgumentException( "run==null" );
            return action( name, ai -> {
                ai.action( e -> run.run() );
            });
        }

        private static final AtomicInteger aiSeq = new AtomicInteger(0);
        public MenuItem action( javax.swing.Action action, Consumer<ActionItem> actionConf ){
            if( action==null )throw new IllegalArgumentException( "action==null" );
            ActionItem ai = new ActionItem().name("action#"+aiSeq.incrementAndGet());
            ai.item( i -> i.setAction(action) );
            if( actionConf!=null ){
                actionConf.accept(ai);
            }
            configure.add(ai);
            return this;
        }

        public MenuItem action( javax.swing.Action action ){
            if( action==null )throw new IllegalArgumentException( "action==null" );
            return action( action, null );
        }
        //endregion

        public MenuItem menu( String name, Consumer<MenuItem> menu ){
            if( name==null )throw new IllegalArgumentException( "name==null" );
            if( menu==null )throw new IllegalArgumentException( "menu==null" );
            var mi = new MenuItem().name(name);
            menu.accept(mi);
            configure.add(mi::build);
            return this;
        }

        //region build()
        protected final List<Consumer<JMenu>> configure = new ArrayList<>();

        @Override
        public void accept(JMenu jMenu) {
            build(jMenu);
        }

        public void build(JMenu menu){
            if( menu==null )throw new IllegalArgumentException( "menu==null" );
            var jm = new JMenu();
            buildThisMenu(jm);
            menu.add(jm);
        }

        @Override
        public void build(JMenuBar bar) {
            if( bar==null )throw new IllegalArgumentException( "bar==null" );
            var jm = new JMenu();
            buildThisMenu(jm);
            bar.add(jm);
        }
        //endregion
    }

    //region menu()
    protected final List<JMenuBarBuilder> jMenuBarBuilders = new ArrayList<>();
    public MenuBuilder menu(String name, Consumer<MenuItem> conf){
        if( name==null )throw new IllegalArgumentException( "name==null" );
        if( conf==null )throw new IllegalArgumentException( "conf==null" );
        var mi = new MenuItem().name(name);
        jMenuBarBuilders.add(mi);
        conf.accept(mi);
        return this;
    }
    //endregion

    //region build()
    public void build(JMenuBar menuBar){
        if( menuBar==null )throw new IllegalArgumentException( "menuBar==null" );
        for( var bld : jMenuBarBuilders ){
            if( bld!=null ){
                bld.build(menuBar);
            }
        }
    }
    public void build(JFrame frame){
        if( frame==null )throw new IllegalArgumentException( "frame==null" );
        var jmbar = frame.getJMenuBar();
        if( jmbar==null ){
            jmbar = new JMenuBar();
        }
        build(jmbar);
        if( frame.getJMenuBar()==null ){
            frame.setJMenuBar(jmbar);
        }
    }
    //endregion
}
