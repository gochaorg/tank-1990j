package xyz.cofe.game.tank;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * "Раскадровка" - последовательность спрайтов
 */
@SuppressWarnings("UnusedReturnValue")
public class SpriteLine implements PositionalDrawing {
    /**
     * Конструктор по умолчанию
     */
    public SpriteLine(){
    }

    /**
     * Конструктор
     * @param sprite спрайт
     */
    public SpriteLine(Sprite sprite){
        if( sprite==null )throw new IllegalArgumentException( "sprite==null" );
        sprites.add(sprite);
    }

    /**
     * Конструктор
     * @param sprites спрайты
     * @param duration частота смены кадров в секундах
     */
    public SpriteLine(Iterable<Sprite> sprites, double duration){
        if( sprites==null )throw new IllegalArgumentException( "sprites==null" );
        sprites.forEach( this.sprites::add );
        this.duration = duration;
    }

    /**
     * Конструктор копирования
     * @param samples образец для копироавния
     */
    public SpriteLine(SpriteLine samples){
        if( samples==null )throw new IllegalArgumentException( "samples==null" );
        sprites.addAll(samples.sprites);
        duration = samples.getDuration();
        started = samples.getStarted();
        stopped = samples.getStopped();
        loop = samples.isLoop();
    }

    /**
     * Клонирование
     * @return клон
     */
    public SpriteLine clone(){
        return new SpriteLine(this);
    }

    public SpriteLine configure(Consumer<SpriteLine> conf){
        if( conf==null )throw new IllegalArgumentException( "conf==null" );
        conf.accept(this);
        return this;
    }

    //region sprites : List<Sprite> - набор спрайтов
    private final List<Sprite> sprites = new ArrayList<>();

    /**
     * Возвращает набор спрайтов
     * @return набор спрайтов
     */
    public List<Sprite> getSprites(){ return Collections.unmodifiableList(sprites); }

    /**
     * Указывает набор спрайтов
     * @param sprites набор спрайтов
     */
    public void setSprites(List<Sprite> sprites){
        if( sprites==null )throw new IllegalArgumentException( "sprites==null" );
        this.sprites.clear();
        this.sprites.addAll(sprites);
    }

    /**
     * Указывает набор спрайтов
     * @param sprites набор спрайтов
     * @return Self ссылка
     */
    public SpriteLine sprites(Iterable<Sprite> sprites){
        if( sprites==null )throw new IllegalArgumentException( "sprites==null" );
        this.sprites.clear();
        sprites.forEach(this.sprites::add);
        return this;
    }

    /**
     * Указывает набор спрайтов
     * @param sprites набор спрайтов
     * @return Self ссылка
     */
    public SpriteLine sprites(Stream<Sprite> sprites){
        if( sprites==null )throw new IllegalArgumentException( "sprites==null" );
        this.sprites.clear();
        sprites.forEach(this.sprites::add);
        return this;
    }

    /**
     * Указывает набор спрайтов
     * @param images набор спрайтов
     * @return Self ссылка
     */
    public SpriteLine images(Iterable<BufferedImage> images){
        if( images==null )throw new IllegalArgumentException( "images==null" );
        sprites.clear();
        images.forEach( im -> {
            sprites.add(new Sprite(im));
        });
        return this;
    }
    //endregion
    //region duration : double - продолжительность смены кадров
    private double duration;

    /**
     * Возвращает продолжительность смены кадров в долях секунды
     * @return продолжительность смены кадров
     */
    public double getDuration(){ return duration; }

    /**
     * Указывает продолжительность смены кадров в долях секунды
     * @param duration продолжительность смены кадров
     */
    public void setDuration(double duration){ this.duration = duration; }

    /**
     * Указывает продолжительность смены кадров в долях секунды
     * @param duration продолжительность смены кадров
     * @return Self ссылка
     */
    public SpriteLine duration(double duration){
        setDuration(duration);
        return this;
    }
    //endregion
    //region started : long - Время начала анимации в System.nanoTime()
    private long started = 0;
    public long getStarted(){ return started; }
    protected void setStarted(long started){ this.started = started; }
    //endregion
    //region stopped : long - Время завершения анимации в System.nanoTime()
    private long stopped = 0;
    public long getStopped(){ return stopped; }
    protected void setStopped(long stopped){ this.stopped = stopped; }
    //endregion
    //region loop : boolean - циклическая анимация
    private boolean loop = true;
    public boolean isLoop(){ return loop; }
    public void setLoop(boolean value){ this.loop = value; }

    /**
     * Указывает циклическая анимация или нет
     * @param value true - циклическая
     * @return Self ссылка
     */
    public SpriteLine loop(boolean value){
        setLoop(value);
        return this;
    }
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
     * @return Self ссылка
     */
    public SpriteLine start(){
        stopped = 0;
        started = System.nanoTime();
        return this;
    }

    /**
     * Остановка анимации
     * @return Self ссылка
     */
    public SpriteLine stop(){
        stopped = System.nanoTime();
        return this;
    }

    /**
     * Возвращает индекс текущего кадра
     * @return индекс текущего кадра
     */
    public int frame(){
        int spriteCount = sprites.size();
        if( spriteCount<1 )return -1;
        if( spriteCount==1 || started==0 ){
            return 0;
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

        return frame;
    }

    @Override
    public void draw(Graphics2D gs, double x, double y){
        if( gs==null )throw new IllegalArgumentException( "gs==null" );

        int spriteCount = sprites.size();
        int frame = frame();

        if( !loop && frame==(spriteCount-1) ){
            stopped = System.nanoTime();
        }

        Sprite s = sprites.get(frame);
        if( s==null )return;

        s.draw(gs,x,y);
    }
}
