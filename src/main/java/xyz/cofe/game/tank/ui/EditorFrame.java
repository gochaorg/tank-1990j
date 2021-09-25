package xyz.cofe.game.tank.ui;

//import com.github.weisj.darklaf.LafManager;
//import com.github.weisj.darklaf.theme.DarculaTheme;
import bibliothek.gui.dock.common.*;
import bibliothek.gui.dock.common.event.CFocusListener;
import bibliothek.gui.dock.common.intern.CDockable;
import bibliothek.gui.dock.common.theme.ThemeMap;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.formdev.flatlaf.FlatLightLaf;
import xyz.cofe.ecolls.Closeables;
import xyz.cofe.game.tank.Observers;
import xyz.cofe.game.tank.store.MapStore;
import xyz.cofe.game.tank.ui.cmd.*;
import xyz.cofe.game.tank.ui.tool.*;
import xyz.cofe.game.tank.unt.Scene;
import xyz.cofe.game.tank.unt.SceneProperty;
import xyz.cofe.gui.swing.SwingListener;
import xyz.cofe.iter.Eterable;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.swing.*;
import javax.swing.Timer;

/**
 * Редактор
 */
public class EditorFrame extends JFrame {
    private final Observers<EditorPanel> onEditorPanelChanged = new Observers<>();

    //region tools - инструменты
    private final SelectTool selectTool = new SelectTool();

    private final BrickTool brickTool = new BrickTool();
    private final WaterTool waterTool = new WaterTool();
    private final SlideTool slideTool = new SlideTool();
    private final BushTool bushTool = new BushTool();
    private final PlayerOneTool playerOneTool = new PlayerOneTool();

    private final Tool[] tools = new Tool[]{
        selectTool, brickTool, waterTool, slideTool, bushTool, playerOneTool
    };

    private final Closeables toolEdSceneLs = new Closeables();
    {
        onEditorPanelChanged.listen( ev -> {
            toolEdSceneLs.close();

            for( var t : tools ){
                if( t instanceof SceneProperty){
                    ((SceneProperty) t).setScene(ev.event.getScene());
                }
                if( t instanceof GridBinding ){
                    ((GridBinding) t).setGrid(()->ev.event.grid);
                }
            }

            selectTool.origin(ev.event::getOrigin);

            toolEdSceneLs.add(
            ev.event.onSceneChanged.listen( eev -> {
                for( var t : tools ){
                    if( t instanceof SceneProperty){
                        ((SceneProperty) t).setScene(eev.event);
                    }
                }
            }));
        });
    }

    private final Map<Tool,JButton> tool2toolbarButton = new LinkedHashMap<>();
    //endregion
    //region activeTool : Tool - активный инструмент
    private Tool activeTool;
    public Tool getActiveTool(){ return activeTool; }
    public Optional<Tool> activeTool(){
        return activeTool!=null
            ? Optional.of(activeTool)
            : Optional.empty();
    }
    public void setActiveTool( Tool tool ){
        //System.out.println("setActiveTool "+tool);
        this.activeTool = tool;
        tool2toolbarButton.forEach( (t,b)->{
            b.setBorderPainted(t!=tool);
        });
    }
    //endregion

    private final SelectAction deleteSelectedAction = new DeleteAction();
    private final AlignByGridAction alignByGridAction = new AlignByGridAction();
    private final DuplicateAction duplicateAction = new DuplicateAction();
    private final ConvertToBushAction convertToBushAction = new ConvertToBushAction();
    private final ConvertToWaterAction convertToWaterAction = new ConvertToWaterAction();
    private final ConvertToBrickAction convertToBrickAction = new ConvertToBrickAction();
    private final ConvertToSlideAction convertToSlideAction = new ConvertToSlideAction();
    private final ConvertToBulletAction convertToBulletAction = new ConvertToBulletAction();
    private final ConvertToPlayerOneAction convertToPlayerOneAction = new ConvertToPlayerOneAction();
    private final ConvertToPlayerTwoAction convertToPlayerTwoAction = new ConvertToPlayerTwoAction();
    {
        duplicateAction.setSelectTool(selectTool);
        convertToBushAction.setSelectTool(selectTool);
        convertToWaterAction.setSelectTool(selectTool);
        convertToBrickAction.setSelectTool(selectTool);
        convertToSlideAction.setSelectTool(selectTool);
    }
    private final SelectAction[] selectActions = new SelectAction[]{
        duplicateAction,

        convertToBushAction,convertToWaterAction,convertToBrickAction,convertToSlideAction,
        convertToBulletAction,convertToPlayerOneAction,convertToPlayerTwoAction,

        alignByGridAction,
        deleteSelectedAction,
    };

