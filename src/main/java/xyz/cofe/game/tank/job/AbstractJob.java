package xyz.cofe.game.tank.job;

import xyz.cofe.ecolls.ListenersHelper;

import java.util.Set;

/**
 * Абстрактная заготовка для периодической задачи
 * @param <SELF> Дочерний класс
 */
public abstract class AbstractJob<SELF extends AbstractJob<SELF>> implements Runnable, Job<SELF> {
    protected final ListenersHelper<JobListener<SELF>, JobEvent<SELF>> listeners
        = new ListenersHelper<>(JobListener::jobEvent);

    /**
     * Запуск планового выполнения задачи
     * @return SELF ссылка
     */
    @SuppressWarnings("unchecked")
    public SELF start(){
        if( isRunning() )throw new AlreadyRunnedError();

        this.startedTime = System.currentTimeMillis();
        this.stoppedTime = 0;

        var starting = doStart();

        nextRunTime = startedTime + starting.duration;
        duration = starting.duration;

        fireStarted();
        return (SELF) this;
    }

    protected static class DoStart {
        public final long duration;
        public DoStart(long duration){
            if( duration<0 )throw new IllegalArgumentException( "duration<0" );
            this.duration = duration;
        }
    }

    protected abstract DoStart doStart();

    /**
     * Остановка планового выполнения задачи
     * @return SELF ссылка
     */
    @SuppressWarnings("unchecked")
    public SELF stop(){
        if( isRunning() ){
            this.stoppedTime = System.currentTimeMillis();
            fireStopped();
        }

        return (SELF) this;
    }

    /**
     * Выполнение задания
     */
    @Override
    public void run(){
        if( !isRunning() ){
            return;
        }

        long now = System.currentTimeMillis();
        if( nextRunTime >now )return;

        nextRunTime = now + getDuration();

        var succ = doRun();
        if( succ ){
            fireExecuted();
        }
    }

    /**
     * Выполнение задания
     * @return true - успешное выполнение
     */
    protected abstract boolean doRun();

    //region duration - промежуток времени между выполнении задания (мс)
    /**
     * Через какой промежуток времени необходимо передвинуть момент (moveNextTime = moveNextTime + moveDuration)
     */
    protected long duration;

    /**
     * Через какой промежуток времени необходимо передвинуть момент (moveNextTime = moveNextTime + moveDuration)
     * @return промежуток времени между выполнении задания (мс)
     */
    public long getDuration(){
        return duration;
    }
    //endregion

    //region startedTime - Время начала задания
    /**
     * Время начала задания - System.currentTimeMillis()
     */
    protected long startedTime = 0;

    /**
     * Время начала задания - System.currentTimeMillis()
     * @return Время начала задания
     */
    public long getStarted(){
        return startedTime;
    }
    //endregion

    //region stoppedTime - Время завершения задания
    /**
     * Время завершения задания, System.currentTimeMillis()
     */
    protected long stoppedTime = 0;

    /**
     * Время завершения задания, System.currentTimeMillis()
     * @return Время завершения задания
     */
    public long getStopped(){
        return stoppedTime;
    }
    //endregion

    //region runNextTime - Следующее время выполнения задания
    /**
     * Следующее время выполнения задания - System.currentTimeMillis()
     */
    protected long nextRunTime;

    /**
     * Следующее время выполнения задания - System.currentTimeMillis()
     * @return следующее время выполнения
     */
    public long getNextRun(){
        return nextRunTime;
    }
    //endregion

    //region running - Задача запланирована и выполняется
    /**
     * Задача запланирована и выполняется
     * @return true - запущена и еще пока не остановлена, false - задача или остановлена, или не была запущена
     */
    public boolean isRunning(){
        if( startedTime == 0 ) return false;
        return stoppedTime <= 0;
    }
    //endregion

    //region listeners / Уведомления
    /**
     * Проверка наличия подписчика
     * @param listener подписчик
     * @return true - подписчик, подписан на события
     */
    public boolean hasJobListener(JobListener<SELF> listener){
        return listeners.hasListener(listener);
    }

    /**
     * Получение списка подписчиков
     * @return подписчики
     */
    public Set<JobListener<SELF>> getJobListeners(){
        return listeners.getListeners();
    }

    /**
     * Добавления подписчика
     * @param listener подписчик
     * @return Отписка от событий
     */
    public AutoCloseable addJobListener(JobListener<SELF> listener){
        return listeners.addListener(listener);
    }

    /**
     * Добавления подписчика
     * @param listener подписчик
     * @param weakLink подписчик добавляется как weak ссылка
     * @return Отписка от событий
     */
    public AutoCloseable addJobListener(JobListener<SELF> listener, boolean weakLink){
        return listeners.addListener(listener, weakLink);
    }

    /**
     * Удаление подписчика
     * @param listener подписчик
     */
    public void removeJobListener(JobListener<SELF> listener){
        listeners.removeListener(listener);
    }

    /**
     * Удаляет всех подписчиков
     */
    public void removeAllJobListeners(){
        listeners.removeAllListeners();
    }

    /**
     * Уведомляет подписчиков о событии
     * @param event событие
     */
    protected void fireJobEvent(JobEvent<SELF> event){
        listeners.fireEvent(event);
    }

    /**
     * Добавляет событие в очередь
     * @param ev событие
     */
    protected void addJobEvent(JobEvent<SELF> ev){
        listeners.addEvent(ev);
    }

    /**
     * Выполняет события из очереди
     */
    protected void runJobEventQueue(){
        listeners.runEventQueue();
    }

    /**
     * Уведомление о запуске задания
     */
    @SuppressWarnings("unchecked")
    protected void fireStarted(){
        fireJobEvent(new JobStarted<>((SELF) this));
    }

    /**
     * Уведомление о завершение задания
     */
    @SuppressWarnings("unchecked")
    protected void fireStopped(){
        fireJobEvent(new JobStopped<>((SELF) this));
    }

    /**
     * Уведомление о выполнения задания
     */
    @SuppressWarnings("unchecked")
    protected void fireExecuted(){
        fireJobEvent(new JobExecuted<>((SELF) this));
    }
    //endregion
}
