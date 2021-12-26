package xyz.cofe.game.tank;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.*;

/**
 * Инструмент редактора карты
 */
public abstract class Tool {
    public abstract String getToolName();

    protected JComponent canvas;

    /**
     * Событие мыши
     * @param canvas холст
     * @param e событие
     */
    public void mouseEvent(JComponent canvas, MouseEvent e){
        this.canvas = canvas;
        if( e!=null ){
            if( e.getID()==MouseEvent.MOUSE_CLICKED ){
                onClicked(e);
            }else if( e.getID()==MouseEvent.MOUSE_MOVED ){
                onMoved(e);
            }else if( e.getID()==MouseEvent.MOUSE_DRAGGED ){
                onDragged(e);
            }else if( e.getID()==MouseEvent.MOUSE_PRESSED ){
                onPressed(e);
            }else if( e.getID()==MouseEvent.MOUSE_RELEASED ){
                onReleased(e);
            }
        }
    }

    protected void onClicked( MouseEvent e ){
    }

    protected void onMoved( MouseEvent e ){
    }

    protected void onDragged( MouseEvent e ){
    }

    protected void onPressed( MouseEvent e ){
    }

    protected void onReleased( MouseEvent e ){
    }

    /**
     * Событие клавиатуры
     * @param canvas холст
     * @param e событие
     */
    public void keyEvent(JComponent canvas, KeyEvent e){
        this.canvas = canvas;
        if( e!=null ){
            if( e.getID()==KeyEvent.KEY_PRESSED ){
                onKeyPressed(e);
            }else if( e.getID()==KeyEvent.KEY_TYPED ){
                onKeyTyped(e);
            }
        }
    }

    protected void onKeyPressed(KeyEvent e){
    }

    protected void onKeyTyped(KeyEvent e){
    }

    public void draw(JComponent canvas, Graphics2D gs){
        this.canvas = canvas;
        onDraw(gs);
    }
    protected void onDraw(Graphics2D gs){

    }
}
