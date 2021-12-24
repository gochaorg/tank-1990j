package xyz.cofe.game.tank.gcycle;

import xyz.cofe.game.tank.GameUnit;
import xyz.cofe.game.tank.Observers;
import xyz.cofe.game.tank.job.MoveCollector;
import xyz.cofe.game.tank.job.Moving;
import xyz.cofe.game.tank.unt.*;
import xyz.cofe.iter.Eterable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Игровой цикл
 */
public class GameCycle {
    /**
     * Уведомление о удалении фигуры со сцены
     */
    private final Observers<Figure<?>> figureDeleted = new Observers<>();

    /**
     * Сцена
     */
    public final Scene scene;

    /**
     * Удаление фигуры со сцены с уведомлением
     * @param figure фигура
     */
    private void delete( Figure<?> figure ){
        if( figure==null )throw new IllegalArgumentException( "figure==null" );
        scene.getFigures().remove(figure);
        figureDeleted.fire(figure);
    }

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
            .filter( f -> f instanceof Player
                || f instanceof Brick
                || f instanceof Steel
            ));
    }

    /**
     * Передвигаемые объекты
     */
    protected Eterable<GameUnit<?>> gameUnits;

    /**
     * Перемещения по сцене
     */
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

    private void acceptFire( LevelBrick<?> brick, Bullet bullet ){
        // Состояние                            direction
        // -----------------------------------|-------|-------|-------|-------|
        //        - верх | верх | ниж  | ниж  | R     | L     | U     | D     |
        //        - лев  | прав | лев  | прав |       |       |       |       |
        // -----------------------------------|-------|-------|-------|-------|
        // 0b0000 -      |      |      |      |       |       |       |       |
        //                                    |       |       |       |       |
        // 0b0001 - есть |      |      |      |       |       |       |       |
        // 0b0010 -      | есть |      |      |       |       |       |       |
        // 0b0011 - есть | есть |      |      |       |       |       |       |
        // 0b0100 -      |      | есть |      |       |       |       |       |
        // 0b0101 - есть |      | есть |      |       |       |       |       |
        // 0b0110 -      | есть | есть |      |       |       |       |       |
        // 0b0111 - есть | есть | есть |      |       |       |       |       |
        // 0b1000 -      |      |      | есть |       |       |       |       |
        // 0b1001 - есть |      |      | есть |       |       |       |       |
        // 0b1010 -      | есть |      | есть |       |       |       |       |
        // 0b1011 - есть | есть |      | есть |       |       |       |       |
        // 0b1100 -      |      | есть | есть |       |       |       |       |
        // 0b1101 - есть |      | есть | есть |       |       |       |       |
        // 0b1110 -      | есть | есть | есть |       |       |       |       |
        // 0b1111 - есть | есть | есть | есть |       |       | 0011  |       |


        var state = brick.state();
        if( state==0b0100 || state==0b1000 || state==0b0010 || state==0b0001 || state==0b0000 ){
            delete(brick);
        }else {
            // Предполагается что при импорте каждый блок будут преобразован в 4 блока - 2 на 2.
            throw new UnsupportedOperationException("not implement");
        }

        if( brick.state()==0 ){
            delete(brick);
        }
    }

    /**
     * Расчет очередного цикла
     */
    public void next(){
        if( !running )return;

        // перемещение фигур с расчетом пересечений
        moveCollector.estimate().apply(3, (moveJob,unit,coll)->{

            // Танк уперся в стену или в стальной блок ?
            if( unit instanceof Player && coll.collisionObject instanceof LevelBrick ){
                ((Player<?>)unit).stop();
            }else

            // Пуля попала в стену ?
            if( unit instanceof Bullet && coll.collisionObject instanceof LevelBrick ){
                var bullet = (Bullet)unit;
                if( coll.collisionObject instanceof Brick ) {
                    acceptFire((LevelBrick<?>) coll.collisionObject, bullet);
                }
                bullet.stop();
                delete(bullet);
            }
        });

        var drops = movings
            .map(Moving::getGameUnit)
            .filter( f -> f instanceof Figure)
            .filter( f -> f.right() < 0
                || f.left() > scene.getWidth()
                || f.top() > scene.getHeight()
                || f.bottom() < 0
            )
            .map( f -> (Figure<?>)f )
            .toList();

        if( drops.size()>0 ){
            for( var d : drops ){
                delete(d);
            }
        }
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

    // Скорость танка
    private double tankSpeed = 32;

    // Скорость снаряда
    private double bulletSpeed = tankSpeed * 4;

    // Максимальная частота выстрелов
    private long fireDelay = 1000 / 2;

    // Время последнего выстрела
    private Map<Player<?>,Long> lastFire = new HashMap<>();
    {
        figureDeleted.listen( ev -> {
            if( ev.event instanceof Player ) {
                lastFire.remove(ev.event);
            }
        });
    }

    /**
     * Выстрел игрока
     * @param player игрок
     * @return true - выстрел произведен; false - не произведен по причине ограничения частоты
     */
    private boolean fire( Player<?> player ){
        var timeout =
            System.currentTimeMillis() -
                lastFire.getOrDefault(player,0L);

        if( timeout<fireDelay )return false;

        lastFire.put(player,System.currentTimeMillis());

        Bullet bullet = player.createBullet();
        scene.getFigures().add(bullet);
        bullet.setJob(
            bullet.moving
                .direction(player.direction())
                .speed(bulletSpeed).start()
        );

        return true;
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
                player.stop();
            }else if( userInput instanceof UserMoveStart ){
                var ums = (UserMoveStart)userInput;
                var j = player.getJob();
                if( j instanceof Moving ){
                    var m = ((Moving<?>)j);
                    if( m.getDirection()!=ums.direction ){
                        player.stop();
                        player.move(ums.direction, tankSpeed);
                    }
                }else{
                    player.move(ums.direction, tankSpeed);
                }
            }else if( userInput instanceof UserFire ){
                fire(player);
            }
        });
    }
}
