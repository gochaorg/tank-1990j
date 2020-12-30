package xyz.cofe.game.tank;

import xyz.cofe.num.BaseNumbers;

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
        };
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
}
