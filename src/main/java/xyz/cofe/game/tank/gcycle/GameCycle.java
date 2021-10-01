package xyz.cofe.game.tank.gcycle;

import xyz.cofe.game.tank.GameUnit;
import xyz.cofe.game.tank.job.MoveCollector;
import xyz.cofe.game.tank.job.Moving;
import xyz.cofe.game.tank.unt.*;
import xyz.cofe.iter.Eterable;

import java.util.List;
import java.util.Optional;

/**
 * Игровой цикл
 */
public class GameCycle {
    /**
     * Сцена
     */
    public final Scene scene;

    /**
     * Перемещение фигур
     */
    public final MoveCollector moveCollector = new MoveCollector();

    /**
     * Конструктор
     * @param scene сцена
     */
    public GameCycle(Scene scene){
        if( scene==null )throw new IllegalArgumentException( "scene==null" );
        this.scene = scene;

        gameUnits = Eterable
            .of(scene.getFigures())
            .filter( f -> f instanceof GameUnit )
            .map( f -> (GameUnit<?>) f );

        movings = Eterable
            .of(gameUnits)
            .filter( f -> f.getJob() instanceof Moving )
            .map( f -> (Moving<?>) f.getJob() );

        moveCollector.setMovings(movings);

        moveCollector.setCollisions(
        Eterable.of(scene.getFigures())
            .filter( f -> f instanceof Player || f instanceof Brick ));
    }

    protected Eterable<GameUnit<?>> gameUnits;
    protected Eterable<? extends Moving<?>> movings;

    /**
     * Игра запущена или остановлена ?
     */
    protected boolean running;

    /**
     * Запуск цикла
     */
    public void start(){
        running = true;
    }

    /**
     * Остановка цикла
     */
    public void stop(){
        running = false;
    }

    /**
     * Расчет очередного цикла
     */
    public void next(){
        if( !running )return;
        moveCollector.estimate().apply(3, (moveJob,unit,coll)->{
            System.out.println("collision "+unit+" with "+coll.collisionObject+" rect "+coll.intersection);
            if( unit instanceof Player && coll.collisionObject instanceof LevelBrick ){
                ((Player<?>)unit).stop();
            }
        });
    }

    // начальное состояние игрока
    private Player<?> initialPlayer;

    /**
     * Возвращает начальное состояние игрока на карте/сцене
     * @return начальное состояние
     */
    protected Optional<? extends Player<?>> initialPlayer(){
        if( initialPlayer!=null )return Optional.of(initialPlayer);
        var init = scene.getFigures().stream().filter( f -> f instanceof PlayerOne ).map( f -> (PlayerOne)f ).findFirst();
        init.ifPresent(playerOne -> {
            initialPlayer = playerOne;
            scene.getFigures().remove(initialPlayer);
        });
        return init;
    }

    // текущее состояние игрока
    private Player<?> _player;

    /**
     * Возвращает unit текущего игрока
     * @return unit текущего игрока
     */
    protected Optional<? extends Player<?>> player(){
        var p = _player;
        if( p!=null )return Optional.of(p);
        var init = initialPlayer();

        // respawn
        _player = init.map(Player::clone).orElse( null );
        if( _player!=null )scene.getFigures().add(_player);

        return _player!=null ? Optional.of(_player) : Optional.empty();
    }

    /**
     * Пользовательский ввод
     * @param userInput событие ввода пользователя
     */
    public void userInput( UserInput userInput ){
        if( userInput==null )throw new IllegalArgumentException( "userInput==null" );
        if( !running )return;
        player().ifPresent( player -> {
            if( userInput instanceof UserMoveStop ){
                //player.stop();
            }else if( userInput instanceof UserMoveStart ){
                var ums = (UserMoveStart)userInput;
                var j = player.getJob();
                if( j instanceof Moving ){
                    var m = ((Moving<?>)j);
                    if( m.getDirection()!=ums.direction ){
                        player.stop();
                        player.move(ums.direction, 32);
                    }
                }else{
                    player.move(ums.direction, 32);
                }
            }
        });
    }
}
