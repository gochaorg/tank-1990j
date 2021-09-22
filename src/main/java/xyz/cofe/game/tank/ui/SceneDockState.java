package xyz.cofe.game.tank.ui;

import bibliothek.gui.dock.common.MultipleCDockableLayout;
import bibliothek.util.xml.XElement;
import xyz.cofe.game.tank.geom.Point;
import xyz.cofe.io.fs.File;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SceneDockState implements MultipleCDockableLayout {
    public String sceneFileName;
    public String title;
    public long lastFocusGained;
    public Point origin;

    public SceneDockState(){}
    public SceneDockState(SceneDock sceneDock){
        if( sceneDock!=null ){
            var f = sceneDock.getSceneFile();
            if( f!=null ){
                sceneFileName = f.toString();
            }
            title = sceneDock.getTitleText();
            lastFocusGained = sceneDock.lastFocusGained;
            origin = sceneDock.editorPanel.getOrigin();
        }
    }

    @Override
    public void writeStream(DataOutputStream out) throws IOException {

    }

    @Override
    public void readStream(DataInputStream in) throws IOException {

    }

    @Override
    public void writeXML(XElement element) {
        if( sceneFileName!=null )element.addElement("sceneFileName").setString(sceneFileName);
        if( title!=null )element.addElement("title").setString(title);
        element.addElement("lastFocusGained").setLong(lastFocusGained);
        if( origin!=null )element.addElement("origin").setString(origin.toString());
    }

    @Override
    public void readXML(XElement element) {
        var e = element.getElement("sceneFileName");
        if( e!=null ) sceneFileName = e.getString();

        e = element.getElement("title");
        if( e!=null ) title = e.getString();

        e = element.getElement("lastFocusGained");
        if( e!=null ) lastFocusGained = e.getLong();

        e = element.getElement("origin");
        if( e!=null ) {
            try {
                origin = Point.parse(e.getString());
            } catch (Throwable err){
                System.err.println(err);
            }
        }
    }

    public SceneDock createSceneDock(SceneDockFactory factory){
        return new SceneDock(factory, this);
    }
}
