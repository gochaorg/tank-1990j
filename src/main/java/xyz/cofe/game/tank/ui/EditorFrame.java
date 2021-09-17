package xyz.cofe.game.tank.ui;

//import com.github.weisj.darklaf.LafManager;
//import com.github.weisj.darklaf.theme.DarculaTheme;
import bibliothek.gui.dock.common.*;
import bibliothek.gui.dock.common.event.CFocusListener;
import bibliothek.gui.dock.common.intern.CDockable;
import bibliothek.gui.dock.common.location.CBaseLocation;
import bibliothek.gui.dock.common.theme.ThemeMap;
import com.formdev.flatlaf.FlatLightLaf;
import xyz.cofe.game.tank.ui.cmd.AlignByGridAction;
import xyz.cofe.game.tank.ui.cmd.DeleteSelectedAction;
import xyz.cofe.game.tank.ui.tool.BrickTool;
import xyz.cofe.game.tank.ui.tool.SelectTool;
import xyz.cofe.game.tank.ui.tool.WaterTool;
import xyz.cofe.game.tank.unt.Figura;
import xyz.cofe.game.tank.unt.Scene;
import xyz.cofe.game.tank.unt.SceneProperty;
import xyz.cofe.gui.swing.SwingListener;
import xyz.cofe.gui.swing.bean.UiBean;
import xyz.cofe.gui.swing.properties.Property;
import xyz.cofe.iter.Eterable;
import xyz.cofe.text.Text;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.function.Predicate;

import javax.swing.*;
import javax.swing.Timer;

/**
 * Редактор
 */
public class EditorFrame extends JFrame {
    //region tools - инструменты
    private final BrickTool brickTool = new BrickTool();
    private final WaterTool waterTool = new WaterTool();
    private final SelectTool selectTool = new SelectTool();
    private final Tool[] tools = new Tool[]{
        selectTool, brickTool, waterTool
    };
    {
        for( var t : tools ){
            if( t instanceof SceneProperty){
                ((SceneProperty) t).setScene(getScene());
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
        this.activeTool = tool;
        tool2toolbarButton.forEach( (t,b)->{
            b.setBorderPainted(t!=tool);
        });
    }
    //endregion

    private final SelectAction deleteSelectedAction = new DeleteSelectedAction();
    private final AlignByGridAction alignByGridAction = new AlignByGridAction();
    private final SelectAction[] selectActions = new SelectAction[]{ deleteSelectedAction, alignByGridAction };

    //region scene : Scene
    private Scene scene;
    public Scene getScene(){
        if( scene!=null ){
            return scene;
        }
        scene = new Scene();
        return scene;
    }
    public void setScene( Scene scene ){
        this.scene = scene;
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

            new MenuBuilder()
                .menu( "File", fileMenu -> {
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
