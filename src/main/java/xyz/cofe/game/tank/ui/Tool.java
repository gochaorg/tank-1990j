package xyz.cofe.game.tank.ui;

import xyz.cofe.game.tank.geom.Point;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.*;

/**
 * Интерфейс инструмента
 */
public interface Tool {
    /**
     * Возвращает наименование инструмента
     * @return Наименование инструмента
     */
    public String name();

    /**
     * Возвращает изображение инструмента
     * @return изображение инструмента
     */
    public BufferedImage image();

    /**
     * Создает кнопку для отображения инструмента
     * @return кнопка
     */
    default JButton toToolbarButton(){
        JButton but = new JButton();
        but.setText(name());
        var img = image();
        but.setIcon(new ImageIcon(img));
        return but;
    }

    /**
     * Клик мыши в заданных координатах
     * @param pt координаты
     */
    default void onMouseClicked(MouseEv pt){}

    /**
     * Клик мыши в заданных координатах
     * @param pt координаты
     */
    default void onMousePressed(MouseEv pt){}

    /**
     * Клик мыши в заданных координатах
     * @param pt координаты
     */
    default void onMouseReleased(MouseEv pt){}

    /**
     * Клик мыши в заданных координатах
     * @param pt координаты
     */
    default void onMouseDragged(MouseEv pt){}

    /**
     * Клик мыши в заданных координатах
     * @param pt координаты
     */
    default void onMouseExited(MouseEv pt){}

    /**
     * Нажатие клавиши клавиатуры
     * @param keyEv событие
     */
    default void onKeyPressed(KeyEv keyEv){}

    /**
     * Отпускание клавиши клавиатуры
     * @param keyEv событие
     */
    default void onKeyReleased(KeyEv keyEv){}

    /**
     * Перерисовка экрана
     * @param gs контекст
     */
    default void onPaint(Graphics2D gs){
    }
}
