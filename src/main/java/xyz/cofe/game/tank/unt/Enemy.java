package xyz.cofe.game.tank.unt;

import xyz.cofe.game.tank.GameUnit;
import xyz.cofe.game.tank.sprite.SpriteLine;
import xyz.cofe.game.tank.sprite.SpritesData;
import xyz.cofe.gui.swing.bean.UiBean;
import xyz.cofe.iter.Eterable;

import java.awt.Graphics2D;
import java.util.Map;

/**
 * Вражеский танк
 */
public class Enemy extends AbstractGameUnit<Enemy> implements GameUnit<Enemy>, Directed<Enemy> {
    /**
     * Конструктор
     */
    public Enemy(){}

    /**
     * Конструктор копирования
     * @param sample образец
     */
    public Enemy(Figure<?> sample){
        super(sample);
        if( sample instanceof Enemy){
            enemyState = ((Enemy)sample).getEnemyState();
        }
        if( sample instanceof Directed ){
            direction = ((Directed<?>)sample).direction();
        }
        job = null;
    }

    /**
     * Конструктор копирования
     * @param sample образец
     */
    public Enemy(Enemy sample){
        super(sample);
        enemyState = sample.enemyState;
        direction = sample.direction;
        job = null;
    }

    /**
     * Клонирование объекта
     * @return клон
     */
    public Enemy clone(){
        return new Enemy(this);
    }

    //region enemyState : EnemyState
    protected EnemyState enemyState = EnemyState.Slow;
    public EnemyState getEnemyState(){
        return enemyState;
    }
    public void setEnemyState(EnemyState enemyState){
        if( enemyState ==null )throw new IllegalArgumentException( "state==null" );
        currentSpriteLine().stopAnimation();
        this.enemyState = enemyState;
        currentSpriteLine().startAnimation();
    }
    //endregion
    //region direction : Direction
    protected Direction direction = Direction.RIGHT;
    public Direction direction(){
        return direction;
    }
    @SuppressWarnings({"UnusedReturnValue"})
    public Enemy direction(Direction direction){
        if( direction==null )throw new IllegalArgumentException( "direction==null" );
        currentSpriteLine().stopAnimation();
        this.direction = direction;
        currentSpriteLine().startAnimation();
        return this;
    }
    //endregion
    //region render player
    private static final Map<EnemyState,Map<Direction, SpriteLine>> sprites =
        Map.of(
            EnemyState.Slow,
            Map.of(
                Direction.LEFT, SpritesData.enemy_slow_left.toSpriteLine(),
                Direction.RIGHT, SpritesData.enemy_slow_right.toSpriteLine(),
                Direction.UP, SpritesData.enemy_slow_up.toSpriteLine(),
                Direction.DOWN, SpritesData.enemy_slow_down.toSpriteLine()
            ),
            EnemyState.SlowBonus,
            Map.of(
                Direction.LEFT, SpritesData.enemy_slow_bonus_left.toSpriteLine(),
                Direction.RIGHT, SpritesData.enemy_slow_bonus_right.toSpriteLine(),
                Direction.UP, SpritesData.enemy_slow_bonus_up.toSpriteLine(),
                Direction.DOWN, SpritesData.enemy_slow_bonus_down.toSpriteLine()
            ),
            EnemyState.Fast,
            Map.of(
                Direction.LEFT, SpritesData.enemy_fast_left.toSpriteLine(),
                Direction.RIGHT, SpritesData.enemy_fast_right.toSpriteLine(),
                Direction.UP, SpritesData.enemy_fast_up.toSpriteLine(),
                Direction.DOWN, SpritesData.enemy_fast_down.toSpriteLine()
            ),
            EnemyState.FastBonus,
            Map.of(
                Direction.LEFT, SpritesData.enemy_fast_bonus_left.toSpriteLine(),
                Direction.RIGHT, SpritesData.enemy_fast_bonus_right.toSpriteLine(),
                Direction.UP, SpritesData.enemy_fast_bonus_up.toSpriteLine(),
                Direction.DOWN, SpritesData.enemy_fast_bonus_down.toSpriteLine()
            ),
            EnemyState.Medium,
            Map.of(
                Direction.LEFT, SpritesData.enemy_medium_left.toSpriteLine(),
                Direction.RIGHT, SpritesData.enemy_medium_right.toSpriteLine(),
                Direction.UP, SpritesData.enemy_medium_up.toSpriteLine(),
                Direction.DOWN, SpritesData.enemy_medium_down.toSpriteLine()
            ),
            EnemyState.MediumBonus,
            Map.of(
                Direction.LEFT, SpritesData.enemy_medium_bonus_left.toSpriteLine(),
                Direction.RIGHT, SpritesData.enemy_medium_bonus_right.toSpriteLine(),
                Direction.UP, SpritesData.enemy_medium_bonus_up.toSpriteLine(),
                Direction.DOWN, SpritesData.enemy_medium_bonus_down.toSpriteLine()
            ),
            EnemyState.Big,
            Map.of(
                Direction.LEFT, SpritesData.enemy_big_left.toSpriteLine(),
                Direction.RIGHT, SpritesData.enemy_big_right.toSpriteLine(),
                Direction.UP, SpritesData.enemy_big_up.toSpriteLine(),
                Direction.DOWN, SpritesData.enemy_big_down.toSpriteLine()
            ),
            EnemyState.BigBonus,
            Map.of(
                Direction.LEFT, SpritesData.enemy_big_bonus_left.toSpriteLine(),
                Direction.RIGHT, SpritesData.enemy_big_bonus_right.toSpriteLine(),
                Direction.UP, SpritesData.enemy_big_bonus_up.toSpriteLine(),
                Direction.DOWN, SpritesData.enemy_big_bonus_down.toSpriteLine()
            )
        );

    protected Map<EnemyState,Map<Direction,SpriteLine>> sprites(){ return sprites; }

    public SpriteLine currentSpriteLine(){
        return sprites().get(getEnemyState()).get(direction());
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

    @Override
    public Enemy startAnimation(){
        currentSpriteLine().startAnimation();
        return this;
    }

    @Override
    public Enemy stopAnimation(){
        spriteLines().forEach( SpriteLine::stopAnimation );
        return this;
    }
    //endregion

    {
        moving.started().listen( ev -> direction(ev.event.getDirection()));
    }

    /**
     * Установка задания - перемещение объекта
     * @param direction направление движения
     * @param speedPixelPerSec скорость - кол-во пикселей в секунду
     */
    @SuppressWarnings("unused")
    public void move( Direction direction, double speedPixelPerSec){
        if( direction==null )throw new IllegalArgumentException( "direction==null" );
        //direction(direction);
        job = moving.direction(direction).speed(speedPixelPerSec).start();
    }

    @SuppressWarnings("unused")
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
