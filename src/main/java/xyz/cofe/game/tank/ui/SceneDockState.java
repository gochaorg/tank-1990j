package xyz.cofe.game.tank.ui;

import bibliothek.gui.dock.common.MultipleCDockableLayout;
import bibliothek.util.xml.XElement;
import xyz.cofe.io.fs.File;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SceneDockState implements MultipleCDockableLayout {
    public String sceneFileName;
    public String title;
    public long lastFocusGained;

    public SceneDockState(){}
    public SceneDockState(SceneDock sceneDock){
        if( sceneDock!=null ){
            var f = sceneDock.getSceneFile();
            if( f!=null ){
                sceneFileName = f.toString();
            }
            title = sceneDock.getTitleText();
            lastFocusGained = sceneDock.lastFocusGained;
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
    }

    @Override
    public void readXML(XElement element) {
        var e = element.getElement("sceneFileName");
        if( e!=null ) sceneFileName = e.getString();

        e = element.getElement("title");
        if( e!=null ) title = e.getString();

        e = element.getElement("lastFocusGained");
        if( e!=null ) lastFocusGained = e.getLong();
    }

    public SceneDock createSceneDock(SceneDockFactory factory){
        return new SceneDock(factory, this);
    }
}
