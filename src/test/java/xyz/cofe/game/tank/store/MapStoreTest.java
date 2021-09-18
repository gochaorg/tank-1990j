package xyz.cofe.game.tank.store;

import org.junit.jupiter.api.Test;
import xyz.cofe.game.tank.geom.Point;
import xyz.cofe.game.tank.unt.Brick;
import xyz.cofe.game.tank.unt.Figura;
import xyz.cofe.game.tank.unt.Scene;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class MapStoreTest {
    @Test
    public void pointStore(){
        var m = new MapStore().store(Point.of(1,2));
        System.out.println(m);

        Point x = new MapStore().restore(m);
        System.out.println(x);
    }

    @Test
    public void brickStore(){
        var b = new Brick().location(Point.of(1,2));
        var m = new MapStore().store(b, BrickV.instance);
        System.out.println(m);

        var x = new MapStore().restore(m);
        System.out.println(x);
    }
}

