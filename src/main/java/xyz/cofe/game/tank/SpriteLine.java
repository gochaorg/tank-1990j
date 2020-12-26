package xyz.cofe.game.tank;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * "Раскадровка" - последовательность спрайтов
 */
public class SpriteLine implements Drawing {
    public SpriteLine(){
    }

    public SpriteLine(Sprite sprite){
        if( sprite==null )throw new IllegalArgumentException( "sprite==null" );
        sprites.add(sprite);
    }

    public SpriteLine(Iterable<Sprite> sprite, double duration){
        if( sprite==null )throw new IllegalArgumentException( "sprite==null" );
        sprite.forEach( sprites::add );
        this.duration = duration;
    }

    public SpriteLine configure(Consumer<SpriteLine> conf){
        if( conf==null )throw new IllegalArgumentException( "conf==null" );
        conf.accept(this);
        return this;
    }

    //region sprites : List<Sprite>
    private final List<Sprite> sprites = new ArrayList<>();
    public List<Sprite> getSprites(){ return Collections.unmodifiableList(sprites); }
    public void setSprites(List<Sprite> sprites){
        this.sprites.clear();
        if( sprites!=null ){
            this.sprites.addAll(sprites);
        }
    }
    public void setSprites(Iterable<Sprite> sprites){
        this.sprites.clear();
        if( sprites!=null ){
            sprites.forEach(this.sprites::add);
        }
    }
    public void setSprites(Stream<Sprite> sprites){
        this.sprites.clear();
        if( sprites!=null ){
            sprites.forEach(this.sprites::add);
        }
    }
    //endregion
    //region duration : double
    private double duration;
    public double getDuration(){ return duration; }
    public void setDuration(double duration){ this.duration = duration; }
    //endregion
    //region started : long
    private long started = 0;
    public long getStarted(){ return started; }
    protected void setStarted(long started){ this.started = started; }
    //endregion
    //region stopped : long
    private long stopped = 0;
    public long getStopped(){ return stopped; }
    protected void setStopped(long stopped){ this.stopped = stopped; }
    //endregion
    //region loop : boolean
    private boolean loop = true;
    public boolean isLoop(){ return loop; }
    public void setLoop(boolean value){ this.loop = value; }
    //endregion

    /**
     * В режиме анимации
     * @return true - режим анимации, присуствует смена кадров
     */
    public boolean isRunning(){
        if( started==0 )return false;
        return stopped == 0;
    }

    /**
     * Запуск анимации
     */
    public void start(){
        stopped = 0;
        started = System.nanoTime();
    }

    /**
     * Остановка анимации
     */
    public void stop(){
        stopped = System.nanoTime();
    }

    @Override
    public void draw(Graphics2D gs, int x, int y){
        if( gs==null )throw new IllegalArgumentException( "gs==null" );

        int spriteCount = sprites.size();
        if( spriteCount<1 )return;
        if( spriteCount==1 || started==0 ){
            Sprite s = sprites.get(0);
            if( s==null )return;

            s.draw(gs,x,y);
            return;
        }

        long tnow = System.nanoTime();
        if( stopped>0 ){
            tnow = stopped;
        }

        long tdiff = Math.abs(tnow - started);
        long sec = 1_000_000_000;
        long frameDuration = (long)(sec * duration);
        int frame = frameDuration>0 ? ((int)( tdiff / frameDuration )) : 0;

        if( frame>=spriteCount ){
            frame = loop ? frame % spriteCount : spriteCount-1;
        }

        if( !loop && frame==(spriteCount-1) ){
            stopped = System.nanoTime();
        }

        Sprite s = sprites.get(frame);
        if( s==null )return;

        //System.out.println("frameDuration="+frameDuration+" frame="+frame);

        s.draw(gs,x,y);
    }
}
