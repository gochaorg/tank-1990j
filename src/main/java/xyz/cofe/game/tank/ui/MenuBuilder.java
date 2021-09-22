package xyz.cofe.game.tank.ui;

import xyz.cofe.gui.swing.SwingListener;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.swing.*;
import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;

public class MenuBuilder {
    private static Activator activator;
    private static class Activator extends JWindow {
        public Activator(){
            setBackground(Color.orange);
            setForeground(Color.black);
            getContentPane().setLayout(new BorderLayout());
            JButton activate = new JButton("edit key stroke");
            SwingListener.onActionPerformed(activate,ev -> {
                //System.out.println("action performed");
                editKeyStroke();
            });
            getContentPane().add(activate);
            pack();
        }
    }

    private static JMenuItem selectedMenuItem;
    private static void select(JMenuItem mi){
        selectedMenuItem = mi;
        //System.out.println("select "+mi);
    }
    private static void tooltipShow(){
        //System.out.println("tooltipShow");
        if( selectedMenuItem==null )return;
        if( !selectedMenuItem.isVisible() )return;
        if( activator==null ){
            //System.out.println("create activator");
            activator = new Activator();
        }

        //System.out.println("setLocationRelativeTo");
        var loc = selectedMenuItem.getLocationOnScreen();
        loc.translate(
            selectedMenuItem.getWidth(),
            (int)(selectedMenuItem.getHeight()/2));

        //System.out.println("activator size "+activator.getSize());
        activator.setAlwaysOnTop(true);
        activator.setLocation(loc);
        activator.setFocusable(true);
        activator.setVisible(true);
        activator.toFront();

        SwingListener.onWindowClosing(activator,ev->{
            //System.out.println("activator onWindowClosing");
            if( activator!=null ) {
                activator.dispose();
                activator = null;
            }
        });
        SwingListener.onMouseExited(activator,ev ->{
            //System.out.println("activator onMouseExited");
            if( activator!=null ) {
                activator.setVisible(false);
                activator.dispose();
                activator = null;
            }
        });
        SwingListener.onFocusLost(activator,ev ->{
            //System.out.println("activator onFocusLost");
            if( activator!=null ) {
                activator.setVisible(false);
                activator.dispose();
                activator = null;
            }
        });

        Timer closeByTimeout = new Timer(3000, e -> {
            if( activator!=null ) {
                activator.setVisible(false);
                activator.dispose();
                activator = null;
            }
        });
        closeByTimeout.setRepeats(false);
        closeByTimeout.setInitialDelay(3000);
        closeByTimeout.start();
    }
    private static void tooltipHide(){
        //System.out.println("tooltipHide");
        if( activator!=null ) {
            //System.out.println("activator.setVisible false");
            activator.setVisible(false);
            //System.out.println("activator.dispose");
            activator.dispose();
            activator = null;
        }
    }

    private static Map<ActionItem, JMenuItem> registry = new LinkedHashMap<>();
    private static Map<ActionItem, Map<String,ActionSetting>> registrySettings = new LinkedHashMap<>();
    private static Map<JMenuItem, ActionItem> backRegistry = new LinkedHashMap<>();
    private static void registry(ActionItem actionItem, JMenuItem menuItem, Map<String,ActionSetting> settings){
        registry.put(actionItem,menuItem);
        registrySettings.put(actionItem,settings);
        backRegistry.put(menuItem,actionItem);
    }
    private static void editKeyStroke(){
        var act = backRegistry.get(selectedMenuItem);
        if( act==null || selectedMenuItem==null )return;

        var settings = registrySettings.get(act);

        var editor = new ActionEditor();
        editor.setRegistry(registry);
        editor.setMenuItem(selectedMenuItem);
        editor.setActionSettings(settings);
        editor.setActionItem(act);
        editor.edit();
    }

    public final Map<String,ActionSetting> settings;

    public MenuBuilder(){
        settings = new LinkedHashMap<>();
    }
    public MenuBuilder(Map<String,ActionSetting> settings){
        if( settings==null )throw new IllegalArgumentException( "settings==null" );
        this.settings = settings;
    }

