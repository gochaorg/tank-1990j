package xyz.cofe.game.tank;

import xyz.cofe.game.tank.unt.SpriteFigura;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * "Раскадровка" - последовательность спрайтов
 */
@SuppressWarnings("UnusedReturnValue")
public class SpriteLine implements PositionalDrawing, Animated<SpriteLine> {
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
        int i=-1;
        for(Sprite s:sprites){
            i++;
            if( s==null ){
                throw new IllegalArgumentException("sprites["+i+"]==null");
            }
        }
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
    @SuppressWarnings("MethodDoesntCallSuperMethod")
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

    private void onSpritesChanged(){
        sizes = null;
        maxSize = null;
    }

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
        int i=-1;
        List<Sprite> newSprites = new ArrayList<>();
        for(Sprite s:sprites){
            i++;
            if( s==null ){
                throw new IllegalArgumentException("sprites["+i+"]==null");
            }else{
                newSprites.add(s);
            }
        }
        synchronized( this ){
            this.sprites.clear();
            this.sprites.addAll(newSprites);
            onSpritesChanged();
        }
    }

    public SpriteLine sprites(List<Sprite> sprites){
        if( sprites==null )throw new IllegalArgumentException( "sprites==null" );
        setSprites(sprites);
        return this;
    }

    /**
     * Указывает набор спрайтов
     * @param images набор спрайтов
     * @return Self ссылка
     */
    public SpriteLine images(Iterable<BufferedImage> images){
        if( images==null )throw new IllegalArgumentException( "images==null" );
        List<Sprite> newSprites = new ArrayList<>();
        int i=-1;
        for( var im : images ){
            i++;
            if( im==null )throw new IllegalArgumentException("images["+i+"]==null");
            newSprites.add(new Sprite(im));
        }
        setSprites(newSprites);
        return this;
    }
    //endregion
    //region getSizes() : List<Size2D> - размеры кадров
    private volatile List<Size2D> sizes;

    /**
     * Размеры кадров
     * @return размеры кадров
     */
    protected List<Size2D> sizes(){
        if( sizes!=null )return sizes;
        synchronized( this ){
            if( sizes!=null )return sizes;
            sizes = sprites.stream().map(Sprite::size).collect(Collectors.toList());
            return sizes;
        }
    }
    //endregion

    //region maxSize() - Максимальный размер кадра
    protected volatile Size2D maxSize;

    /**
     * Возвращает максимальный размер кадра
     * @return Максимальный размер кадра
     */
    public Size2D maxSize(){
        if( maxSize!=null )return maxSize;
        synchronized( this ){
            if( maxSize!=null )return maxSize;
            List<Size2D> sizes = sizes();
            if( sizes.isEmpty() ){
                maxSize = new MutableRect();
            }else if( sizes.size()==1 ){
                maxSize = sizes.get(0);
            }else{
                maxSize = sizes.stream().max(Comparator.comparingDouble(Size2D::area)).get();
            }
            return maxSize;
        }
    }
    //endregion
    //region minSize() - Минимальный размер кадра
    protected volatile Size2D minSize;

    /**
     * Возвращает минимальный размер кадра
     * @return Минимальный размер кадра
     */
    public Size2D minSize(){
        if( minSize!=null )return minSize;
        synchronized( this ){
            if( minSize!=null )return minSize;
            List<Size2D> sizes = sizes();
            if( sizes.isEmpty() ){
                minSize = new MutableRect();
            }else if( sizes.size()==1 ){
                minSize = sizes.get(0);
            }else{
                minSize = sizes.stream().min(Comparator.comparingDouble(Size2D::area)).get();
            }
            return minSize;
        }
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

    //region start()/stop()/isRunning()
    /**
     * В режиме анимации
     * @return true - режим анимации, присуствует смена кадров
     */
    public boolean isAnimationRunning(){
        if( started==0 )return false;
        return stopped == 0;
    }

    /**
     * Запуск анимации
     * @return Self ссылка
     */
    public SpriteLine startAnimation(){
        stopped = 0;
        started = System.nanoTime();
        return this;
    }

    /**
     * Остановка анимации
     * @return Self ссылка
     */
    public SpriteLine stopAnimation(){
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
    //endregion

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

    public SpriteFigura toFigure(){
        return new SpriteFigura(this);
    }
}
