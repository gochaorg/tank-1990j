package xyz.cofe.game.tank.job;

public interface JobEvent<JOB extends Job<JOB>> {
    JOB getJob();
}
