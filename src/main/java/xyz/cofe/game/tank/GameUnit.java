package xyz.cofe.game.tank;

import xyz.cofe.game.tank.geom.Rect;
import xyz.cofe.game.tank.job.Job;

/**
 * Игровой объект
 * @param <SELF> тип объекта - класс наследник
 */
public interface GameUnit<SELF extends Moveable<SELF> & Animated<SELF>> extends Drawing, Rect, Moveable<SELF>, Animated<SELF>, Runnable {
    Job<?> getJob();
    void setJob(Job<?> newJob);
}
