package xyz.cofe.game.tank.job;

public class JobStarted<JOB extends Job<JOB>> implements JobEvent<JOB> {
    public JobStarted(JOB job){
        if( job==null )throw new IllegalArgumentException( "job==null" );
        this.job = job;
    }

    protected JOB job;

    @Override
    public JOB getJob(){
        return job;
    }
}
