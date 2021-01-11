package xyz.cofe.game.tank.job;

/**
 * Задание выполнено
 * @param <JOB> класс задания
 */
public class JobExecuted<JOB extends Job<JOB>> implements JobEvent<JOB> {
    public JobExecuted(JOB job){
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
