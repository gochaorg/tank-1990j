package xyz.cofe.game.tank.job;

import java.util.function.Consumer;
import xyz.cofe.game.tank.GameUnit;
import xyz.cofe.game.tank.geom.Rect;

/**
 * Предварительный расчет передвижения объекта
 */
public class UnitMoveEstimation<UNT extends GameUnit<UNT>> {
    /**
     * Конструктор
     * @param moving задание передвижения
     * @param to Куда будет передвинут объект в текущий игровой цикл
     * @param nextRunTime Время следующего передвижения
     * @param appling Функция перемещения - непосредственное перемещение
     */
    public UnitMoveEstimation(Moving<UNT> moving, Rect to, long nextRunTime, Consumer<UnitMoveEstimation<UNT>> appling){
        if( moving == null ) throw new IllegalArgumentException("moving==null");
        if( to == null ) throw new IllegalArgumentException("to==null");
        if( appling == null ) throw new IllegalArgumentException("appling==null");

        this.moving = moving;
        this.to = to;

        var gu = moving.getGameUnit();
        if( gu == null ) throw new IllegalArgumentException("moving.getGameUnit() == null");

        from = gu;
        scn = moving.getScn();
        this.nextRunTime = nextRunTime;
        this.appling = appling;
    }

    //region nextRun
    protected long nextRunTime;

    /**
     * Время следующего передвижения
     * @return Время следующего передвижения
     */
    public long getNextRun(){
        return nextRunTime;
    }
    //endregion
    //region moving
    protected Moving<UNT> moving;

    /**
     * Возвращает задание передвижения
     * @return задание передвижения
     */
    public Moving<UNT> getMoving(){
        return moving;
    }
    //endregion
    //region scn
    protected long scn;

    public long getScn(){
        return scn;
    }
    //endregion
    //region from : Rect
    protected Rect from;

    public Rect getFrom(){
        return from;
    }
    //endregion
    //region to : Rect
    protected Rect to;

    public Rect getTo(){
        return to;
    }
    //endregion
    //region bounds : Rect
    protected Rect bounds;
    public Rect getBounds(){
        if( bounds!=null )return bounds;
        bounds = from.bounds(to);
        return bounds;
    }
    //endregion
    //region appling
    protected Consumer<UnitMoveEstimation<UNT>> appling;

    public void apply(){
        appling.accept(this);
    }
    //endregion
}
