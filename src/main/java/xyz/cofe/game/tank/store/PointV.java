package xyz.cofe.game.tank.store;

import xyz.cofe.game.tank.geom.Point;

public class PointV extends OBJ<Point> {
    public static final PointV instance = new PointV();
    private PointV() {
        super(Point.class, () -> Point.of(0, 0));
    }

    final DoubleKey x = new DoubleKey("x", Point::x, (p, x) -> Point.of(x, p.y()));
    final DoubleKey y = new DoubleKey("y", Point::y, (p, y) -> Point.of(p.x(), y));
}
