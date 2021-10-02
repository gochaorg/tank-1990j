package xyz.cofe.game.tank.store;

import xyz.cofe.game.tank.geom.Point;
import xyz.cofe.game.tank.unt.Bullet;
import xyz.cofe.game.tank.unt.Direction;
import xyz.cofe.game.tank.unt.Figure;

public class BulletV extends OBJ<Bullet> implements ObjectMapper {
    public static final BulletV instance = new BulletV();
    @Override public void registry(ObjectMappers om) {
        ObjectMappers.mappers.put(instance.clazz, instance);
    }

    public BulletV() {
        super(Bullet.class, Bullet::new);
    }

    public final ObjKey<Point> location = new ObjKey<Point>("location", Figure::getLocation, Figure::location);
    public final StringKey direction = new StringKey("direction",ob -> ob.direction().name(), (ob,v)->ob.direction(Direction.valueOf(v)) );
}
