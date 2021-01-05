package xyz.cofe.game.tank.job;

import xyz.cofe.ecolls.Closeables;

import java.util.Set;
import java.util.function.Consumer;

public interface Job<SELF extends Job<SELF>> {
    SELF start();
    SELF stop();
    boolean isRunning();

    boolean hasJobListener(JobListener<SELF> listener);
    Set<JobListener<SELF>> getJobListeners();
    AutoCloseable addJobListener(JobListener<SELF> listener);
    AutoCloseable addJobListener(JobListener<SELF> listener, boolean weakLink);
    void removeJobListener(JobListener<SELF> listener);

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
    public default SELF onStarted(Consumer<JobStarted<SELF>> listener){
        return onStarted(null, listener);
    }

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
    public default SELF onStopped(Consumer<JobStopped<SELF>> listener){
        return onStopped(null, listener);
    }

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
    public default SELF onExecuted(Consumer<JobExecuted<SELF>> listener){
        return onExecuted(null, listener);
    }
}