    private final Closeables selectActionsSceneLs = new Closeables();
    {
        onEditorPanelChanged.listen( ev -> {
            selectActionsSceneLs.close();
            for( var t : selectActions ){
                if( t instanceof SceneProperty){
                    ((SceneProperty) t).setScene(ev.event.getScene());
                }
                if( t instanceof GridBinding ){
                    ((GridBinding) t).setGrid(()->ev.event.grid);
                }
                if( t instanceof OriginProperty ){
                    ((OriginProperty) t).setOrigin(ev.event.getOrigin());
                }
            }
            selectActionsSceneLs.add(
                ev.event.onSceneChanged.listen(eev -> {
                    for( var t : selectActions ){
                        if( t instanceof SceneProperty){
                            ((SceneProperty) t).setScene(eev.event);
                        }
                    }
                })
            );
        });
    }

    private final JPanel toolPanel;
    {
        toolPanel = new JPanel();
        SwingUtilities.invokeLater(()->{
            //toolPanel.setLayout(new FlowLayout());
            toolPanel.setLayout(new GridBagLayout());
            var tIdx = -1;
            var lastGridY = -1;
            for( var tool : tools ){
                var but = tool.toToolbarButton();
                if( but!=null ){
                    tIdx++;
                    GridBagConstraints gbc = new GridBagConstraints();
                    gbc.fill = GridBagConstraints.HORIZONTAL;
                    gbc.gridx = 0;
                    gbc.gridy = tIdx*2;
                    gbc.weightx = 1;
                    gbc.weighty = 0;
                    gbc.insets = new Insets(2,3,2, 2);
                    lastGridY = Math.max(gbc.gridy, lastGridY);

                    toolPanel.add(but, gbc);
                    tool2toolbarButton.put(tool,but);
                    SwingListener.onActionPerformed(but, ev -> {
                        setActiveTool(tool);
                    });
                }
            }
            if( tIdx>=0 ){
                JPanel pad = new JPanel();

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.fill = GridBagConstraints.BOTH;
                gbc.gridx = 0;
                gbc.gridy = lastGridY+1;
                gbc.weightx = 1;
                gbc.weighty = 1;

                toolPanel.add(pad, gbc);
            }
        });
    }

    private final PropertySheet propsPanel;
    {
        propsPanel = new PropertySheet();
        selectTool.addListener( ev -> {
            if( ev instanceof SelectTool.LastSelectChanged ){
                var e = ((SelectTool.LastSelectChanged)ev);
                if( e.figura!=null )propsPanel.edit(e.figura);
            }
        });
//        PropertySheet.extend(Figura.class, ext -> {
//            ext.property("x", double.class, f -> f.left(), (f,v)->{
//                f.location(v,f.top());
//                return f.left();
//            });
//            ext.property("y", double.class, f -> f.top(), (f,v)->{
//                f.location(f.left(),v);
//                return f.top();
//            });
//        });
    }

