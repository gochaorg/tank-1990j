package xyz.cofe.game.tank;

import java.awt.*;

/**
 * Отображение объекта в заданных координатах
 */
public interface PositionalDrawing extends Drawing {
    /**
     * Отображение объекта
     * @param gs интерфейс
     * @param x координаты отображения
     * @param y координаты отображения
     */
    void draw(Graphics2D gs, double x, double y );

    @Override
    default void draw(Graphics2D gs){
        draw(gs, 0, 0);
    }
}
