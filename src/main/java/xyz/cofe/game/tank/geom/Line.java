package xyz.cofe.game.tank.geom;

import xyz.cofe.fn.Tuple3;

import java.util.Optional;

/**
 * Линия на плоскости
 *
 * <p>
 *     Переводы
 * <ul>
 *     <li>Straight - прямая
 *     <li>Line - линия
 *     <li>Line segment - отрезок
 *     <li>Ray - лучь
 * </ul>
 */
public interface Line {
    /**
     * Начало линии, координаты
     * @return Начало линии, координаты
     */
    double x0();

    /**
     * Начало линии, координаты
     * @return Начало линии, координаты
     */
    double y0();

    /**
     * Конец линии, координаты
     * @return Конец линии, координаты
     */
    double x1();

    /**
     * Конец линии, координаты
     * @return Конец линии, координаты
     */
    double y1();

    /**
     * Создание линии
     * @param x0 Начало линии, координаты
     * @param y0 Начало линии, координаты
     * @param x1 Конец линии, координаты
     * @param y1 Конец линии, координаты
     * @return Линия
     */
    static Line of( double x0, double y0, double x1, double y1 ){
        return new Line() {
            @Override
            public double x0(){
                return x0;
            }

            @Override
            public double y0(){
                return y0;
            }

            @Override
            public double x1(){
                return x1;
            }

            @Override
            public double y1(){
                return y1;
            }

            @Override
            public String toString(){
                return "Line{ x0="+x0+" y0="+y0+" x1="+x1+" y1="+y1+" }";
            }
        };
    }

    /**
     * Создание линии
     * @param p0 Начало линии, координаты
     * @param p1 Конец линии, координаты
     * @return Линия
     */
    static Line of( Point p0, Point p1 ){
        if( p0==null )throw new IllegalArgumentException( "p0==null" );
        if( p1==null )throw new IllegalArgumentException( "p1==null" );
        return of( p0.x(), p0.y(), p1.x(), p1.y() );
    }

    /**
     * Пересечение прямых
     * @param line вторая прямая
     * @return точка пересечения, если есть
     */
    public default Optional<Point> intersection( Line line ){
        if( line==null )throw new IllegalArgumentException( "line==null" );
        // https://ru.wikipedia.org/wiki/%D0%9F%D0%B5%D1%80%D0%B5%D1%81%D0%B5%D1%87%D0%B5%D0%BD%D0%B8%D0%B5_%D0%BF%D1%80%D1%8F%D0%BC%D1%8B%D1%85

        // Делимое (dividend, numerator)
        double numerator;

        // Если две прямые параллельны или совпадают, знаменатель обращается в нуль:
        // Знаменатель
        double x1 = x0();
        double x2 = x1();
        double x3 = line.x0();
        double x4 = line.x1();

        double y1 = y0();
        double y2 = y1();
        double y3 = line.y0();
        double y4 = line.y1();

        double divider = ((x1- x2)*(y3-y4))-((y1-y2)*(x3-x4));
        if( divider==0 )return Optional.empty();

        double x = ((x1*y2-y1*x2)*(x3-x4)-(x1-x2)*(x3*y4-y3*x4)) / divider;
        double y = ((x1*y2-y1*x2)*(y3-y4)-(y1-y2)*(x3*y4-y3*x4)) / divider;

        return Optional.of(Point.of(x,y));
    }

    /**
     * Получение коэффициентов прямой A,B,C
     * @return кооэфициенты: A*x+B*y+C = 0
     */
    public default Tuple3<Double,Double,Double> factors(){
        double a = y0() - y1();
        double b = x1() - x0();
        double c = - a * x0() - b * y0();
        return Tuple3.of(a,b,c);
    }

    public default Optional<Point> projectionOfX( double x ){
        double xdiff = x1() - x0();
        if( xdiff==0 ){
            if( x==x1() ){
                double l = Math.abs(y0() - y1());
                if( l<=0 ){
                    return Optional.of(Point.of(x,y0()));
                }else{
                    return Optional.of(Point.of(x,Math.min(y0(),y1())+l/2));
                }
            }else {
                return Optional.empty();
            }
        }

        double ydiff = y1() - y0();
        if( ydiff==0 ){
            return Optional.of(Point.of(x,y0()));
        }

        var factrs = factors();
        // A*x+B*y+C = 0
        // A*x + B*y + C = 0
        // A*x + B*y = -C
        // B*y = -C - A*x
        // y = (-C - A*x) / B
        var y = (-factrs.c() - factrs.a()*x) / factrs.b();
        return Optional.of(Point.of(x,y));
    }

    public default Optional<Point> projectionOfY( double y ){
        double xdiff = x1() - x0();
        if( xdiff==0 ){
            return Optional.of(Point.of(x0(),y));
        }

        double ydiff = y1() - y0();
        if( ydiff==0 ){
            if( y==y1() ){
                double l = Math.abs(x0() - x1());
                if( l<=0 ){
                    return Optional.of(Point.of(x0(),y));
                }else{
                    return Optional.of(Point.of(Math.min(x0(),x1())+l/2, y));
                }
            }else {
                return Optional.empty();
            }
        }

        var factrs = factors();
        // A*x+B*y+C = 0
        // A*x + B*y + C = 0
        // A*x + B*y = -C
        // A*x = -C - B*y
        // x = (-C - B*y) / A
        var x = (-factrs.c() - factrs.b()*y) / factrs.a();
        return Optional.of(Point.of(x,y));
    }
}