    protected Closeables activeSceneDockListeners = new Closeables();
    {
        onEditorPanelChanged.listen( ev1 -> {
            activeSceneDockListeners.closeAll(true);

            activeSceneDockListeners.add(
            SwingListener.onMouseClicked(ev1.event, ev ->
                activeTool().ifPresent( tool ->
                    tool.onMouseClicked(new MouseEv(ev, ev1.event).shift(ev1.event.getOrigin()))
                )));

            activeSceneDockListeners.add(
            SwingListener.onMousePressed(ev1.event, ev ->
                activeTool().ifPresent( tool ->
                    tool.onMousePressed(new MouseEv(ev, ev1.event).shift(ev1.event.getOrigin()))
                )));

            activeSceneDockListeners.add(
            SwingListener.onMouseReleased(ev1.event, ev ->
                activeTool().ifPresent( tool ->
                    tool.onMouseReleased(new MouseEv(ev, ev1.event).shift(ev1.event.getOrigin()))
                )));

            activeSceneDockListeners.add(
            SwingListener.onMouseDragged(ev1.event, ev ->
                activeTool().ifPresent( tool ->
                    tool.onMouseDragged(new MouseEv(ev, ev1.event).shift(ev1.event.getOrigin()))
                )));

            activeSceneDockListeners.add(
            SwingListener.onMouseExited(ev1.event, ev ->
                activeTool().ifPresent( tool ->
                    tool.onMouseExited(new MouseEv(ev, ev1.event).shift(ev1.event.getOrigin()))
                )));

            activeSceneDockListeners.add(
            SwingListener.onKeyPressed(ev1.event, ev ->
                activeTool().ifPresent( tool ->
                    tool.onKeyPressed(new KeyEv(ev))
                )));

            activeSceneDockListeners.add(
            SwingListener.onKeyReleased(ev1.event, ev ->
                activeTool().ifPresent( tool ->
                    tool.onKeyReleased(new KeyEv(ev))
                )));

            activeSceneDockListeners.add(
            ev1.event.onPaintComponent( gs -> {
                var tool = getActiveTool();
                if( tool!=null ){
                    tool.onPaint(gs);
                }
            }));
        });
    }

    private final ObjectBrowser objectBrowser;
    {
        objectBrowser = new ObjectBrowser();
        objectBrowser.treeTable.onFocusedNodeChanged(node -> {
            propsPanel.edit(node.getData());
        });
    }

    public static enum DockId {
        tool,
        properties,
        editor,
        objectBrowser
    }

    protected Optional<SceneDock> getFocusedSceneDock(){
        var s = lastFocusedSceneDock!=null ? lastFocusedSceneDock.get() : null;
        return s!=null ? Optional.of(s) : Optional.empty();
    };

    protected WeakReference<SceneDock> lastFocusedSceneDock;
    protected void onSceneDockFocus( SceneDock sceneDock ){
        lastFocusedSceneDock = new WeakReference<>(sceneDock);
        onEditorPanelChanged.fire(sceneDock.editorPanel);
    }

