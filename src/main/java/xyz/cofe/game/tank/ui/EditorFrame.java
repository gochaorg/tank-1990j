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
import xyz.cofe.game.tank.store.MapStore;
import xyz.cofe.game.tank.ui.cmd.*;
import xyz.cofe.game.tank.ui.tool.*;
import xyz.cofe.game.tank.unt.Scene;
import xyz.cofe.game.tank.unt.SceneProperty;
import xyz.cofe.gui.swing.SwingListener;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.*;
import javax.swing.Timer;

/**
 * Редактор
 */
public class EditorFrame extends JFrame {
    //region tools - инструменты
    private final SelectTool selectTool = new SelectTool();

    private final BrickTool brickTool = new BrickTool();
    private final WaterTool waterTool = new WaterTool();
    private final SlideTool slideTool = new SlideTool();
    private final BushTool bushTool = new BushTool();
    private final Tool[] tools = new Tool[]{
        selectTool, brickTool, waterTool, slideTool, bushTool
    };
    {
        for( var t : tools ){
            if( t instanceof SceneProperty){
                ((SceneProperty) t).setScene(getScene());
                onSceneChanged(((SceneProperty) t)::setScene);
            }
        }
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
        alignByGridAction,
        deleteSelectedAction,
    };

    //region scene : Scene
    private final List<Consumer<Scene>> onSceneChanged = new ArrayList<>();
    private void onSceneChanged( Consumer<Scene> changeListener ){
        if( changeListener==null )throw new IllegalArgumentException( "changeListener==null" );
        if( onSceneChanged==null ){
            SwingUtilities.invokeLater(()->{
                onSceneChanged(changeListener);
            });
        }else {
            onSceneChanged.add(changeListener);
        }
    }
    private Scene scene;
    public Scene getScene(){
        if( scene!=null ){
            return scene;
        }
        scene = new Scene();
        return scene;
    }
    public void setScene( Scene scene ){
        if( scene==null )throw new IllegalArgumentException( "scene==null" );
        this.scene = scene;
        onSceneChanged.forEach( l -> l.accept(this.scene));
    }
    //endregion
    //region save/load scene
    protected xyz.cofe.io.fs.File sceneFile;
    protected void saveSceneAs( Scene scene, boolean forceSaveAs ){
        if( scene==null )throw new IllegalArgumentException( "scene==null" );
        var f = sceneFile;
        if( f!=null && !forceSaveAs ){
            saveSceneAs(scene, f);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        fileChooser.setDialogTitle("save scene");
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if( sceneFile!=null ){
            var dir = sceneFile.getParent();
            if( dir.exists() ){
                fileChooser.setCurrentDirectory(dir.toFile());
            }
        }else{
            fileChooser.setCurrentDirectory(new File("."));
        }
        if( fileChooser.showSaveDialog(this)==JFileChooser.APPROVE_OPTION ){
            saveSceneAs(scene, new xyz.cofe.io.fs.File(fileChooser.getSelectedFile().toString()));
        }
    }
    protected void saveSceneAs( Scene scene, xyz.cofe.io.fs.File file ){
        if( scene==null )throw new IllegalArgumentException( "scene==null" );
        if( file==null )throw new IllegalArgumentException( "file==null" );

        var map = new MapStore().store(scene);
        var om = new ObjectMapper();
        om.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            var json = om.writeValueAsString(map);
            file.writeText(json, StandardCharsets.UTF_8);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    protected void loadScene(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        fileChooser.setDialogTitle("save scene");
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if( sceneFile!=null ){
            var dir = sceneFile.getParent();
            if( dir.exists() ){
                fileChooser.setCurrentDirectory(dir.toFile());
            }
        }else{
            fileChooser.setCurrentDirectory(new File("."));
        }
        if( fileChooser.showOpenDialog(this)==JFileChooser.APPROVE_OPTION ){
            loadScene(new xyz.cofe.io.fs.File(fileChooser.getSelectedFile().toString()));
        }
    }
    protected void loadScene( xyz.cofe.io.fs.File file ){
        var om = new ObjectMapper();
        om.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            var jsonData = om.readValue(file.readText(StandardCharsets.UTF_8),Object.class);
            if( jsonData instanceof Map ){
                //noinspection unchecked,rawtypes,rawtypes
                var sceneObj = new MapStore().restore((Map)jsonData);
                if( sceneObj instanceof Scene ){
                    setScene((Scene) sceneObj);
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    //endregion

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

    private final EditorPanel editorPanel;
    {
        editorPanel = new EditorPanel();
        editorPanel.setScene(getScene());
        onSceneChanged(editorPanel::setScene);

        SwingListener.onMouseClicked(editorPanel, ev ->
            activeTool().ifPresent( tool ->
                tool.onMouseClicked(new MouseEv(ev, editorPanel).shift(editorPanel.getOrigin()))
            ));
        SwingListener.onMousePressed(editorPanel, ev ->
            activeTool().ifPresent( tool ->
                tool.onMousePressed(new MouseEv(ev, editorPanel).shift(editorPanel.getOrigin()))
            ));
        SwingListener.onMouseReleased(editorPanel, ev ->
            activeTool().ifPresent( tool ->
                tool.onMouseReleased(new MouseEv(ev, editorPanel).shift(editorPanel.getOrigin()))
            ));
        SwingListener.onMouseDragged(editorPanel, ev ->
            activeTool().ifPresent( tool ->
                tool.onMouseDragged(new MouseEv(ev, editorPanel).shift(editorPanel.getOrigin()))
            ));
        SwingListener.onMouseExited(editorPanel, ev ->
            activeTool().ifPresent( tool ->
                tool.onMouseExited(new MouseEv(ev, editorPanel).shift(editorPanel.getOrigin()))
            ));
        SwingListener.onKeyPressed(editorPanel, ev ->
            activeTool().ifPresent( tool ->
                tool.onKeyPressed(new KeyEv(ev))
            ));
        SwingListener.onKeyReleased(editorPanel, ev ->
            activeTool().ifPresent( tool ->
                tool.onKeyReleased(new KeyEv(ev))
            ));
        editorPanel.onPaintComponent( gs -> {
            var tool = getActiveTool();
            if( tool!=null ){
                tool.onPaint(gs);
            }
        });
        selectTool.origin(editorPanel::getOrigin);
        selectTool.setGrid(()->editorPanel.grid);
        alignByGridAction.setGrid(editorPanel.grid);
    }

    private final ObjectBrowser objectBrowser;
    {
        objectBrowser = new ObjectBrowser();
        objectBrowser.setScene(getScene());
        onSceneChanged( objectBrowser::setScene );

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

            var editorDock = new DefaultSingleCDockable(DockId.editor.name(),"Editor");
            editorDock.getContentPane().add(ef.editorPanel);
            SwingUtilities.invokeLater(()->{
                cc.addDockable(editorDock);
                editorDock.setTitleText("Editor");
                editorDock.setMinimizable(false);
                editorDock.setExternalizable(false);
                editorDock.setCloseable(true);
                editorDock.setVisible(true);
            });

            SwingListener.onWindowOpened(ef, ev -> {
                var t = new Timer(1000, x -> {
                    ef.editorPanel.requestFocus();
                });
                t.setInitialDelay(1000);
                t.setRepeats(false);
                t.start();
            });

            editorDock.addFocusListener(new CFocusListener() {
                @Override
                public void focusGained(CDockable dockable){
                    //System.out.println("focus");
                }

                @Override
                public void focusLost(CDockable dockable){
                    //System.out.println("focus lost");
                }
            });

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
                    if( DockId.editor.name().equals(id) )return editorDock;
                    if( DockId.properties.name().equals(id) )return propDock;
                    if( DockId.objectBrowser.name().equals(id) )return obDock;
                    return null;
                }
            };

            cc.addSingleDockableFactory(DockId.tool.name(), f);
            cc.addSingleDockableFactory(DockId.editor.name(), f);
            cc.addSingleDockableFactory(DockId.properties.name(), f);
            cc.addSingleDockableFactory(DockId.objectBrowser.name(), f);

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

            new MenuBuilder(asettings)
                .menu( "File", fileMenu -> {
                    fileMenu.action("Load scene", ()->{
                        ef.loadScene();
                    });
                    fileMenu.action("Save scene", ()->{
                        ef.saveSceneAs(ef.getScene(),false);
                    });
                    fileMenu.action("Save scene as", ()->{
                        ef.saveSceneAs(ef.getScene(),true);
                    });
                    fileMenu.action("Exit", ()->{
                        ef.setVisible(false);
                        ef.dispose();
                    });
                })
                .menu("Mode", modeMenu -> {
                    modeMenu.action("Snap to grid", act -> {
                        act.checked(false, snap -> {
                            System.out.println("snap "+snap);
                            ef.selectTool.setSnapToGrid(snap);
                        });
                    });
                })
                .menu("Tools", toolsMenu -> {
                    for( var tool : ef.tools ){
                        toolsMenu.action(tool.name(), ()->{
                            ef.setActiveTool(tool);
                        });
                    }
                })
                .menu("Command", commandMenu -> {
                    for( var cmd : ef.selectActions ){
                        commandMenu.action(cmd.name(), ()->{
                            cmd.execute(ef.getScene(), ef.selectTool.getSelection());
                        });
                    }
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
                });
            }
        });
    }
}
