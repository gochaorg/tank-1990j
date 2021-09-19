package xyz.cofe.game.tank.store;

import xyz.cofe.game.tank.geom.Point;

public class PointV extends OBJ<Point> implements ObjectMapper {
    public static final PointV instance = new PointV();
    @Override public void registry(ObjectMappers om) {
        ObjectMappers.mappers.put(instance.clazz, instance);
    }
    public PointV() {
        super(Point.class, () -> Point.of(0, 0));
    }

    final DoubleKey x = new DoubleKey("x", Point::x, (p, x) -> Point.of(x, p.y()));
    final DoubleKey y = new DoubleKey("y", Point::y, (p, y) -> Point.of(p.x(), y));
}
