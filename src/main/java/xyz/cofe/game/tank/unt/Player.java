package xyz.cofe.game.tank.unt;

import xyz.cofe.fn.Consumer1;
import xyz.cofe.game.tank.*;
import xyz.cofe.iter.Eterable;

import java.awt.Graphics2D;
import java.util.Map;

/**
 * Некий игрок
 * @param <SELF> дочерний класс
 */
public abstract class Player<SELF extends Player<SELF>> extends Figura<SELF> implements GameUnit<SELF> {
    //region playerState
    protected PlayerState playerState = PlayerState.Level0;
    public PlayerState getPlayerState(){
        return playerState;
    }
    public void setPlayerState(PlayerState playerState){
        if( playerState ==null )throw new IllegalArgumentException( "state==null" );
        currentSpriteLine().stopAnimation();
        this.playerState = playerState;
        currentSpriteLine().startAnimation();
    }
    //endregion
    //region direction
    protected Direction direction = Direction.RIGHT;
    public Direction direction(){
        return direction;
    }
    @SuppressWarnings({"unchecked", "UnusedReturnValue"})
    public SELF direction(Direction direction){
        if( direction==null )throw new IllegalArgumentException( "direction==null" );
        currentSpriteLine().stopAnimation();
        this.direction = direction;
        currentSpriteLine().startAnimation();
        return (SELF) this;
    }
    //endregion
    //region render player
    protected abstract Map<PlayerState, Map<Direction, SpriteLine>> sprites();

    public SpriteLine currentSpriteLine(){
        return sprites().get(getPlayerState()).get(direction());
    }

    protected Eterable<SpriteLine> spriteLines(){
        Iterable<Iterable<SpriteLine>> x = Eterable.of(sprites().values()).map(m -> (Iterable<SpriteLine>)m.values() ).toList();
        return Eterable.<SpriteLine>empty().union(x);
    }

    @Override
    public void draw(Graphics2D gs){
        if( gs==null )throw new IllegalArgumentException( "gs==null" );
        currentSpriteLine().draw(gs,left(),top());
    }

    @Override
    public double width(){
        return currentSpriteLine().maxSize().width();
    }

    @Override
    public double height(){
        return currentSpriteLine().maxSize().height();
    }

    @Override
    public boolean isAnimationRunning(){
        return currentSpriteLine().isAnimationRunning();
    }

    @SuppressWarnings("unchecked")
    @Override
    public SELF startAnimation(){
        currentSpriteLine().startAnimation();
        return (SELF) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public SELF stopAnimation(){
        spriteLines().forEach( SpriteLine::stopAnimation );
        return (SELF) this;
    }
    //endregion

    //region run()
    protected Runnable job;

    @Override
    public void run(){
        Runnable r = job;
        if( r!=null ){
            r.run();
        }
    }
    //endregion

    /**
     * Остановка танка
     */
    public void stop(){
        stopAnimation();
        job = null;
    }

    //region collision - Список объектов возможных коллизий
    protected Iterable<? extends Figura<?>> collision;
    public Iterable<? extends Figura<?>> collision(){ return collision; }
    @SuppressWarnings({"unchecked", "SpellCheckingInspection", "UnusedReturnValue"})
    public SELF collision(Iterable<? extends Figura<?>> collizion){
        this.collision = collizion;
        return (SELF) this;
    }
    //endregion

    //region Переммещение
    /**
     * Время начала движения, момент вызова move() - System.currentTimeMillis()
     */
    protected long moveStartedTime;

    /**
     * Следущее время когда необходимо передвинуть объект - System.currentTimeMillis()
     */
    protected long moveNextTime;

    /**
     * Через какой промежуток времени необходимо передвинуть момент (moveNextTime = moveNextTime + moveDuration)
     */
    protected long moveDuration;
    protected double moveStartedX;
    protected double moveStartedY;
    protected double moveStartedSpeedPixelPerSec;

    /**
     * Смещение на которое необходимо передвинуть объект, спустя заданное время {@link #moveNextTime}, {@link #moveDuration}
     */
    protected double moveOffset;
    protected Consumer1<Moveable<?>> movingFn;

    /**
     * Перемещение объекта
     * @param direction направление движения
     * @param speedPixelPerSec скорость - кол-во пикселей в секунду
     */
    public void move(Direction direction, double speedPixelPerSec){
        if( direction==null )throw new IllegalArgumentException( "direction==null" );
        direction(direction);
        moveStartedTime = System.currentTimeMillis();
        moveStartedX = left();
        moveStartedY = top();
        moveStartedSpeedPixelPerSec = speedPixelPerSec;

        // Движение не чаще 40 мс
        moveDuration = 40;
        moveOffset = speedPixelPerSec * 0.001 * ((double)moveDuration);
        moveNextTime = moveStartedTime + moveDuration;
        switch( direction ){
            case UP:
                movingFn = m -> m.location( m.left(), m.top()-moveOffset ); break;
            case DOWN:
                movingFn = m -> m.location( m.left(), m.top()+moveOffset ); break;
            case LEFT:
                movingFn = m -> m.location( m.left()-moveOffset, m.top() ); break;
            case RIGHT:
                movingFn = m -> m.location( m.left()+moveOffset, m.top() ); break;
        }

        job = () -> {
            long now = System.currentTimeMillis();
            if( moveNextTime>now )return;
            moveNextTime = now + moveDuration;

            if( movingFn!=null ){
                if( collision!=null ){
                    var target = new MutableRect(this);
                    movingFn.accept(target);

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
                        movingFn.accept(this);
                    }
                }else{
                    movingFn.accept(this);
                }
            }
        };
    }

    /**
     * Произошла коллизия
     * @param collision где произошла коллизия
     * @param withObject с кем произошла коллизия
     */
    protected void collision( Rect collision, Moveable<?> withObject ){
        System.out.println("collision detect at "+collision+" with "+withObject);
        stop();
    }
    //endregion

    public Bullet createBullet(){
        var blt = new Bullet().direction(direction());

        var myXCenter = width() / 2 + left();
        var myYCenter = height() / 2 + top();

        switch( direction() ){
            case UP:
                blt.location( myXCenter-blt.width()/2, myYCenter-height()/2-blt.height() );
                break;
            case DOWN:
                blt.location( myXCenter-blt.width()/2, myYCenter+height()/2+blt.height()*0 );
                break;
            case LEFT:
                blt.location( myXCenter-width()/2-blt.width(), myYCenter-blt.height()/2 );
                break;
            case RIGHT:
                blt.location( myXCenter+width()/2+blt.width()*0, myYCenter-blt.height()/2 );
                break;
        }

        return blt;
    }
}
