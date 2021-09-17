package xyz.cofe.game.tank.ui.canvas;

import xyz.cofe.game.tank.geom.Point;
import xyz.cofe.game.tank.geom.Rect;

import javax.swing.JComponent;

/**
 * Отображение UI элемента в контексте компонента
 * @param <SELF> Тип UI элемента | SELF ссылка
 */
public abstract class CanvasHost<SELF extends CanvasHost<SELF>> {
    //region component : JComponent
    private JComponent component;

    /**
     * Возвращает ссылку на компонент в рамках которого рендеринг
     * @return компонент
     */
    public JComponent component(){ return component; }

    /**
     * Указывает ссылку на компонент в рамках которого рендеринг
     * @param c компонент
     * @return SELF ссылка
     */
    public SELF component( JComponent c ){
        component = c;
        //noinspection unchecked
        return (SELF) this;
    }
    //endregion

    //region origin : Point
    private Point origin = Point.of(0,0);

    /**
     * Возвращает точку отсчета
     * @return точка отсчета
     */
    public Point origin(){ return origin; }

    /**
     * Указывает точку отсчета
     * @param p точка отсчета
     * @return SELF ссылка
     */
    @SuppressWarnings("UnusedReturnValue")
    public SELF origin(Point p){
        if( p==null )throw new IllegalArgumentException( "p==null" );
        origin = p;
        //noinspection unchecked
        return (SELF) this;
    }
    //endregion

    protected Point translate( Point p ){
        if( p==null )throw new IllegalArgumentException( "p==null" );
        return Point.of(
            translateX(p.x()),
            translateY(p.y())
        );
    }

    protected double translateX( double x ){ return x - origin().x(); }
    protected double translateY( double y ){ return y - origin().y(); }

    protected Rect viewPortRect( double gridStep ){
        if( gridStep<=0 )throw new IllegalArgumentException( "gridStep<=0" );

        var cmpt = component();
        if( cmpt==null )throw new IllegalStateException( "cmpt==null" );

        double viewWidth = cmpt.getWidth();
        double viewHeight = cmpt.getHeight();

        double xBegin = origin().x();
        double xEnd = xBegin + viewWidth;
        double xCorr = xBegin % gridStep;
        xBegin -= xCorr;
        //xBegin -= Math.abs(xCorr);

        double yBegin = origin().y();
        double yEnd = yBegin + viewHeight;
        double yCorr = yBegin % gridStep;
        yBegin -= yCorr;
        //yBegin -= Math.abs(yCorr);

        return Rect.rect( xBegin, yBegin, xEnd, yEnd );
    }
}
