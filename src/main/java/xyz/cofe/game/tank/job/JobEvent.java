package xyz.cofe.game.tank.job;

/**
 * Событие задания
 * @param <JOB>
 */
public interface JobEvent<JOB extends Job<JOB>> {
    /**
     * Возвращает задание с которым связано событие
     * @return задание
     */
    JOB getJob();
}
