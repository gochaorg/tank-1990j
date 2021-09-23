package xyz.cofe.game.tank.ui;

import bibliothek.gui.dock.common.DefaultMultipleCDockable;
import bibliothek.gui.dock.common.MultipleCDockableFactory;
import bibliothek.gui.dock.common.action.CAction;
import bibliothek.gui.dock.common.event.CFocusListener;
import bibliothek.gui.dock.common.event.CVetoClosingEvent;
import bibliothek.gui.dock.common.event.CVetoClosingListener;
import bibliothek.gui.dock.common.intern.CDockable;
import bibliothek.gui.dock.dockable.IconHandling;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import xyz.cofe.game.tank.Observers;
import xyz.cofe.game.tank.store.MapStore;
import xyz.cofe.game.tank.unt.Scene;
import xyz.cofe.gui.swing.SwingListener;
import xyz.cofe.io.fs.File;

import javax.swing.Icon;
import javax.swing.JFileChooser;
import java.awt.BorderLayout;
import java.awt.Component;
import java.io.IOError;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class SceneDock extends DefaultMultipleCDockable {
    @Override
    protected void finalize() throws Throwable {
        System.out.println("finalize");
        super.finalize();
    }

    //region constructor
    public SceneDock(MultipleCDockableFactory<?, ?> factory) {
        super(factory);
        editorPanel = new EditorPanel();
        initUI();
    }
    public SceneDock(MultipleCDockableFactory<?, ?> factory, SceneDockState state){
        super(factory);
        if( state !=null ){
            var sf = state.sceneFileName;
            if( sf!=null ){
                setSceneFile(new File(sf));
            }

            if( state.title!=null && state.title.length()>0 ){
                setTitleText(state.title);
            }

            lastFocusGained = state.lastFocusGained;
        }

        editorPanel = new EditorPanel();
        initUI();

        if( sceneFile!=null ){
            reloadScene();
        }

        if( state!=null ){
            if( state.origin!=null ){
                editorPanel.setOrigin(state.origin);
            }
        }
    }

    public SceneDockState createStoreLayout(){
        return new SceneDockState(this);
    }

    public final Observers<SceneDock> onFocusGained = new Observers<>();
    public final Observers<SceneDock> onFocusLost = new Observers<>();
    public final Observers<SceneDock> onClosing = new Observers<>();
    public final Observers<SceneDock> onClosed = new Observers<>();

    public long lastFocusGained;

    private void initUI(){
        editorPanel.setScene(new Scene());

        setCloseable(true);
        setMaximizable(true);
        setMinimizable(true);
        setExternalizable(true);
        setRemoveOnClose(true);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(editorPanel);
        setFocusComponent(editorPanel);
        editorPanel.setFocusable(true);

//        SwingListener.onMousePressed(editorPanel, e -> {
//            System.out.println("onMousePressed "+e);
//        });

        addVetoClosingListener(new CVetoClosingListener() {
            @Override
            public void closing(CVetoClosingEvent event) {
                onClosing.fire(SceneDock.this);
            }

            @Override
            public void closed(CVetoClosingEvent event) {
                onClosed.fire(SceneDock.this);
            }
        });

        addFocusListener(new CFocusListener() {
            @Override
            public void focusGained(CDockable dockable) {
                onFocusGained.fire(SceneDock.this);
            }

            @Override
            public void focusLost(CDockable dockable) {
                onFocusLost.fire(SceneDock.this);
            }
        });
    }
    //endregion

    public final EditorPanel editorPanel;

    //region sceneFile : File
    protected File sceneFile;

    public File getSceneFile() {
        return sceneFile;
    }

    public void setSceneFile(File sceneFile) {
        this.sceneFile = sceneFile;
    }
    //endregion

    protected void reloadScene(){
        if( sceneFile!=null ){
            loadScene(sceneFile);
        }
    }
    public void saveSceneAs( boolean forceSaveAs ){
        var f = sceneFile;
        if( f!=null && !forceSaveAs ){
            saveSceneAs(f);
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
            fileChooser.setCurrentDirectory(new java.io.File("."));
        }
        if( fileChooser.showSaveDialog(null)==JFileChooser.APPROVE_OPTION ){
            saveSceneAs( new xyz.cofe.io.fs.File(fileChooser.getSelectedFile().toString()));
        }
    }
    public void saveSceneAs( xyz.cofe.io.fs.File file ){
        if( file==null )throw new IllegalArgumentException( "file==null" );

        var map = new MapStore().store(editorPanel.getScene());
        var om = new ObjectMapper();
        om.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            var json = om.writeValueAsString(map);
            file.writeText(json, StandardCharsets.UTF_8);

            setTitleText(file.getName());
            setSceneFile(file);
        } catch (IOError | JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    public void loadScene(){
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
            fileChooser.setCurrentDirectory(new java.io.File("."));
        }
        if( fileChooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION ){
            loadScene(new xyz.cofe.io.fs.File(fileChooser.getSelectedFile().toString()));
        }
    }
    public void loadScene( xyz.cofe.io.fs.File file ){
        if( file==null )throw new IllegalArgumentException( "file==null" );

        var om = new ObjectMapper();
        om.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            var jsonData = om.readValue(file.readText(StandardCharsets.UTF_8),Object.class);
            if( jsonData instanceof Map){
                //noinspection unchecked,rawtypes,rawtypes
                var sceneObj = new MapStore().restore((Map)jsonData);
                if( sceneObj instanceof Scene ){
                    editorPanel.setScene((Scene) sceneObj);
                    //editorPanel.getScene().assign((Scene) sceneObj);
                    setTitleText(file.getName());
                    setSceneFile(file);
                }
            }
        } catch (IOError | JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
