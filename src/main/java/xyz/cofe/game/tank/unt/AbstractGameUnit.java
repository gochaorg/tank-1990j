package xyz.cofe.game.tank.unt;

import xyz.cofe.game.tank.Animated;
import xyz.cofe.game.tank.Drawing;
import xyz.cofe.game.tank.GameUnit;
import xyz.cofe.game.tank.Moveable;
import xyz.cofe.game.tank.geom.Rect;
import xyz.cofe.game.tank.job.Job;
import xyz.cofe.game.tank.job.Moving;

/**
 * Абстрактная игровая фигура (пуля или танк), данной фигуре можно назначить задание
 * @param <SELF> Собственный тип
 */
public abstract class AbstractGameUnit<SELF extends AbstractGameUnit<SELF>>
    extends Figure<SELF>
    implements Runnable, Rect, Moveable<SELF>, Drawing, Animated<SELF>, GameUnit<SELF>
{
    /**
     * Конструктор по умолчанию
     */
    public AbstractGameUnit() {
    }

    /**
     * Конструктор копирования
     * @param sample образец для копирования
     */
    public AbstractGameUnit(Figure<?> sample) {
        super(sample);
    }

    /**
     * Клонирование объекта
     * @return клон
     */
    public abstract SELF clone();

    /**
     * Текущее задание
     */
    protected Job<?> job;

    /**
     * Возвращает текущее задание
     * @return текущее задание или null
     */
    public Job<?> getJob(){
        return job;
    }

    /**
     * Указывает текущее задание
     * @param newJob задание
     */
    public void setJob(Job<?> newJob){
        this.job = newJob;
    }

    /**
     * Выполняет текущее задание
     */
    public void run(){
        Runnable r = job;
        if( r!=null ){
            r.run();
        }
    }

    /**
     * Остановка игровой фигуры (танк/пуля)
     */
    public void stop(){
        stopAnimation();
        if( job!=null ){
            job.stop();
        }
        job = null;
    }

    @SuppressWarnings("unchecked")
    public final Moving<SELF> moving = new Moving<SELF>((SELF) this);
}
