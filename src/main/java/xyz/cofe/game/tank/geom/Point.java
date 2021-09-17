package xyz.cofe.game.tank.geom;

import java.awt.event.MouseEvent;

/**
 * Точка на плоскости
 */
public interface Point {
    /**
     * Координаты
     * @return Координаты
     */
    double x();

    /**
     * Координаты
     * @return Координаты
     */
    double y();

    /**
     * Создание точки
     * @param x Координаты
     * @param y Координаты
     * @return Точка
     */
    static Point of( double x, double y ){
        return new Point() {
            @Override
            public double x(){
                return x;
            }

            @Override
            public double y(){
                return y;
            }

            @Override
            public String toString(){
                return "Point{ x="+x+" y="+y+" }";
            }
        };
    }

    /**
     * Создание точки
     * @param mouseEvent Координаты
     * @return Точка
     */
    static Point of( MouseEvent mouseEvent ){
        if( mouseEvent==null )throw new IllegalArgumentException( "mouseEvent==null" );
        return of( mouseEvent.getX(), mouseEvent.getY() );
    }

    /**
     * Перемещение точки
     * @param pt смещение
     * @return смещенная точка
     */
    default Point translate( Point pt ){
        if( pt==null )throw new IllegalArgumentException( "pt==null" );
        return of( x()+pt.x(), y()+ pt.y() );
    }

    /**
     * Инверсия координат по осям
     * @return инвертированная точка
     */
    default Point negative(){
        return of( -x(), -y() );
    }

    /**
     * Создание отрезка
     * @param x целевая координата
     * @param y целевая координата
     * @return отрезок
     */
    default Line toLine( double x, double y ){
        return Line.of(x(), y(), x, y);
    }

    /**
     * Создание отрезка
     * @param p целевая координата
     * @return отрезок
     */
    default Line toLine( Point p ){
        if( p==null )throw new IllegalArgumentException( "p==null" );
        return Line.of(x(), y(), p.x(), p.y() );
    }

    /**
     * Расчет дистанции
     * @param x  целевая координата
     * @param y целевая координата
     * @return отрезок
     */
    default double distance( double x, double y ){
        double xd = x() - x;
        double yd = y() - y;
        if( xd==0 && yd==0 ){
            return 0;
        }
        return Math.sqrt( xd*xd + yd*yd );
    }

    /**
     * Расчет дистанции
     * @param p целевая координата
     * @return отрезок
     */
    default double distance( Point p ){
        if( p==null )throw new IllegalArgumentException( "p==null" );
        double xd = x() - p.x();
        double yd = y() - p.y();
        if( xd==0 && yd==0 ){
            return 0;
        }
        return Math.sqrt( xd*xd + yd*yd );
    }
}
