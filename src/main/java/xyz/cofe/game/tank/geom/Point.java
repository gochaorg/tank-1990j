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
}
