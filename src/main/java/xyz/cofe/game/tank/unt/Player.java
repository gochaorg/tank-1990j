package xyz.cofe.game.tank.unt;

import xyz.cofe.game.tank.*;
import xyz.cofe.game.tank.sprite.SpriteLine;
import xyz.cofe.iter.Eterable;

import java.awt.Graphics2D;
import java.util.Map;

/**
 * Некий игрок
 * @param <SELF> дочерний класс
 */
public abstract class Player<SELF extends Player<SELF>> extends AbstractGameUnit<SELF> implements GameUnit<SELF>, Directed<SELF> {
    public Player(){}
    public Player(Figure<?> sample){
        super(sample);
        if( sample instanceof Player ){
            playerState = ((Player<?>)sample).getPlayerState();
        }
        if( sample instanceof Directed ){
            direction = ((Directed<?>)sample).direction();
        }
        job = null;
    }
    public Player(Player<?> sample){
        super(sample);
        playerState = sample.playerState;
        direction = sample.direction;
        job = null;
    }
    public abstract SELF clone();

    //region playerState : PlayerState
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
    //region direction : Direction
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

    {
        moving.started().listen( ev -> {
            direction(ev.event.getDirection());
        });
    }

    /**
     * Перемещение объекта
     * @param direction направление движения
     * @param speedPixelPerSec скорость - кол-во пикселей в секунду
     */
    public void move(Direction direction, double speedPixelPerSec){
        if( direction==null )throw new IllegalArgumentException( "direction==null" );
        //direction(direction);
        job = moving.direction(direction).speed(speedPixelPerSec).start();
    }

    public Bullet createBullet(){
        var blt = new Bullet().direction(direction());

        var myXCenter = width() / 2 + left();
        var myYCenter = height() / 2 + top();
        var indent = 2;

        switch( direction() ){
            case UP:
                blt.location( myXCenter-blt.width()/2, myYCenter-height()/2-blt.height()-indent );
                break;
            case DOWN:
                blt.location( myXCenter-blt.width()/2, myYCenter+height()/2+blt.height()*0+indent );
                break;
            case LEFT:
                blt.location( myXCenter-width()/2-blt.width()-indent, myYCenter-blt.height()/2 );
                break;
            case RIGHT:
                blt.location( myXCenter+width()/2+blt.width()*0+indent, myYCenter-blt.height()/2 );
                break;
        }

        return blt;
    }
}
