package xyz.cofe.game.tank.store;

import xyz.cofe.game.tank.geom.Point;
import xyz.cofe.game.tank.unt.Bullet;
import xyz.cofe.game.tank.unt.Direction;
import xyz.cofe.game.tank.unt.Figura;

import java.util.function.Supplier;

public class BulletV extends OBJ<Bullet> {
    public BulletV() {
        super(Bullet.class, Bullet::new);
    }

    public final ObjKey<Point> location = new ObjKey<Point>("location", Figura::getLocation, Figura::location);
    public final StringKey direction = new StringKey("direction",ob -> ob.direction().name(), (ob,v)->ob.direction(Direction.valueOf(v)) );
}
