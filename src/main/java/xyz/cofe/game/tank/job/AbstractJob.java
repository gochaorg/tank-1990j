package xyz.cofe.game.tank.job;

import xyz.cofe.ecolls.ListenersHelper;
import xyz.cofe.game.tank.Observers;

import java.util.Set;

/**
 * Абстрактная заготовка для периодической задачи
 * @param <SELF> Дочерний класс
 */
public abstract class AbstractJob<SELF extends AbstractJob<SELF>> implements Runnable, Job<SELF> {
    private final Observers<SELF> started = new Observers<>();
    private final Observers<SELF> stopped = new Observers<>();
    private final Observers<SELF> executed = new Observers<>();

    /**
     * Событие старта задания
     * @return старт задания
     */
    public Observers<SELF> started(){ return this.started; }

    /**
     * Событие остановки задания
     * @return остановка задания
     */
    public Observers<SELF> stopped(){ return this.stopped; }

    /**
     * Событие выполнения задания
     * @return выполнения задания
     */
    public Observers<SELF> executed(){ return this.executed; }


    //region start(), doStart()
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

        SELF self = (SELF)this;
        started.fire(self);
        return self;
    }

    protected static class DoStart {
        public final long duration;
        public DoStart(long duration){
            if( duration<0 )throw new IllegalArgumentException( "duration<0" );
            this.duration = duration;
        }
    }

    protected abstract DoStart doStart();
    //endregion

    //region stop()
    /**
     * Остановка планового выполнения задачи
     * @return SELF ссылка
     */
    @SuppressWarnings("unchecked")
    public SELF stop(){
        if( isRunning() ){
            this.stoppedTime = System.currentTimeMillis();
            stopped.fire((SELF) this);
        }

        return (SELF) this;
    }
    //endregion

    //region run() / doRun()
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
            //noinspection unchecked
            executed.fire((SELF) this);
        }
    }

    /**
     * Выполнение задания
     * @return true - успешное выполнение
     */
    protected abstract boolean doRun();
    //endregion

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
    public long getStartedTime(){
        return startedTime;
    }

    public boolean isStarted(){ return startedTime>0; }
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
    public long getStoppedTime(){
        return stoppedTime;
    }

    public boolean isStopped(){ return stoppedTime>0; }
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
}
