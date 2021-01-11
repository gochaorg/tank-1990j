package xyz.cofe.game.tank.job;

/**
 * Ошибка изменения задания, задание уже запущено и измеенение его работы не предусмотрено, необходимо остановить задание
 */
public class AlreadyRunnedError extends IllegalStateException {
    public AlreadyRunnedError(){
        super("already runned");
    }

    public AlreadyRunnedError(String s){
        super(s);
    }

    public AlreadyRunnedError(String message, Throwable cause){
        super(message, cause);
    }

    public AlreadyRunnedError(Throwable cause){
        super(cause);
    }
}
