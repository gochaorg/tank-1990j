package xyz.cofe.game.tank.job;

public class JobStopped<JOB extends Job<JOB>> implements JobEvent<JOB> {
    public JobStopped(JOB job){
        if( job==null )throw new IllegalArgumentException( "job==null" );
        this.job = job;
    }
    
    protected JOB job;

    @Override
    public JOB getJob(){
        return job;
    }
}
