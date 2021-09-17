package xyz.cofe.game.tank.geom;

import java.awt.event.MouseEvent;
import java.util.Optional;

/**
 * Прямоугольная рамка с началом координат в левом,верхнем углу
 */
public interface Rect extends Size2D {
    /**
     * Левый край объекта
     * @return Левый край объекта
     */
    double left();

    /**
     * Верхний край объекта
     * @return Верхний край объекта
     */
    double top();

    /**
     * Правый край объекта
     * @return Правый край объекта
     */
    double right();

    /**
     * Нижний край объекта
     * @return Нижний край объекта
     */
    double bottom();

    /**
     * Создаение прямоугольника
     * @param left Левый край объекта
     * @param top Верхний край объекта
     * @param right Правый край объекта
     * @param bottom ижний край объекта
     * @return прямоугольник
     */
    public static Rect rect( double left, double top, double right, double bottom ){
        var swapLeftRight = right < left;
        var swapTopDown = bottom < top;

        var x0 = swapLeftRight ? right : left;
        var x1 = swapLeftRight ? left : right;
        var y0 = swapTopDown ? bottom : top;
        var y1 = swapTopDown ? top : bottom;
        double w = x1 - x0;
        double h = y1 - y0;

        return new Rect() {
            @Override
            public double left(){
                return x0;
            }

            @Override
            public double top(){
                return y0;
            }

            @Override
            public double right(){
                return x1;
            }

            @Override
            public double bottom(){
                return y1;
            }

            @Override
            public double width(){
                return w;
            }

            @Override
            public double height(){
                return h;
            }

            @Override
            public String toString(){
                return "Rect{ left="+x0+" top="+y0+" right="+x1+" bottom="+y1+" w="+w+" h="+h+" }";
            }
        };
    }

    public static Rect rect( Point p0, Point p1 ){
        if( p0==null )throw new IllegalArgumentException( "p0==null" );
        if( p1==null )throw new IllegalArgumentException( "p1==null" );
        return rect(p0.x(), p0.y(), p1.x(), p1.y());
    }

    /**
     * Создаение прямоугольника
     * @param left Левый край объекта
     * @param top Верхний край объекта
     * @param width ширина
     * @param height высота
     * @return прямоугольник
     */
    public static Rect of( double left, double top, double width, double height ){
        var rgt = left + width;
        var btm = top + height;
        return rect(left,top,rgt,btm);
    }

    /**
     * Проверка наличия координат в прямоугольнике
     * @param x координата
     * @param y координата
     * @return true = left &lt;= x < right &amp;&amp; top &lt;= y &lt; bottom
     */
    public default boolean contains( double x, double y ){
        return left() <= x && x < right() && top() <= y && y< bottom();
    }

    /**
     * Проверка наличия координат в прямоугольнике
     * @param p координата
     * @return true = left &lt;= x < right &amp;&amp; top &lt;= y &lt; bottom
     */
    public default boolean contains( Point p ){
        if( p==null )throw new IllegalArgumentException( "p==null" );
        return contains(p.x(), p.y());
    }

    /**
     * Проверка наличия координат в прямоугольнике
     * @param p координата
     * @return true = left &lt;= x < right &amp;&amp; top &lt;= y &lt; bottom
     */
    public default boolean contains( java.awt.Point p ){
        if( p==null )throw new IllegalArgumentException( "p==null" );
        return contains( p.getX(), p.getY() );
    }

    /**
     * Проверка наличия координат в прямоугольнике
     * @param ev координата
     * @return true = left &lt;= x < right &amp;&amp; top &lt;= y &lt; bottom
     */
    public default boolean contains( MouseEvent ev ){
        if( ev==null )throw new IllegalArgumentException( "ev==null" );
        return contains( ev.getPoint() );
    }

    /**
     * Пересечение квадратов
     * @param rect кавдрат
     * @return пересечение
     */
    public default Optional<Rect> intersection( Rect rect ){
        if( rect==null )throw new IllegalArgumentException( "rect==null" );
        double a_x0 = Math.min(left(),right());
        double a_y0 = Math.min(top(),bottom());
        double a_x1 = Math.max(right(), left());
        double a_y1 = Math.max(bottom(), top());

        double b_x0 = Math.min(rect.left(), rect.right());
        double b_y0 = Math.min(rect.top(), rect.bottom());
        double b_x1 = Math.max(rect.right(), rect.left());
        double b_y1 = Math.max(rect.bottom(), rect.top());

        if( a_x1<=b_x0 )return Optional.empty();
        if( a_y1<=b_y0 )return Optional.empty();

        if( a_x0>b_x1 )return Optional.empty();
        if( a_y0>b_y1 )return Optional.empty();

//        double a_dx = Math.abs(a_x0 - a_x1);
//        double b_dx = Math.abs(b_x0 - b_x1);
//        double a_dy = Math.abs(a_y0 - a_y1);
//        double b_dy = Math.abs(b_y0 - b_y1);

        Double iX0 = null;
        Double iX1 = null;
        Double iY0 = null;
        Double iY1 = null;

        if( b_x0 <= a_x0 && a_x0 <= b_x1 ){
            iX0 = a_x0;
        }else if( a_x0 <= b_x0 && b_x0 <= a_x1 ){
            iX0 = b_x0;
        }

        if( b_x0 <= a_x1 && a_x1 <= b_x1 ){
            iX1 = a_x1;
        }else if( a_x0 <= b_x1 && b_x1 <= a_x1 ){
            iX1 = b_x1;
        }

        if( b_y0 <= a_y0 && a_y0 <= b_y1 ){
            iY0 = a_y0;
        }else if( a_y0 <= b_y0 && b_y0 <= a_y1 ){
            iY0 = b_y0;
        }

        if( b_y0 <= a_y1 && a_y1 <= b_y1 ){
            iY1 = a_y1;
        }else if( a_y0 <= b_y1 && b_y1 <= a_y1 ){
            iY1 = b_y1;
        }

        if( iX0!=null && iX1!=null && iY0!=null && iY1!=null ){
            return Optional.of(rect(
                Math.min(iX0,iX1),
                Math.min(iY0,iY1),
                Math.max(iX0,iX1),
                Math.max(iY0,iY1)
            ));
        }

        return Optional.empty();
    }

    /**
     * Вычисляет общую рамку, объединяющую текущую и указанную
     * @param rect рамка
     * @return общая рамка
     */
    public default Rect bounds( Rect rect ){
        if( rect==null )throw new IllegalArgumentException( "rect==null" );
        double x0 = Math.min( left(), rect.left() );
        double y0 = Math.min( top(), rect.top() );
        double x1 = Math.max( right(), rect.right() );
        double y1 = Math.max( bottom(), rect.bottom() );
        return Rect.rect(x0,y0,x1,y1);
    }

    /**
     * Возвращает центральную точку
     * @return центральная точка
     */
    public default Point getCentralPoint(){
        return Point.of(left()+ width()/2, top()+ height()/2);
    }

    /**
     * Возвращает левую верхнюю точку
     * @return точка
     */
    public default Point leftTopPoint(){
        return Point.of(left(), top());
    }

    /**
     * Возвращает правую верхнюю точку
     * @return точка
     */
    public default Point rightTopPoint(){
        return Point.of(right(), top());
    }

    /**
     * Возвращает левую нижнюю точку
     * @return точка
     */
    public default Point leftBottomPoint(){
        return Point.of(left(), bottom());
    }

    /**
     * Возвращает правую нижнюю точку
     * @return точка
     */
    public default Point rightBottomPoint(){
        return Point.of(right(), bottom());
    }
}
