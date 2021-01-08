package xyz.cofe.game.tank.job;

import java.util.Optional;
import xyz.cofe.ecolls.Closeables;
import xyz.cofe.game.tank.GameUnit;
import xyz.cofe.game.tank.Moveable;
import xyz.cofe.game.tank.geom.MutableRect;
import xyz.cofe.game.tank.geom.Rect;
import xyz.cofe.game.tank.unt.Directed;
import xyz.cofe.game.tank.unt.Direction;
import xyz.cofe.game.tank.unt.Figura;

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

    //region collision - коллизии
    protected Iterable<? extends Figura<?>> collision;

    /**
     * Возвращает список объектов с которыми возможны коллизии
     * @return список объектов
     */
    public Iterable<? extends Figura<?>> collision(){ return collision; }

    /**
     * Указывает список объектов с которыми возможны коллизии
     * @param collizion список объектов
     * @return SELF ссылка
     */
    @SuppressWarnings({"SpellCheckingInspection", "UnusedReturnValue"})
    public Moving<UNT> collision(Iterable<? extends Figura<?>> collizion){
        this.collision = collizion;
        return this;
    }
    //endregion

    /**
     * Функция перемещения
     */
    private void move(Moveable<?> m){
        direction().move(m, offset);
    }

    protected volatile long scn = 0;
    public long getScn(){
        return scn;
    }

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

        if( collision!=null ){
            var target = new MutableRect(gu);
            move(target);

            int collisionCount = 0;
            for( var crect : collision ){
                if( crect!=null ){
                    var collsn = target.intersection(crect);
                    if( collsn.isPresent() ){
                        collision(collsn.get(), crect);
                        collisionCount++;
                    }
                }
            }

            if( collisionCount<1 ){
                move(gu);
                return true;
            }
        }else{
            move(gu);
            return true;
        }

        return false;
    }

    public Optional<Estimation<UNT>> estimate(){
        if( !isRunning() )return Optional.empty();

        long now = System.currentTimeMillis();
        if( nextRunTime>now )return Optional.empty();

        long nextRunTime1 = now + getDuration();

        var gu = getGameUnit();
        if( gu==null )return Optional.empty();

        var target = new MutableRect(gu);
        move(target);

        return Optional.of( new Estimation<>(this, target, nextRunTime1) );
    }

    /**
     * Предварительный расчет
     */
    public static class Estimation<UNT extends GameUnit<UNT>> {
        public Estimation( Moving<UNT> moving, Rect to, long nextRunTime ){
            if( moving==null )throw new IllegalArgumentException( "moving==null" );
            if( to==null )throw new IllegalArgumentException( "to==null" );

            this.moving = moving;
            this.to = to;

            var gu = moving.getGameUnit();
            if( gu==null )throw new IllegalArgumentException("moving.getGameUnit() == null");

            from = gu;
            scn = moving.getScn();
            this.nextRunTime = nextRunTime;
        }

        protected long nextRunTime;
        public long getNextRun(){ return nextRunTime; }

        protected Moving<UNT> moving;
        public Moving<UNT> getMoving(){ return moving; }

        protected long scn;
        public long getScn(){ return scn; }

        protected Rect from;
        public Rect getFrom(){
            return from;
        }

        protected Rect to;
        public Rect getTo(){
            return to;
        }

        public void apply(){
            moving.apply(this);
        }
    }

    protected synchronized void apply( Estimation<UNT> est ){
        if( est==null )throw new IllegalArgumentException( "est==null" );
        if( scn!=est.getScn() )throw new IllegalStateException("scn not matched");

        var gu = getGameUnit();
        if( gu==null )throw new IllegalStateException("gameUnit is null");

        scn++;

        gu.location( est.getTo().left(), est.getTo().top() );
        nextRunTime = est.getNextRun();
    }

    /**
     * Произошла коллизия
     * @param collision где произошла коллизия
     * @param withObject с кем произошла коллизия
     */
    protected void collision(Rect collision, Figura<?> withObject ){
        stop();
        fireJobEvent(new CollisionDetected<>(this,withObject,collision));
    }

    public Moving<UNT> onCollision( Consumer<CollisionDetected<UNT>> listener ){
        if( listener==null )throw new IllegalArgumentException( "listener==null" );
        listeners.addListener( e -> {
            if( e instanceof CollisionDetected ){
                listener.accept((CollisionDetected<UNT>)e);
            }
        });
        return this;
    }
    public Moving<UNT> onCollision(Closeables closeables, Consumer<CollisionDetected<UNT>> listener ){
        if( listener==null )throw new IllegalArgumentException( "listener==null" );
        var u = listeners.addListener( e -> {
            if( e instanceof CollisionDetected ){
                listener.accept((CollisionDetected<UNT>)e);
            }
        });
        if( closeables!=null ){
            closeables.add(u);
        }
        return this;
    }
}
