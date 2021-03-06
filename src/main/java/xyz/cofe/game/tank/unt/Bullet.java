package xyz.cofe.game.tank.unt;

import xyz.cofe.game.tank.geom.MutableRect;
import xyz.cofe.game.tank.geom.Rect;
import xyz.cofe.game.tank.sprite.SpriteLine;
import xyz.cofe.game.tank.sprite.SpritesData;

import java.awt.Graphics2D;
import java.util.Map;

/**
 * Пуля
 */
public class Bullet extends AbstractGameUnit<Bullet> implements Directed<Bullet> {
    public Bullet(){
    }

    /**
     * Конструктор копирования
     * @param sample образец
     */
    public Bullet(Figure<?> sample){
        super(sample);
        if( sample instanceof Directed ){
            direction = ((Directed<?>)sample).direction();
        }
    }

    /**
     * Конструктор копирования
     * @param sample образец
     */
    public Bullet(Bullet sample){
        super(sample);
        direction = sample.direction;
    }

    /**
     * Клонирование объекта
     * @return клон
     */
    public Bullet clone(){ return new Bullet(this); }

    private static final int UP_FRAME = 0;
    private static final int DOWN_FRAME = 2;
    private static final int LEFT_FRAME = 3;
    private static final int RIGHT_FRAME = 1;
    private static int frameByDirection(Direction direction){
        switch( direction ){
            case UP: return UP_FRAME;
            case DOWN: return DOWN_FRAME;
            case LEFT: return LEFT_FRAME;
            case RIGHT: return RIGHT_FRAME;
        }
        return UP_FRAME;
    }
    private static SpriteLine spritesStatic;
    private static final Map<Direction, Rect> frameBounds;
    static {
        spritesStatic = SpritesData.bullet.toSpriteLine();
        spritesStatic.stopAnimation();
        frameBounds = Map.of(
            Direction.UP, spritesStatic.sprite(frameByDirection(Direction.UP)).bounds(),
            Direction.DOWN, spritesStatic.sprite(frameByDirection(Direction.DOWN)).bounds(),
            Direction.LEFT, spritesStatic.sprite(frameByDirection(Direction.LEFT)).bounds(),
            Direction.RIGHT, spritesStatic.sprite(frameByDirection(Direction.RIGHT)).bounds()
        );
    }

    private SpriteLine sprites;
    { sprites = spritesStatic.clone(); }

    //region draw()
    @Override
    public void draw(Graphics2D gs){
        if( gs==null )throw new IllegalArgumentException( "gs==null" );
        sprites.draw(gs,left(),top());
    }

    @Override
    public double width(){
        return sprites.maxSize().width();
    }

    @Override
    public double height(){
        return sprites.maxSize().height();
    }
    //endregion
    //region direction
    protected Direction direction = Direction.RIGHT;
    public Direction direction(){
        return direction;
    }
    @SuppressWarnings("UnusedReturnValue")
    public Bullet direction(Direction direction){
        if( direction==null )throw new IllegalArgumentException( "direction==null" );
        this.direction = direction;
        sprites.frame(frameByDirection(this.direction));
        return this;
    }
    //endregion

    public Rect bounds(){
        MutableRect mrect = new MutableRect(frameBounds.get(direction()));
        mrect.location( mrect.left()+left(), mrect.top()+top() );
        return mrect;
    }

    //region animation
    @Override
    public boolean isAnimationRunning() {
        return false;
    }

    @Override
    public Bullet startAnimation() {
        return this;
    }

    @Override
    public Bullet stopAnimation() {
        return this;
    }
    //endregion

    {
        moving.started().listen( ev -> {
            direction(ev.event.getDirection());
        });
    }

    @Override
    public String toString(){
        return this.getClass().getSimpleName()+"{ x0="+left()+" y0="+top()+" x1="+right()+" y1="+bottom()+" w="+width()+" h="+height()+"}";
    }
}
