package xyz.cofe.game.tank.job;

import java.util.Set;

public interface Job<SELF extends Job<SELF>> {
    SELF start();
    SELF stop();
    boolean isRunning();

    boolean hasJobListener(JobListener<SELF> listener);
    Set<JobListener<SELF>> getJobListeners();
    AutoCloseable addJobListener(JobListener<SELF> listener);
    AutoCloseable addJobListener(JobListener<SELF> listener, boolean weakLink);
    void removeJobListener(JobListener<SELF> listener);
}
