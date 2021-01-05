package xyz.cofe.game.tank.job;

public interface JobListener<JOB extends Job<JOB>> {
    void jobEvent( JobEvent<JOB> jobEvent );
}
