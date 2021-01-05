package xyz.cofe.game.tank.job;

import xyz.cofe.ecolls.ListenersHelper;

import java.util.Set;

public abstract class AbstractJob<SELF extends AbstractJob<SELF>> implements Runnable, Job<SELF> {
    protected final ListenersHelper<JobListener<SELF>, JobEvent<SELF>> listeners
        = new ListenersHelper<>(JobListener::jobEvent);
    /**
     * Время начала движения, момент вызова move() - System.currentTimeMillis()
     */
    protected long startedTime = 0;
    /**
     * Время завершения движения, момент вызова move() - System.currentTimeMillis()
     */
    protected long stoppedTime = 0;

    /**
     * Следущее время когда необходимо передвинуть объект - System.currentTimeMillis()
     */
    protected long runNextTime;
    /**
     * Через какой промежуток времени необходимо передвинуть момент (moveNextTime = moveNextTime + moveDuration)
     */
    protected long duration;

    public boolean hasJobListener(JobListener<SELF> listener){
        return listeners.hasListener(listener);
    }

    public Set<JobListener<SELF>> getJobListeners(){
        return listeners.getListeners();
    }

    public AutoCloseable addJobListener(JobListener<SELF> listener){
        return listeners.addListener(listener);
    }

    public AutoCloseable addJobListener(JobListener<SELF> listener, boolean weakLink){
        return listeners.addListener(listener, weakLink);
    }

    public void removeJobListener(JobListener<SELF> listener){
        listeners.removeListener(listener);
    }

    public void removeAllJobListeners(){
        listeners.removeAllListeners();
    }

    protected void fireJobEvent(JobEvent<SELF> event){
        listeners.fireEvent(event);
    }

    protected void addJobEvent(JobEvent<SELF> ev){
        listeners.addEvent(ev);
    }

    protected void runJobEventQueue(){
        listeners.runEventQueue();
    }

    public long getStarted(){
        return startedTime;
    }

    public long getStopped(){
        return stoppedTime;
    }

    public long getNextRun(){
        return runNextTime;
    }

    public long getDuration(){
        return duration;
    }

    public boolean isRunning(){
        if( startedTime == 0 ) return false;
        if( stoppedTime > 0 ) return false;
        return true;
    }

    public abstract SELF start();

    public abstract SELF stop();

    @Override
    public abstract void run();

    @SuppressWarnings("unchecked")
    protected void fireStarted(){
        fireJobEvent(new JobStarted<>((SELF) this));
    }

    @SuppressWarnings("unchecked")
    protected void fireStopped(){
        fireJobEvent(new JobStopped<>((SELF) this));
    }

    @SuppressWarnings("unchecked")
    protected void fireExecuted(){
        fireJobEvent(new JobExecuted<>((SELF) this));
    }
}
