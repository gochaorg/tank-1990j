package xyz.cofe.game.tank.store;

import xyz.cofe.game.tank.geom.Point;
import xyz.cofe.game.tank.unt.*;

public class EnemyV extends OBJ<Enemy> implements ObjectMapper {
    public static final EnemyV instance = new EnemyV();
    @Override public void registry(ObjectMappers om) {
        ObjectMappers.mappers.put(instance.clazz, instance);
    }

    public EnemyV() {
        super(Enemy.class, Enemy::new);
    }

    public final ObjKey<Point> location = new ObjKey<Point>("location",
        Figure::getLocation,
        (ob,v) -> {
            ob.setLocation(v);
            return ob;
        }
    );

    public final StringKey direction = new StringKey("direction",
        ob -> ob.direction().name(),
        (ob,v)->{
            ob.direction(Direction.valueOf(v));
            return ob;
        }
    );

    public final StringKey enemyState = new StringKey("enemyState",
        ob -> ob.getEnemyState().name(),
        (ob,v)->{
            ob.setEnemyState(EnemyState.valueOf(v));
            return ob;
        }
    );
}
