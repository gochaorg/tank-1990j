package xyz.cofe.game.tank.job;

import xyz.cofe.ecolls.Closeables;

import java.util.Set;
import java.util.function.Consumer;

/**
 * Периодично выполняемая задача
 * @param <SELF> Дочерний класс реализующий задачу
 */
public interface Job<SELF extends Job<SELF>> extends Runnable {
    /**
     * Запуск планового выполнения задачи
     * @return SELF ссылка
     */
    SELF start();

    /**
     * Остановка планового выполнения задачи
     * @return SELF ссылка
     */
    @SuppressWarnings("UnusedReturnValue")
    SELF stop();

    /**
     * Задача запланирована и выполняется
     * @return true - запущена и еще пока не остановлена, false - задача или остановлена, или не была запущена
     */
    boolean isRunning();

    public boolean isStarted();

    /**
     * Время начала задания - System.currentTimeMillis()
     * @return Время начала задания
     */
    public long getStartedTime();

    public boolean isStopped();

    /**
     * Время завершения задания, System.currentTimeMillis()
     * @return Время завершения задания
     */
    public long getStoppedTime();

    //region Подписчики / Listeners
    /**
     * Проверка наличия подписчика
     * @param listener подписчик
     * @return true - подписчик, подписан на события
     */
    boolean hasJobListener(JobListener<SELF> listener);

    /**
     * Получение списка подписчиков
     * @return подписчики
     */
    Set<JobListener<SELF>> getJobListeners();

    /**
     * Добавления подписчика
     * @param listener подписчик
     * @return Отписка от событий
     */
    AutoCloseable addJobListener(JobListener<SELF> listener);

    /**
     * Добавления подписчика
     * @param listener подписчик
     * @param weakLink подписчик добавляется как weak ссылка
     * @return Отписка от событий
     */
    AutoCloseable addJobListener(JobListener<SELF> listener, boolean weakLink);

    /**
     * Удаление подписчика
     * @param listener подписчик
     */
    void removeJobListener(JobListener<SELF> listener);

    /**
     * Подписка на события запуска задания
     * @param closeables принимает отписки от событий
     * @param listener подписчик
     * @return SELF ссылки
     */
    @SuppressWarnings("unchecked")
    public default SELF onStarted(Closeables closeables, Consumer<JobStarted<SELF>> listener){
        if( listener==null )throw new IllegalArgumentException( "listener==null" );
        var cl = addJobListener(jobEvent -> {
            if( jobEvent instanceof JobStarted ){
                listener.accept((JobStarted<SELF>) jobEvent);
            }
        });
        if( closeables!=null ){
            closeables.add(cl);
        }
        return (SELF) this;
    }

    /**
     * Подписка на события запуска задания
     * @param listener подписчик
     * @return SELF ссылки
     */
    public default SELF onStarted(Consumer<JobStarted<SELF>> listener){
        return onStarted(null, listener);
    }

    /**
     * Подписка на события остановки задания
     * @param closeables принимает отписки от событий
     * @param listener подписчик
     * @return SELF ссылки
     */
    @SuppressWarnings("unchecked")
    public default SELF onStopped(Closeables closeables, Consumer<JobStopped<SELF>> listener){
        if( listener==null )throw new IllegalArgumentException( "listener==null" );
        var cl = addJobListener(jobEvent -> {
            if( jobEvent instanceof JobStopped ){
                listener.accept((JobStopped<SELF>) jobEvent);
            }
        });
        if( closeables!=null ){
            closeables.add(cl);
        }
        return (SELF) this;
    }

    /**
     * Подписка на события остановки задания
     * @param listener подписчик
     * @return SELF ссылки
     */
    public default SELF onStopped(Consumer<JobStopped<SELF>> listener){
        return onStopped(null, listener);
    }

    /**
     * Подписка на события выполнения задания
     * @param closeables принимает отписки от событий
     * @param listener подписчик
     * @return SELF ссылки
     */
    @SuppressWarnings("unchecked")
    public default SELF onExecuted(Closeables closeables, Consumer<JobExecuted<SELF>> listener){
        if( listener==null )throw new IllegalArgumentException( "listener==null" );
        var cl = addJobListener(jobEvent -> {
            if( jobEvent instanceof JobExecuted ){
                listener.accept((JobExecuted<SELF>) jobEvent);
            }
        });
        if( closeables!=null ){
            closeables.add(cl);
        }
        return (SELF) this;
    }

    /**
     * Подписка на события выполнения задания
     * @param listener подписчик
     * @return SELF ссылки
     */
    public default SELF onExecuted(Consumer<JobExecuted<SELF>> listener){
        return onExecuted(null, listener);
    }
    //endregion
}
