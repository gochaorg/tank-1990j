package xyz.cofe.game.tank.unt;

import xyz.cofe.game.tank.Animated;
import xyz.cofe.game.tank.sprite.Sprite;
import xyz.cofe.game.tank.sprite.SpriteLine;

import java.awt.Graphics2D;
import java.util.List;

/**
 * Фигура представляющая спрайт {@link Sprite} или последовательность спрайтов {@link SpriteLine}
 */
public class SpriteFigura extends Figure<SpriteFigura> implements Animated<SpriteFigura> {
    protected SpriteLine sprites;

    public SpriteFigura(Sprite sprite){
        if( sprite==null )throw new IllegalArgumentException( "sprite==null" );
        sprites = new SpriteLine().sprites(List.of(sprite));
    }

    public SpriteFigura(SpriteLine sprite){
        if( sprite==null )throw new IllegalArgumentException( "sprite==null" );
        sprites = sprite;
    }

    public SpriteFigura(SpriteFigura sample){
        if( sample==null )throw new IllegalArgumentException( "sample==null" );
        if( sample.sprites!=null ){
            sprites = sample.sprites.clone();
        }
    }

    public SpriteFigura clone(){
        return new SpriteFigura(this);
    }

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

    @Override
    public boolean isAnimationRunning(){
        return sprites.isAnimationRunning();
    }

    @Override
    public SpriteFigura startAnimation(){
        sprites.startAnimation();
        return this;
    }

    @Override
    public SpriteFigura stopAnimation(){
        sprites.stopAnimation();
        return this;
    }
}
