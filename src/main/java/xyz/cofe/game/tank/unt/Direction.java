package xyz.cofe.game.tank.unt;

import xyz.cofe.game.tank.Moveable;

/**
 * Направление
 */
public enum Direction {
    /** Вверх */
    UP,

    /** Вниз */
    DOWN,

    /** Влево */
    LEFT,

    /** Вправо */
    RIGHT;

    /**
     * Перемещение объекта, без учета коллиций и чего либо
     * @param m объект
     * @param offset на сколько передвинуть
     */
    public void move(Moveable m, double offset){
        if( m==null )throw new IllegalArgumentException( "m==null" );
        switch( this ){
            case UP: m.location( m.left(), m.top()- offset); break;
            case DOWN: m.location( m.left(), m.top()+ offset); break;
            case LEFT: m.location( m.left()- offset, m.top() ); break;
            case RIGHT: m.location( m.left()+ offset, m.top() ); break;
        }
    }
}