    public interface JMenuBuilder extends Consumer<JMenu> {
        void build(JMenu menu);

        @Override
        default void accept(JMenu jMenu) {
            build(jMenu);
        }
    }
    public static class ActionItem implements JMenuBuilder {
        protected final MenuBuilder menuBuilder;
        public ActionItem(MenuBuilder menuBuilder){
            if( menuBuilder==null )throw new IllegalArgumentException( "menuBuilder==null" );
            this.menuBuilder = menuBuilder;
        }
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
        protected Supplier<? extends JMenuItem> menuItemBuilder = ()->{
            JMenuItem mi = new JMenuItem();
            return mi;
        };
        public void build(JMenu menu){
            if( menu==null )throw new IllegalArgumentException( "menu==null" );
            var mi = menuItemBuilder.get();

            SwingListener.onMouseEntered(mi, e -> {
                select(mi);
            });
            mi.addMenuKeyListener(new MenuKeyListener() {
                @Override
                public void menuKeyTyped(MenuKeyEvent e) {
                }

                @Override
                public void menuKeyPressed(MenuKeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                        tooltipShow();
                    }
                }

                @Override
                public void menuKeyReleased(MenuKeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                        tooltipHide();
                    }
                }
            });

            if( name!=null ) {
                mi.setText(name);
                var aset =
                    !menuBuilder.actionSettingsOnDaemon()
                        ? menuBuilder.settings.get(name)
                        : menuBuilder.settings.computeIfAbsent(name, n -> new ActionSetting());
                if( aset!=null ){
                    aset.toKeyStroke().ifPresent(mi::setAccelerator);
                    if( aset.getText()!=null && aset.getText().length()>0 ){
                        mi.setText(aset.getText());
                    }

                    aset.addListener(ev -> {
                        if( ev instanceof ActionSetting.KeyStrokeChanged ){
                            var e = (ActionSetting.KeyStrokeChanged)ev;
                            e.actionSetting.toKeyStroke().ifPresentOrElse(
                                mi::setAccelerator,
                                ()->{
                                    mi.setAccelerator(null);
                                }
                            );
                        }
                        if( ev instanceof ActionSetting.TextChanged ){
                            var txt = ((ActionSetting.TextChanged) ev).current;
                            if( txt!=null && txt.length()>0 ){
                                mi.setText(txt);
                            }else {
                                mi.setText(name);
                            }
                        }
                    });

                }
            }
            for( var i : jMenuItems ){
                if( i!=null ){
                    i.accept(mi);
                }
            }
            menu.add(mi);
            registry(this,mi,menuBuilder.settings);
        }
        //endregion
    }

    public interface JMenuBarBuilder {
        void build(JMenuBar bar);
    }
    public static class MenuItem implements JMenuBarBuilder, Consumer<JMenu> {
        protected final MenuBuilder menuBuilder;
        public MenuItem(MenuBuilder menuBuilder){
            if( menuBuilder==null )throw new IllegalArgumentException( "menuBuilder==null" );
            this.menuBuilder = menuBuilder;
        }

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
            var ai = new ActionItem(menuBuilder).name(name);
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
            ActionItem ai = new ActionItem(menuBuilder).name("action#"+aiSeq.incrementAndGet());
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
            var mi = new MenuItem(menuBuilder).name(name);
            menu.accept(mi);
            configure.add(mi::build);
            return this;
        }

        public MenuItem separator(){
            configure.add(JMenu::addSeparator);
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

    protected boolean actionSettingsOnDaemon = true;
    public boolean actionSettingsOnDaemon(){ return actionSettingsOnDaemon; }
    public MenuBuilder actionSettingsOnDaemon(boolean v){ actionSettingsOnDaemon=v; return this; }

    //region menu()
    protected final List<JMenuBarBuilder> jMenuBarBuilders = new ArrayList<>();
    public MenuBuilder menu(String name, Consumer<MenuItem> conf){
        if( name==null )throw new IllegalArgumentException( "name==null" );
        if( conf==null )throw new IllegalArgumentException( "conf==null" );
        var mi = new MenuItem(this).name(name);
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
