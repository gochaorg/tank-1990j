package xyz.cofe.game.tank.unt;

import xyz.cofe.collection.BasicEventList;
import xyz.cofe.collection.EventList;
import xyz.cofe.ecolls.ListenersHelper;

import java.util.Set;

/**
 * Сцена
 */
public class Scene {
    protected final FiguraListener figuraListener = new FiguraListener() {
        @Override
        public void figuraEvent(FiguraEvent event) {
            if( event instanceof FiguraMoved ){
                fireEvent(new MovedFigure(Scene.this, (FiguraMoved<?>) event));
            }
        }
    };

    /** Фигуры расставленные на сцене */
    protected final EventList<Figura<?>> figures = new BasicEventList<>();
    {
        figures.onChanged((idx,oldItem,newItem)->{
            if( oldItem!=null ){
                fireEvent(new RemoveFigure(this,oldItem));
                oldItem.removeFiguraListener(figuraListener);
            }
            if( newItem!=null ){
                fireEvent(new AddFigure(this,newItem));
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
}
