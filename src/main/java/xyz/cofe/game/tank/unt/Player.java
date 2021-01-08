package xyz.cofe.game.tank.unt;

import xyz.cofe.game.tank.*;
import xyz.cofe.game.tank.job.Job;
import xyz.cofe.game.tank.job.Moving;
import xyz.cofe.game.tank.sprite.SpriteLine;
import xyz.cofe.iter.Eterable;

import java.awt.Graphics2D;
import java.util.Map;

/**
 * Некий игрок
 * @param <SELF> дочерний класс
 */
public abstract class Player<SELF extends Player<SELF>> extends Figura<SELF> implements GameUnit<SELF>, Directed<SELF> {
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
    protected Job<?> job;

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
        if( job!=null ){
            job.stop();
        }
        job = null;
    }

    protected final Moving<SELF> moving = new Moving<SELF>((SELF) this)
        .onStopped( ev -> {
            System.out.println("stopped event");
            stop();
        })
        .onCollision( ev -> {
            System.out.println("collision "+ev.getRect()+" with "+ev.getWithFigura());
        });

    public Player<SELF> collision(Iterable<? extends Figura<?>> collizion){
        moving.collision(collizion);
        return this;
    }

    //region Переммещение
    /**
     * Перемещение объекта
     * @param direction направление движения
     * @param speedPixelPerSec скорость - кол-во пикселей в секунду
     */
    public void move(Direction direction, double speedPixelPerSec){
        if( direction==null )throw new IllegalArgumentException( "direction==null" );
        direction(direction);

        job = moving.direction(direction).speed(speedPixelPerSec).start();
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
