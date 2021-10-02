package xyz.cofe.game.tank.store;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import xyz.cofe.game.tank.geom.Point;
import xyz.cofe.game.tank.unt.Brick;
import xyz.cofe.game.tank.unt.Scene;
import xyz.cofe.game.tank.unt.Water;

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

    public static class Compaund1 {
        private Brick brick1;
        public Brick getBrick1() {
            return brick1;
        }

        public void setBrick1(Brick brick1) {
            this.brick1 = brick1;
        }

        private Brick brick2;

        public Brick getBrick2() {
            return brick2;
        }

        public void setBrick2(Brick brick2) {
            this.brick2 = brick2;
        }
    }

    public static class Compaund1View extends OBJ<Compaund1> {
        public Compaund1View() {
            super(Compaund1.class, Compaund1::new);
        }
        final ObjKey<Brick> brick1 = new ObjKey<Brick>(
            "brick1", Compaund1::getBrick1, (p, b) -> {p.setBrick1(b); return p;} );
        final ObjKey<Brick> brick2 = new ObjKey<Brick>(
            "brick2", Compaund1::getBrick2, (p, b) -> {p.setBrick2(b); return p;} );
    }

    @Test
    public void crossRef(){
        ObjectMappers.mappers.put(Compaund1.class, new Compaund1View());
        ObjectMappers.mappers.put(Brick.class, BrickV.instance);
        ObjectMappers.mappers.put(Point.class, PointV.instance);

        Compaund1 compaund1 = new Compaund1();
        compaund1.setBrick1(new Brick().location(1,2));
        compaund1.setBrick2(compaund1.getBrick1());

        var map = new MapStore().store(compaund1);
        System.out.println(map);

        var r = new MapStore().restore(map);
        Assertions.assertTrue(r instanceof Compaund1);
        compaund1 = (Compaund1) r;

        Assertions.assertTrue(compaund1.getBrick1()!=null);
        Assertions.assertTrue(compaund1.getBrick2()!=null);
        Assertions.assertTrue(compaund1.getBrick1()==compaund1.getBrick2());

        var om = new ObjectMapper();
        om.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            System.out.println(om.writeValueAsString(map));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void sceneFig01(){
        var scene = new Scene();
        scene.getFigures().add(new Brick().location(1,1));
        scene.getFigures().add(new Water().location(10,1));

        var map = new MapStore().store(scene);

        var om = new ObjectMapper();
        om.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            System.out.println(om.writeValueAsString(map));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return;
        }

        var scene01 = new MapStore().restore(map);
        Assertions.assertTrue(scene01 instanceof Scene);

        scene = (Scene)scene01;
        Assertions.assertTrue(scene.getFigures().size()==2);
        Assertions.assertTrue(scene.getFigures().stream().filter(f -> f instanceof Brick).count()>0);
        Assertions.assertTrue(scene.getFigures().stream().filter(f -> f instanceof Water).count()>0);
    }
}

