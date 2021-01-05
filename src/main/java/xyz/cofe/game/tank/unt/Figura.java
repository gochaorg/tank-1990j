package xyz.cofe.game.tank.unt;

import xyz.cofe.game.tank.Drawing;
import xyz.cofe.game.tank.Moveable;
import xyz.cofe.game.tank.geom.Rect;

/**
 * Абстрактная фигура, с координатами
 */
public abstract class Figura<SELF extends Figura<SELF>> implements Drawing, Rect, Moveable<SELF> {
    //region left : double - Левый край объекта
    protected double left;
    /**
     * Левый край объекта
     * @return Левый край объекта
     */
    public double left(){ return left; }
    @SuppressWarnings("unchecked")
    public SELF left(double left){
        this.left = left;
        return (SELF)this;
    }
    //endregion
    //region top : double - Верхний край объекта
    protected double top;
    /**
     * Верхний край объекта
     * @return Верхний край объекта
     */
    public double top(){ return top; }
    @SuppressWarnings("unchecked")
    public SELF top(double top){
        this.top = top;
        return (SELF)this;
    }
    //endregion
    //region right() : double - Правый край объекта
    /**
     * Правый край объекта
     * @return Правый край объекта
     */
    @Override
    public double right(){
        return left()+width();
    }
    //endregion
    //region bottom() : double - Нижний край объекта
    /**
     * Нижний край объекта
     * @return Нижний край объекта
     */
    @Override
    public double bottom(){
        return top()+width();
    }
    //endregion
    //region width, height
    /**
     * Ширина объекта
     * @return ширина объекта
     */
    @Override
    public abstract double width();

    /**
     * Высота объекта
     * @return высота объекта
     */
    @Override
    public abstract double height();
    //endregion
    //region location(left,top)
    /**
     * Указание левого верхнего угла объекта
     * @param left левый край объекта
     * @param top верхний край объекта
     * @return self ссылка
     */
    @SuppressWarnings("unchecked")
    public SELF location(double left, double top ){
        left(left);
        top(top);
        return (SELF) this;
    }
    //endregion
}
