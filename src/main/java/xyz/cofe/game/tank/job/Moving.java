package xyz.cofe.game.tank.job;

import java.util.Optional;

import xyz.cofe.game.tank.GameUnit;
import xyz.cofe.game.tank.Moveable;
import xyz.cofe.game.tank.geom.MutableRect;
import xyz.cofe.game.tank.unt.Directed;
import xyz.cofe.game.tank.unt.Direction;

import java.util.function.Consumer;

/**
 * Задача перемещения игрового объекта
 */
public class Moving<UNT extends GameUnit<UNT>> extends AbstractJob<Moving<UNT>> {
    /**
     * Создание задачи передвижения игрового объекта
     * @param gameUnit игровой объект
     */
    public Moving(UNT gameUnit){
        if( gameUnit==null )throw new IllegalArgumentException( "gameUnit==null" );
        this.gameUnit = gameUnit;
    }

    public Moving<UNT> configure(Consumer<Moving<UNT>> conf){
        if( conf==null )throw new IllegalArgumentException( "conf==null" );
        conf.accept(this);
        return this;
    }

    public static <A extends GameUnit<A>> Moving<A> create(A unit){
        if( unit==null )throw new IllegalArgumentException( "unit==null" );
        return new Moving<>(unit);
    }

    //region gameUnit - игровой объект
    protected UNT gameUnit;

    /**
     * Возвращает игровой объект
     * @return Игровой объект
     */
    public UNT getGameUnit(){ return gameUnit; }
    //endregion

    //region speed - Скорость - кол-во пикселей в секунду
    /**
     * Скорость - кол-во пикселей в секунду
     */
    protected double speed = 10;

    /**
     * Возвращает скорость - кол-во пикселей в секунду
     * @return скорость
     */
    public double getSpeed(){ return speed; }

    /**
     * Указывает скорость - кол-во пикселей в секунду
     * @param speed скорость
     */
    public void setSpeed(double speed){
        if( isRunning() )throw new AlreadyRunnedError();
        this.speed = speed;
    }

    /**
     * Указывает скорость - кол-во пикселей в секунду
     * @param speed скорость
     * @return SELF ссылка
     */
    public Moving<UNT> speed(double speed){
        setSpeed(speed);
        return this;
    }
    //endregion
    //region offset - Смещение на которое необходимо передвинуть объект, спустя заданное время
    /**
     * Смещение на которое необходимо передвинуть объект, спустя заданное время {@link #nextRunTime}, {@link #duration}
     */
    protected double offset;

    /**
     * Смещение на которое необходимо передвинуть объект, спустя заданное время {@link #nextRunTime}, {@link #duration}
     * @return Смещение на которое необходимо передвинуть объект
     */
    public double getOffset(){ return offset; }
    //endregion
    //region direction - Направление премещения
    /**
     * Направление премещения
     */
    protected Direction direction = Direction.RIGHT;

    /**
     * Указывает направление перемещения
     * @return направление
     */
    public Direction direction(){ return direction; }

    /**
     * Указывает направление перемещения
     * @param direction направление перемещения
     * @return SELF ссылка
     */
    public Moving<UNT> direction(Direction direction){
        setDirection(direction);
        return this;
    }

    /**
     * Указывает направление перемещения
     * @return направление
     */
    public Direction getDirection(){ return direction; }

    /**
     * Указывает направление перемещения
     * @param direction направление перемещения
     */
    public void setDirection(Direction direction){
        if( direction==null )throw new IllegalArgumentException( "direction==null" );
        if( isRunning() )throw new AlreadyRunnedError();
        this.direction = direction;
    }
    //endregion

    //region move()
    /**
     * Функция перемещения
     */
    private void move(Moveable<?> m){
        direction().move(m, offset);
    }
    //endregion

    //region getScn()
    /**
     * текущий номер изменения, используется чтоб не применять повторно {@link UnitMoveEstimation}
     */
    protected long scn = 0;

    /**
     * Возвращает текущий номер изменения, используется чтоб не применять повторно {@link UnitMoveEstimation}
     * @return Номер SCN
     */
    public long getScn(){
        return scn;
    }
    //endregion

    @Override
    protected DoStart doStart(){
        if( gameUnit==null )throw new IllegalStateException("gameUnit==null");

        var dstart = new DoStart(40);
        offset = speed * 0.001 * ((double) dstart.duration);

        if( gameUnit instanceof Directed ){
            //noinspection rawtypes
            ((Directed)gameUnit).direction(direction);
        }

        return dstart;
    }

    @Override
    protected boolean doRun(){
        var gu = getGameUnit();
        if( gu==null ){
            stop();
            return false;
        }

        move(gu);
        return true;
    }

    //region estimation

    /**
     * Расчет дальнейшего перемещения
     * @return дальнейшее перемещение
     */
    public Optional<UnitMoveEstimation<UNT>> estimate(){
        if( !isRunning() )return Optional.empty();

        long now = System.currentTimeMillis();
        if( nextRunTime>now )return Optional.empty();

        long nextRunTime1 = now + getDuration();

        var gu = getGameUnit();
        if( gu==null )return Optional.empty();

        var target = new MutableRect(gu);
        move(target);

        return Optional.of( new UnitMoveEstimation<>(this, target, nextRunTime1, this::apply) );
    }

    private void apply( UnitMoveEstimation<UNT> est ){
        if( est==null )throw new IllegalArgumentException( "est==null" );
        if( scn!=est.getScn() )throw new IllegalStateException("scn not matched");
        if( !isRunning() ){
            return;
        }

        var gu = getGameUnit();
        if( gu==null )throw new IllegalStateException("gameUnit is null");

        scn++;

        gu.location( est.getTo().left(), est.getTo().top() );
        nextRunTime = est.getNextRun();

        executed().fire(this);
    }
    //endregion
}
