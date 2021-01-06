package xyz.cofe.game.tank.geom;

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

    default Line toLine( double x, double y ){
        return Line.of(x(), y(), x, y);
    }

    default Line toLine( Point p ){
        if( p==null )throw new IllegalArgumentException( "p==null" );
        return Line.of(x(), y(), p.x(), p.y() );
    }

    default double distance( double x, double y ){
        double xd = x() - x;
        double yd = y() - y;
        if( xd==0 && yd==0 ){
            return 0;
        }
        return Math.sqrt( xd*xd + yd*yd );
    }

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
