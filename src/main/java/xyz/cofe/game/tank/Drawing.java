package xyz.cofe.game.tank;

import java.awt.*;

/**
 * Отображение объекта
 */
public interface Drawing {
    /**
     * Отображение объекта
     * @param gs интерфейс
     * @param x координаты отображения
     * @param y координаты отображения
     */
    void draw(Graphics2D gs, int x, int y );
}