    public static void main(String[] args){
        Map<String,String> opts = new LinkedHashMap<>();
        String argName = null;
        for( var arg : args ){
            if( argName==null && arg.startsWith("-") && arg.length()>1 ){
                argName = arg.substring(1);
            }else {
                if( argName!=null ) {
                    opts.put(argName,arg);
                    argName = null;
                }
            }
        }

        SwingUtilities.invokeLater(()->{
            FlatLightLaf.install();

            EditorFrame ef = new EditorFrame();
            ef.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            ef.setTitle("Editor");
            ef.setExtendedState(JFrame.MAXIMIZED_BOTH);
            ef.setSize(new Dimension(500,400));

            CControl cc = new CControl(ef);

            //...........
            ef.getContentPane().setLayout(new BorderLayout());
            ef.getContentPane().add(cc.getContentArea());
            cc.getThemes().select(ThemeMap.KEY_ECLIPSE_THEME);

            var toolDock = new DefaultSingleCDockable(DockId.tool.name());
            toolDock.getContentPane().add(ef.toolPanel);
            SwingUtilities.invokeLater(()->{
                cc.addDockable(toolDock);
                CLocation loc = CLocation.base(cc.getContentArea()).normalWest(0.2);
                toolDock.setLocation(loc);
                toolDock.setTitleText("Tool");
                toolDock.setVisible(true);
                toolDock.setStickySwitchable(true);
            });

            var propDock = new DefaultSingleCDockable(DockId.properties.name(),"Properties");
            propDock.getContentPane().add(ef.propsPanel);
            SwingUtilities.invokeLater(()->{
                cc.addDockable(propDock);
                CLocation loc = CLocation.base(cc.getContentArea()).normalEast(0.2);
                propDock.setLocation(loc);
                propDock.setVisible(true);
            });

            var obDock = new DefaultSingleCDockable(DockId.objectBrowser.name(),"Objects");
            obDock.getContentPane().add(ef.objectBrowser);
            SwingUtilities.invokeLater(()->{
                cc.addDockable(obDock);

                var base = CLocation.base(cc.getContentArea());
                CLocation loc = base.normalWest(0.2);
                obDock.setLocation(loc);
                obDock.setVisible(true);
            });

            SingleCDockableFactory f = new SingleCDockableFactory() {
                @Override
                public SingleCDockable createBackup(String id){
                    System.out.println("id="+id);
                    if( DockId.tool.name().equals(id) )return toolDock;
                    //if( DockId.editor.name().equals(id) )return editorDock;
                    if( DockId.properties.name().equals(id) )return propDock;
                    if( DockId.objectBrowser.name().equals(id) )return obDock;
                    return null;
                }
            };

            cc.addSingleDockableFactory(DockId.tool.name(), f);
            //cc.addSingleDockableFactory(DockId.editor.name(), f);
            cc.addSingleDockableFactory(DockId.properties.name(), f);
            cc.addSingleDockableFactory(DockId.objectBrowser.name(), f);

            //MultipleCDockableFactory
            SceneDockFactory sceneDockFactory = new SceneDockFactory();

            var sceneDockListeners = new WeakHashMap<SceneDock, Closeables>();
            var sceneDockNode = new WeakHashMap<SceneDock,SceneNode>();
            var sceneDockSceneChangeListener = new WeakHashMap<SceneDock,Closeables>();

            Consumer<SceneDock> listenSceneDock = sceneDock -> {
                var cl = sceneDockListeners.computeIfAbsent(sceneDock, x -> new Closeables());

                var ref = new WeakReference<>(sceneDock);

                SceneNode snode = new SceneNode(sceneDock.editorPanel.getScene());
                snode.setName(()->{
                    var r = ref.get();
                    return r!=null ? "scene "+r.getTitleText() : "scene";
                });

                ef.objectBrowser.treeTable.getRoot().append(snode);
                sceneDockNode.put(sceneDock, snode);

                Runnable clScene = ()->{
                    var r = ref.get();
                    var sn = sceneDockNode.get(r);
                    if( sn!=null ){
                        ef.objectBrowser.treeTable.getRoot().delete(sn);
                        sn.close();
                    }
                };

                Closeables cS = new Closeables();
                cS.add(clScene);
                sceneDockSceneChangeListener.put(sceneDock,cS);

                var cl2 = sceneDock.editorPanel.onSceneChanged.listen( newScene -> {
                    var sceneDock1 = ref.get();
                    var cl1 = sceneDockSceneChangeListener.get(sceneDock1);
                    if( cl1!=null ){
                        cl1.close();
                    }

                    if( sceneDock1!=null ) {
                        SceneNode snode1 = new SceneNode(newScene.event);
                        snode1.setName(() -> {
                            var r = ref.get();
                            return r != null ? "scene " + r.getTitleText() : "scene";
                        });

                        ef.objectBrowser.treeTable.getRoot().append(snode1);
                        sceneDockNode.put(sceneDock1, snode1);

                        Runnable clScene1 = ()->{
                            var r = ref.get();
                            var sn = sceneDockNode.get(r);
                            if( sn!=null ){
                                ef.objectBrowser.treeTable.getRoot().delete(sn);
                                sn.close();
                            }
                        };

                        if( cl1!=null ){
                            cl1.add(clScene1);
                        }
                    }
                });
                cl.add(cS);
                cl.add(cl2);

                cl.add( sceneDock.onClosed.listen( cev -> {
                    var r = ref.get();
                    if( r!=null ) {
                        var clSet = sceneDockListeners.get(r);
                        if (clSet != null) {
                            System.out.println("close all");
                            clSet.closeAll(true);
                        }
                    }
                    System.out.println("closed "+cev.event.getTitleText());
                }) );
                cl.add( sceneDock.onFocusGained.listen(cev -> {
                    ef.onSceneDockFocus(cev.event);
                }) );
            };
            sceneDockFactory.onSceneDockCreated.add(ev -> {
                listenSceneDock.accept(ev.event);
            });
            cc.addMultipleDockableFactory("scene", sceneDockFactory);

            ActionsSettings asettings = new ActionsSettings();
            if( opts.containsKey("ui.actions.file") ){
                var file = new xyz.cofe.io.fs.File(opts.get("ui.actions.file"));
                var dir = file.getParent();
                if( dir!=null && !dir.exists() )dir.createDirectories();
                if( file.isFile() ){
                    ObjectMapper om = new ObjectMapper();
                    try {
                        asettings = om.readValue(file.readText(StandardCharsets.UTF_8),ActionsSettings.class);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }

                var f_settings = asettings;
                SwingListener.onWindowClosing(ef, e->{
                    ObjectMapper om = new ObjectMapper();
                    om.enable(SerializationFeature.INDENT_OUTPUT);
                    try {
                        file.writeText(
                            om.writeValueAsString(f_settings),
                            StandardCharsets.UTF_8
                            );
                    } catch (JsonProcessingException err) {
                        err.printStackTrace();
                    }
                });
            }

            var sceneEditorCounter = new AtomicInteger(0);

            new MenuBuilder(asettings)
                .menu( "File", fileMenu -> {
                    fileMenu.action("New scene", ()->{
                        SceneDock sceneDock = new SceneDock(sceneDockFactory);
                        listenSceneDock.accept(sceneDock);

                        cc.addDockable(sceneDock);

                        sceneDock.setLocation(cc.getDefaultLocation());
                        sceneDock.setTitleText("Editor#"+sceneEditorCounter.incrementAndGet());

                        ef.getFocusedSceneDock().ifPresent(sceneDock::setLocationsAside);
                        sceneDock.setVisible(true);
                    });
                    fileMenu.action("Load scene", ()->{
                        ef.getFocusedSceneDock().ifPresent(SceneDock::loadScene);
                    });
                    fileMenu.action("Save scene", ()->{
                        ef.getFocusedSceneDock().ifPresent(sceneDock -> {
                            sceneDock.saveSceneAs(false);
                        });
                    });
                    fileMenu.action("Save scene as", ()->{
                        ef.getFocusedSceneDock().ifPresent(sceneDock -> {
                            sceneDock.saveSceneAs(true);
                        });
                    });
                    fileMenu.action("Exit", ()->{
                        ef.setVisible(false);
                        ef.dispose();
                    });
                })
                .menu("Mode", modeMenu -> {
                    modeMenu.action("Snap to grid", act -> {
                        act.checked(false, snap -> {
                            for( var t : ef.tools ){
                                if( t instanceof SnapToGridProperty ){
                                    ((SnapToGridProperty) t).setSnapToGrid(snap);
                                }
                            }
                            for( var t : ef.selectActions ){
                                if( t instanceof SnapToGridProperty ){
                                    ((SnapToGridProperty) t).setSnapToGrid(snap);
                                }
                            }
                        });
                    });
                })
                .menu("Tools", toolsMenu -> {
                    for( var tool : ef.tools ){
                        toolsMenu.action(tool.name(), act->{
                            act.checked(()->{
                                var at = ef.getActiveTool();
                                return at==tool;
                            },(s)->{});
                            act.action(ev->{
                                ef.setActiveTool(tool);
                            });
                        });
                    }
                })
                .menu("Command", commandMenu -> {
                    for( var cmd : ef.selectActions ){
                        commandMenu.action(cmd.name(), ()->{
                            var sc = ef.selectTool.getScene();
                            var sel = ef.selectTool.getSelection();
                            if( sc!=null && sel!=null ) {
                                cmd.execute(sc, sel);
                            }
                        });
                    }
                    commandMenu.separator();
                    commandMenu.action("Reset view",
                        new ResetViewCommand().origin(()->ef.getFocusedSceneDock().map(s->s.editorPanel).orElse(null))
                    );
                    commandMenu.action("Run GC", new RunGCCommand());
                })
                .menu( "About", aboutMenu -> {
                    aboutMenu.action("about", ()->{});
                })
                .build(ef);

            ef.setVisible(true);

            if( opts.containsKey("ui.docks.file") ) {
                java.io.File docsFile = new File(opts.get("ui.docks.file"));
                var prnt = docsFile.getParentFile();
                if( prnt!=null && !prnt.exists() ){
                    prnt.mkdirs();
                }
                SwingListener.onWindowClosing(ef, e -> {
                    try {
                        cc.writeXML(docsFile);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });
                SwingListener.onWindowOpened(ef, e -> {
                    try {
                        cc.readXML(docsFile);
                    } catch (FileNotFoundException ex){
                        System.out.println("not found "+docsFile);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                    sceneDockListeners.keySet().stream().max(Comparator.comparingLong(a -> a.lastFocusGained)).ifPresent(s -> {
                        s.toFront(s.editorPanel);
                    });
                });
            }
        });
    }
}
