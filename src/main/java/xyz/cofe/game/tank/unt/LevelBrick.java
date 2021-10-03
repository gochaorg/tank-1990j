package xyz.cofe.game.tank.unt;

import xyz.cofe.game.tank.Drawing;
import xyz.cofe.game.tank.Moveable;
import xyz.cofe.game.tank.geom.Rect;
import xyz.cofe.game.tank.sprite.Sprite;

import java.awt.*;

/**
 * Строительный блок уровня из комбинации спрайтов 2x2
 */
public abstract class LevelBrick<SELF extends LevelBrick<SELF>> extends Figure<SELF> implements Drawing, Rect, Moveable<SELF> {
    public LevelBrick(){}
    public LevelBrick(Figure<?> sample){
        super(sample);
        if( sample instanceof LevelBrick ){
            this.state = ((LevelBrick<?>) sample).state;
        }
    }
    public LevelBrick(LevelBrick<?> sample){
        super(sample);
        this.state = sample.state;
    }

    public abstract SELF clone();

    /**
     * Спрайт (16x16) изображающий строительный блок
     * @return спрайт
     */
    public abstract Sprite sprite();

    //region state : int (0..15) - Состояние блока - биотовая маска
    /**
     * Состояние блока - биотовая маска:
     * <ul>
     *     <li>0b0001 - Левый верхний спрайт виден</li>
     *     <li>0b0010 - Правый верхний спрайт виден</li>
     *     <li>0b0100 - Левый нижний спрайт виден</li>
     *     <li>0b1000 - Правый нижний спрайт виден</li>
     * </ul>
     */
    protected int state = 15;
    public int state(){ return state; }
    @SuppressWarnings("unchecked")
    public SELF state(int state){
        if( state<0 )throw new IllegalStateException("state<0");
        if( state>15 )throw new IllegalArgumentException( "state>15" );
        this.state = state;
        return (SELF)this;
    }

    public static final int UL_BRICK = 0b0001;
    public static final int UR_BRICK = 0b0010;
    public static final int BL_BRICK = 0b0100;
    public static final int BR_BRICK = 0b1000;
    public boolean isUpLeft(){ return (state & UL_BRICK) == UL_BRICK; }
    public void setUpLeft( boolean set ){
        state = set ? state | UL_BRICK : state & ~UL_BRICK;
    }
    public boolean isUpRight(){ return (state & UR_BRICK) == UR_BRICK; }
    public void setUpRight( boolean set ){
        state = set ? state | UR_BRICK : state & ~UR_BRICK;
    }
    public boolean isBottomLeft(){ return (state & BL_BRICK) == BL_BRICK; }
    public void setBottomLeft( boolean set ){
        state = set ? state | BL_BRICK : state & ~BL_BRICK;
    }
    public boolean isBottomRight(){ return (state & BR_BRICK) == BR_BRICK; }
    public void setBottomRight( boolean set ){
        state = set ? state | BR_BRICK : state & ~BR_BRICK;
    }
    //endregion
    //region width, height
    /**
     * Ширина объекта
     * @return ширина объекта
     */
    @Override
    public double width(){
        return sprite().image().getWidth()
            *
            ((isBottomRight() || isUpRight()) ? 2 : 1 );
    }

    /**
     * Высота объекта
     * @return высота объекта
     */
    @Override
    public double height(){
        return sprite().image().getHeight()
            *
            (( isBottomLeft() || isBottomRight() ) ? 2 : 1 );
    }
    //endregion

    @Override
    public void draw(Graphics2D gs){
        if( gs==null )throw new IllegalArgumentException( "gs==null" );
        if( isUpLeft() ) {
            sprite().draw(gs, left(), top());
        }
        if( isUpRight() ) {
            sprite().draw(gs, left()+sprite().image().getWidth(), top());
        }
        if( isBottomLeft() ) {
            sprite().draw(gs, left(), top()+sprite().image().getHeight());
        }
        if( isBottomRight() ) {
            sprite().draw(gs, left()+sprite().image().getWidth(), top()+sprite().image().getHeight());
        }
    }

    @Override
    public String toString(){
        return this.getClass().getSimpleName()+"{ x0="+left()+" y0="+top()+" x1="+right()+" y1="+bottom()+" w="+width()+" h="+height()+"}";
    }
}
