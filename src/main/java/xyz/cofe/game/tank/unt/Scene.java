package xyz.cofe.game.tank.unt;

import xyz.cofe.collection.BasicEventList;
import xyz.cofe.collection.EventList;
import xyz.cofe.ecolls.ListenersHelper;
import xyz.cofe.game.tank.geom.Size2D;
import xyz.cofe.gui.swing.bean.UiBean;

import java.awt.Color;
import java.util.Set;

/**
 * Сцена
 */
public class Scene {
    //region event - Событие сцены
    /**
     * Событие сцены
     */
    public static class Event {
        protected Scene scene;
        public Event(Scene scene){
            this.scene = scene;
        }

        /**
         * Возвращает ссылку на сцену
         * @return сцена
         */
        public Scene getScene(){ return scene; }
    }

    /**
     * Подписчик на события сцены
     */
    public interface Listener {
        /**
         * События сцены
         * @param ev событие
         */
        void sceneEvent( Event ev );
    }

    protected final ListenersHelper<Listener,Event> listeners = new ListenersHelper<>(Listener::sceneEvent);

    /**
     * Проверка наличия подписчика
     * @param listener  подписчик
     * @return true - подписчик, подписан
     */
    public boolean hasListener(Listener listener) {
        return listeners.hasListener(listener);
    }

    /**
     * Полчение списка подписчиков
     * @return подписчики
     */
    public Set<Listener> getListeners() {
        return listeners.getListeners();
    }

    /**
     * Добавление подписчика
     * @param listener подписчик
     * @return отписка
     */
    public AutoCloseable addListener(Listener listener) {
        return listeners.addListener(listener);
    }

    /**
     * Добавление подписчика
     * @param listener подписчик
     * @param weakLink true - listener будет добавлен как WeakRef / false - как HardLink
     * @return отписка
     */
    public AutoCloseable addListener(Listener listener, boolean weakLink) {
        return listeners.addListener(listener, weakLink);
    }

    /**
     * Удаление подписчика
     * @param listener подписчик
     */
    public void removeListener(Listener listener) {
        listeners.removeListener(listener);
    }

    /**
     * Удаление всех подписчиков
     */
    public void removeAllListeners() {
        listeners.removeAllListeners();
    }

    /**
     * рассылка уведомлений
     * @param event уведомление
     */
    public void fireEvent(Event event) {
        listeners.fireEvent(event);
    }

    /**
     * Добавление уведомления в очередь
     * @param ev уведомление
     */
    public void addEvent(Event ev) {
        listeners.addEvent(ev);
    }

    /**
     * Обработка очереди уведомлений
     */
    public void runEventQueue() {
        listeners.runEventQueue();
    }
    //endregion

    //region figures : EventList<Figura<?>>
    @SuppressWarnings("rawtypes")
    protected final FiguraListener figuraListener = event -> {
        if( event instanceof FiguraMoved ){
            fireEvent(new MovedFigure(Scene.this, (FiguraMoved<?>) event));
        }
    };

    /** Фигуры расставленные на сцене */
    protected final EventList<Figura<?>> figures = new BasicEventList<>();
    {
        figures.onChanged((idx,oldItem,newItem)->{
            if( oldItem!=null ){
                fireEvent(new RemoveFigure(this,oldItem));
                //noinspection unchecked
                oldItem.removeFiguraListener(figuraListener);
            }
            if( newItem!=null ){
                fireEvent(new AddFigure(this,newItem));
                //noinspection unchecked
                newItem.addFiguraListener(figuraListener);
            }
        });
    }

    /**
     * Фигуры расставленные на сцене
     * @return фигуры
     */
    public EventList<Figura<?>> getFigures(){
        return figures;
    }
    //endregion

    //region AddFigure / RemoveFigure

    /**
     * Фигура добавлена на сцену
     */
    public static class AddFigure extends Event {
        public AddFigure(Scene scene, Figura<?> f) {
            super(scene);
            figura = f;
        }

        protected Figura<?> figura;
        public Figura<?> getFigura(){ return figura; }
    }

    /**
     * Фигура удалена со сцены
     */
    public static class RemoveFigure extends Event {
        public RemoveFigure(Scene scene, Figura<?> f) {
            super(scene);
            figura = f;
        }

        protected Figura<?> figura;
        public Figura<?> getFigura(){ return figura; }
    }
    //endregion
    //region MovedFigure
    /**
     * Перемещение фигуры по сцене
     */
    public static class MovedFigure extends Event {
        public MovedFigure(Scene scene, FiguraMoved<?> moved ) {
            super(scene);
            this.moved = moved;
        }

        protected FiguraMoved<?> moved;
        public FiguraMoved<?> getFigura(){ return moved; }
    }
    //endregion

    //region size
    //region SizeChanged
    public static class SizeChanged extends Event {
        public final Size2D previous;
        public final Size2D current;
        public SizeChanged(Scene scene,Size2D previous,Size2D current) {
            super(scene);
            if( previous==null )throw new IllegalArgumentException( "previous==null" );
            if( current==null )throw new IllegalArgumentException( "current==null" );
            this.previous = previous;
            this.current = current;
        }
    }
    //endregion
    //region width : double
    protected double width = 32 * 8;
    public double getWidth() {
        return width;
    }
    public void setWidth(double width) {
        var old = Size2D.of(width,height);
        this.width = width;
        size2D = Size2D.of(width,height);
        fireEvent(new SizeChanged(this,old,size2D));
    }
    //endregion
    //region height : double
    protected double height = 32 * 8;
    public double getHeight() {
        return height;
    }
    public void setHeight(double height) {
        var old = Size2D.of(width,height);
        this.height = height;
        size2D = Size2D.of(width,height);
        fireEvent(new SizeChanged(this,old,size2D));
    }
    //endregion
    //region size : Size2D
    private Size2D size2D = Size2D.of(width,height);
    @UiBean(forceHidden = true)
    public Size2D getSize(){ return size2D; }
    public void setSize(Size2D size){
        if( size==null )throw new IllegalArgumentException( "size==null" );
        var old = Size2D.of(width,height);
        width = size.width();
        height = size.height();
        fireEvent(new SizeChanged(this,old,size2D));
    }
    //endregion
    //endregion

    //region borderWidth : double
    protected double borderWidth = 32*2;
    public double getBorderWidth() {
        return borderWidth;
    }
    public void setBorderWidth(double borderWidth) {
        this.borderWidth = borderWidth;
    }
    //endregion
    //region borderColor : Color
    protected Color borderColor = Color.gray;

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }
    //endregion

    public void assign(Scene scene){
        if( scene==null )throw new IllegalArgumentException( "scene==null" );
        getFigures().clear();
        getFigures().addAll(scene.getFigures());
        setSize(scene.getSize());
        setBorderColor(scene.getBorderColor());
        setBorderWidth(scene.getBorderWidth());
    }
}
