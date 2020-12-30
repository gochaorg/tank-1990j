package xyz.cofe.game.tank;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameMap implements Drawing {
    protected List<Drawing> drawings = new ArrayList<>();

    public void add( Drawing drawing ){

    }

    @Override
    public void draw(Graphics2D gs){
        if( gs==null )throw new IllegalArgumentException( "gs==null" );
    }
}
