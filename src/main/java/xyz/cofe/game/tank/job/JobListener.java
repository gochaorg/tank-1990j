package xyz.cofe.game.tank.job;

/**
 * Подписчик на события задания
 * @param <JOB> Класс задания
 */
public interface JobListener<JOB extends Job<JOB>> {
    /**
     * Уведомление о событии задания
     * @param jobEvent событие
     */
    void jobEvent( JobEvent<JOB> jobEvent );
}
