package xyz.cofe.game.tank;

import xyz.cofe.game.tank.geom.Rect;

/**
 * Размещение объекта
 */
public interface Moveable<SELF extends Moveable<SELF>> extends Rect {
    /**
     * Указание левого верхнего угла объекта
     * @param left левый край объекта
     * @param top верхний край объекта
     * @return self ссылка
     */
    public SELF location(double left, double top );
}
