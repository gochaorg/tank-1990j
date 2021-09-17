package xyz.cofe.game.tank.unt;

import java.util.Set;
import xyz.cofe.ecolls.ListenersHelper;
import xyz.cofe.game.tank.Drawing;
import xyz.cofe.game.tank.Moveable;
import xyz.cofe.game.tank.geom.Point;
import xyz.cofe.game.tank.geom.Rect;
import xyz.cofe.gui.swing.bean.UiBean;

/**
 * Абстрактная фигура, с координатами
 */
public abstract class Figura<SELF extends Figura<SELF>> implements Drawing, Rect, Moveable<SELF> {
    //region left : double - Левый край объекта
    private double left;
    /**
     * Левый край объекта
     * @return Левый край объекта
     */
    public double left(){ return left; }
    @SuppressWarnings("unchecked")
    private SELF left(double left){
        this.left = left;
        return (SELF)this;
    }
    //endregion
    //region top : double - Верхний край объекта
    private double top;
    /**
     * Верхний край объекта
     * @return Верхний край объекта
     */
    public double top(){ return top; }
    @SuppressWarnings("unchecked")
    private SELF top(double top){
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
    public SELF location( double left, double top ){
        double x0 = left();
        double y0 = top();

        left(left);
        top(top);

        fireMoved(Point.of(x0,y0), Point.of(left,top));

        return (SELF) this;
    }

    /**
     * Указание левого верхнего угла объекта
     * @param p координаты левого, верхнего угла
     * @return self ссылка
     */
    public SELF location( Point p ){
        if( p==null )throw new IllegalArgumentException( "p==null" );
        return location(p.x(), p.y());
    }

    public Point getLocation(){ return leftTopPoint(); }
    public void setLocation(Point p){
        if( p==null )throw new IllegalArgumentException( "p==null" );
        location(p);
    }
    //endregion

    protected final ListenersHelper<FiguraListener<SELF>, FiguraEvent<SELF>> figuraListeners
        = new ListenersHelper<>(FiguraListener::figuraEvent);

    @SuppressWarnings("unchecked")
    protected void fireMoved(Point from, Point to){
        fireFiguraEvent(new FiguraMoved<SELF>((SELF) this,from,to));
    }

    /**
     * Проверка наличия подписчика в списке обработки
     * @param listener подписчик
     * @return true - есть в списке обработки
     */
    public boolean hasFiguraListener(FiguraListener<SELF> listener){
        return figuraListeners.hasListener(listener);
    }

    /**
     * Получение списка подписчиков
     * @return подписчики
     */
    @UiBean(forceHidden = true)
    public Set<FiguraListener<SELF>> getFiguraListeners(){
        return figuraListeners.getListeners();
    }

    /**
     * Добавление подписчика.
     * @param listener Подписчик.
     * @return Интерфес для отсоединения подписчика
     */
    public AutoCloseable addFiguraListener(FiguraListener<SELF> listener){
        return figuraListeners.addListener(listener);
    }

    /**
     * Добавление подписчика.
     * @param listener Подписчик.
     * @param weakLink true - добавить как weak ссылку / false - как hard ссылку
     * @return Интерфес для отсоединения подписчика
     */
    public AutoCloseable addFiguraListener(FiguraListener<SELF> listener, boolean weakLink){
        return figuraListeners.addListener(listener, weakLink);
    }

    /**
     * Добавление подписчика.
     * @param listener Подписчик.
     * @param weakLink true - добавить как weak ссылку / false - как hard ссылку
     * @param limitCalls Ограничение кол-ва вызовов, 0 или меньше - нет ограничений
     * @return Интерфес для отсоединения подписчика
     */
    public AutoCloseable addFiguraListener(FiguraListener<SELF> listener, boolean weakLink, int limitCalls){
        return figuraListeners.addListener(listener, weakLink, limitCalls);
    }

    /**
     * Удаление подписчика из списка обработки
     * @param listener подписчик
     */
    public void removeFiguraListener(FiguraListener<SELF> listener){
        figuraListeners.removeListener(listener);
    }

    public void removeAllFiguraListeners(){
        figuraListeners.removeAllListeners();
    }

    /**
     * Рассылка уведомления подписчикам
     * @param event уведомление
     */
    protected void fireFiguraEvent(FiguraEvent<SELF> event){
        figuraListeners.fireEvent(event);
    }
}
