package xyz.cofe.game.tank.ui;

import xyz.cofe.game.tank.geom.Point;

import java.awt.Component;
import java.awt.event.MouseEvent;

/**
 * События мыши
 */
public class MouseEv implements Point {
    /**
     * Конструктор
     */
    public MouseEv(){}

    /**
     * Конструктор
     * @param x координаты
     * @param y координаты
     */
    public MouseEv(double x, double y){
        this.x = x;
        this.y = y;
    }

    /**
     * Конструктор копирования
     * @param ev образец
     */
    public MouseEv(MouseEvent ev){
        this( ev, null );
    }

    /**
     * Конструктор копирования
     * @param ev образец
     * @param component с каким компонентом связана мышь
     */
    public MouseEv(MouseEvent ev, Component component){
        if( ev==null )throw new IllegalArgumentException( "ev==null" );
        this.x = ev.getX();
        this.y = ev.getY();
        button = ev.getButton();
        shift = ev.isShiftDown();
        control = ev.isControlDown();
        alt = ev.isAltDown();
        meta = ev.isMetaDown();
        clickCount = ev.getClickCount();
        this.component = component;
    }

    protected int button;

    /**
     * Номер нажатой клавиши
     * @return номер нажатой клавиши, см {@link MouseEvent#BUTTON1}
     */
    public int getButton(){ return button; }

    /**
     * Возвращает признак, что нажата левая клавиша мыши
     * @return true - нажата левая клавиша мыши
     */
    public boolean isLeftButton(){ return button == MouseEvent.BUTTON1; }

    /**
     * Возвращает признак, что нажата правая клавиша мыши
     * @return true - нажата правая клавиша мыши
     */
    public boolean isRightButton(){ return button == MouseEvent.BUTTON3; }

    protected boolean shift;

    /**
     * Возвращает признак нажатия SHIFT
     * @return true - клавиша SHIFT нажата
     */
    public boolean isShift(){ return shift; }

    protected boolean control;

    /**
     * Возвращает признак нажатия CONTROL
     * @return true - клавиша CONTROL нажата
     */
    public boolean isControl(){ return control; }

    protected boolean alt;

    /**
     * Возвращает признак нажатия ALT
     * @return true - клавиша ALT нажата
     */
    public boolean isAlt(){ return alt; }

    protected boolean meta;

    /**
     * Возвращает признак нажатия META (Win)
     * @return true - клавиша META (Win) нажата
     */
    public boolean isMeta(){ return meta; }

    protected int clickCount;

    /**
     * Возвращает кол-во кликов мыши
     * @return кол-во кликов мыши
     */
    public int getClickCount(){ return clickCount; }

    protected Component component;

    /**
     * Возвращает компонент с которым связано событие
     * @return компонент или null
     */
    public Component getComponent(){ return component; }

    protected double x;

    /**
     * Возвращает координаты мыши
     * @return координаты
     */
    @Override public double x() { return x; }

    /**
     * Указывает координаты мыши
     * @param v координаты
     * @return SELF ссылка
     */
    public MouseEv x( double v ){
        this.x = v;
        return this;
    }

    protected double y;

    /**
     * Возвращает координаты мыши
     * @return координаты
     */
    @Override public double y() { return y; }


    /**
     * Указывает координаты мыши
     * @param v координаты
     * @return SELF ссылка
     */
    public MouseEv y( double v ){
        this.y = v;
        return this;
    }

    /**
     * Смещает координаты
     * @param p на сколько сдвинуть
     * @return SELF ссылка
     */
    public MouseEv shift( Point p ){
        if( p==null )throw new IllegalArgumentException( "p==null" );
        this.x += p.x();
        this.y += p.y();
        return this;
    }

    /**
     * Масштабирует координаты
     * @param scale коэффициент масштабирования
     * @return SELF ссылка
     */
    public MouseEv scale( double scale ){
        this.x = this.x * scale;
        this.y = this.y * scale;
        return this;
    }
}
