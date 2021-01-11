package xyz.cofe.game.tank.job;

/**
 * Задание запущено
 * @param <JOB> класс задания
 */
public class JobStarted<JOB extends Job<JOB>> implements JobEvent<JOB> {
    /**
     * Конструктор
     * @param job задание
     */
    public JobStarted(JOB job){
        if( job==null )throw new IllegalArgumentException( "job==null" );
        this.job = job;
    }

    protected JOB job;

    /**
     * Возвращает задание с которым связано событие
     * @return задание
     */
    @Override
    public JOB getJob(){
        return job;
    }
}
