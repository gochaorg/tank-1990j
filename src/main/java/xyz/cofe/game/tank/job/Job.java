package xyz.cofe.game.tank.job;

import xyz.cofe.ecolls.Closeables;
import xyz.cofe.game.tank.Observers;

import java.util.Set;
import java.util.function.Consumer;

/**
 * Периодично выполняемая задача
 * @param <SELF> Дочерний класс реализующий задачу
 */
public interface Job<SELF extends Job<SELF>> extends Runnable {
    /**
     * Событие старта задания
     * @return старт задания
     */
    Observers<SELF> started();

    /**
     * Событие остановки задания
     * @return остановка задания
     */
    Observers<SELF> stopped();

    /**
     * Событие выполнения задания
     * @return выполнения задания
     */
    Observers<SELF> executed();

    //region start() / stop() / isRunning()
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
    //endregion
    //region started, startedTime
    /**
     * Флаг - задание запущенно
     * @return true - задание запущенно; false - не было запущено.
     */
    public boolean isStarted();

    /**
     * Время начала задания - System.currentTimeMillis()
     * @return Время начала задания; 0 - не было завершено или не было запущено
     */
    public long getStartedTime();
    //endregion
    //region stopped, stoppedTime
    /**
     * Флаг - задание завершено
     * @return true - задание завершено; false - не было запущено или еще не завершено.
     */
    public boolean isStopped();

    /**
     * Время завершения задания, System.currentTimeMillis()
     * @return Время завершения задания; 0 - не было завершено или не было запущено
     */
    public long getStoppedTime();
    //endregion
}
