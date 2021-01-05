package xyz.cofe.game.tank.job;

import xyz.cofe.ecolls.ListenersHelper;
import xyz.cofe.game.tank.GameUnit;
import xyz.cofe.game.tank.Moveable;
import xyz.cofe.game.tank.geom.MutableRect;
import xyz.cofe.game.tank.geom.Rect;
import xyz.cofe.game.tank.unt.Directed;
import xyz.cofe.game.tank.unt.Direction;
import xyz.cofe.game.tank.unt.Figura;

import java.util.Set;

/**
 * Задача перемещения игрового объекта
 */
public class Moving extends AbstractJob<Moving> {
    public Moving(GameUnit<?> gameUnit){
        if( gameUnit==null )throw new IllegalArgumentException( "gameUnit==null" );
        this.gameUnit = gameUnit;
    }

    private GameUnit<?> gameUnit;
    public GameUnit<?> gameUnit(){ return gameUnit; }

    //region speed - Скорость - кол-во пикселей в секунду
    /**
     * Скорость - кол-во пикселей в секунду
     */
    protected double speed = 10;

    public double getSpeed(){ return speed; }

    public void setSpeed(double speed){
        if( isRunning() )throw new AlreadyRunnedError();
        this.speed = speed;
    }

    public Moving speed(double speed){
        setSpeed(speed);
        return this;
    }
    //endregion

    //region offset - Смещение на которое необходимо передвинуть объект, спустя заданное время
    /**
     * Смещение на которое необходимо передвинуть объект, спустя заданное время {@link #runNextTime}, {@link #duration}
     */
    protected double offset;

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
    public Moving direction(Direction direction){
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

    //region collision
    protected Iterable<? extends Figura<?>> collision;
    public Iterable<? extends Figura<?>> collision(){ return collision; }
    @SuppressWarnings({"SpellCheckingInspection", "UnusedReturnValue"})
    public Moving collision(Iterable<? extends Figura<?>> collizion){
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

    @Override
    public Moving start(){
        if( gameUnit==null )throw new IllegalStateException("gameUnit==null");
        if( isRunning() )throw new AlreadyRunnedError();

        this.startedTime = System.currentTimeMillis();
        this.stoppedTime = 0;

        if( gameUnit instanceof Directed ){
            //noinspection rawtypes
            ((Directed)gameUnit).direction(direction);
        }

        duration = 40;
        offset = speed * 0.001 * ((double) duration);
        runNextTime = startedTime + duration;

        fireStarted();
        return this;
    }

    @Override
    public Moving stop(){
        this.stoppedTime = System.currentTimeMillis();
        fireStopped();
        return this;
    }

    @Override
    public void run(){
        if( !isRunning() ){
            return;
        }

        var gu = gameUnit();
        if( gu==null ){
            stop();
            return;
        }

        long now = System.currentTimeMillis();
        if( runNextTime >now )return;

        runNextTime = now + duration;

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
                fireExecuted();
            }
        }else{
            move(gu);
            fireExecuted();
        }
    }

    /**
     * Произошла коллизия
     * @param collision где произошла коллизия
     * @param withObject с кем произошла коллизия
     */
    protected void collision(Rect collision, Moveable<?> withObject ){
        System.out.println("collision detect at "+collision+" with "+withObject);
        stop();
    }
}
