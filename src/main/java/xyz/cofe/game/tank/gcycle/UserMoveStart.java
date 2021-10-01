package xyz.cofe.game.tank.gcycle;

import xyz.cofe.game.tank.unt.Direction;

/**
 * Начало перемещение
 */
public class UserMoveStart extends UserInput {
    /**
     * Начало перемещения
     */
    public final Direction direction;

    public UserMoveStart(Direction direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        return UserMoveStart.class.getSimpleName()+" "+direction;
    }
}
