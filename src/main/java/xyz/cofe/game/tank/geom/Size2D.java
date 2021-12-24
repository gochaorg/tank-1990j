package xyz.cofe.game.tank.geom;

/**
 * Возвращает размер объекта
 */
public interface Size2D {
    /**
     * Ширина объекта
     * @return ширина объекта
     */
    double width();

    /**
     * Высота объекта
     * @return высота объекта
     */
    double height();

    /**
     * Площадь объекта
     * @return площать объекта, по умолчанию простое умножение width * height
     */
    default double area(){
        return width() * height();
    }

    public static Size2D of(double width, double height ){
        return new Size2D() {
            @Override
            public double width(){
                return width;
            }

            @Override
            public double height(){
                return height;
            }

            @Override
            public String toString(){
                return "Size{ w="+width+" h="+height+" }";
            }
        };
    }

    public static Size2D of(Rect rect){
        if( rect==null )throw new IllegalArgumentException( "rect==null" );
        return of( rect.width(), rect.height() );
    }
}
