package xyz.cofe.game.tank.unt;

/**
 * Направленный объект
 * @param <SELF> класс потомок
 */
public interface Directed<SELF extends Directed<SELF>> {
    /**
     * Указывает направление объекта
     * @return направление
     */
    Direction direction();

    /**
     * Указывает направление объекта
     * @param d направление
     * @return SELF ссылка
     */
    SELF direction(Direction d);
}
