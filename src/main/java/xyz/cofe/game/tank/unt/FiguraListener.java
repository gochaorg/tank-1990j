package xyz.cofe.game.tank.unt;

/**
 * Подписчик на события фигуры
 * @param <SELF> фигура
 */
public interface FiguraListener<SELF extends Figure<SELF>> {
    /**
     * Уведомление о событии фигуры
     * @param event событие
     */
    void figuraEvent(FiguraEvent<SELF> event);
}
