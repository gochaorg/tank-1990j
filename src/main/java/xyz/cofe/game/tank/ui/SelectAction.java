package xyz.cofe.game.tank.ui;

import xyz.cofe.game.tank.unt.Figure;
import xyz.cofe.game.tank.unt.Scene;

import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.Set;

/**
 * Действие в выбранными объектами
 */
public interface SelectAction {
    /**
     * Картинка/изображение действия (иконка)
     * @return иконка
     */
    default Optional<BufferedImage> image() { return Optional.empty(); }

    /**
     * Название действия
     * @return название
     */
    default String name(){ return this.getClass().getSimpleName(); }

    /**
     * Выполнение действия над объектами
     * @param scene сцена
     * @param selection выбранные объекты
     */
    void execute(Scene scene, Set<Figure<?>> selection);
}
