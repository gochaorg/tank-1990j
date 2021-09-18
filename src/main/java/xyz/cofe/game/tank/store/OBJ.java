package xyz.cofe.game.tank.store;

import xyz.cofe.collection.ClassMap;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class OBJ<B> {
    public static final Map<Class<?>,OBJ<?>> objectMappers = new LinkedHashMap<>();

    public final Class<B> clazz;
    public final Supplier<B> newInstance;
    public final List<Key<?>> keys = new ArrayList<>();

    public OBJ(Class<B> cls, Supplier<B> newInst) {
        if (cls == null) throw new IllegalArgumentException("cls==null");
        if (newInst == null) throw new IllegalArgumentException("newInst==null");
        clazz = cls;
        newInstance = newInst;
        objectMappers.put(clazz, this);
    }
    public abstract class Key<V> {
        public final String name;
        public final Function<B, V> read;
        public final BiFunction<B, V, B> write;

        public Key(String name, Function<B, V> read, BiFunction<B, V, B> write) {
            if (name == null) throw new IllegalArgumentException("name==null");
            if (read == null) throw new IllegalArgumentException("read==null");
            if (write == null) throw new IllegalArgumentException("write==null");
            this.name = name;
            this.read = read;
            this.write = write;
            keys.add(this);
        }

        public B set(B b, V v) {
            if (b == null) throw new IllegalArgumentException("b==null");
            return write.apply(b, v);
        }
        public V get(B b) {
            if (b == null) throw new IllegalArgumentException("b==null");
            return read.apply(b);
        }
    }

    public class IntKey extends Key<Integer> {
        public IntKey(String name, Function<B, Integer> read, BiFunction<B, Integer, B> write) {
            super(name, read, write);
        }
    }
    public class DoubleKey extends Key<Double> {
        public DoubleKey(String name, Function<B, Double> read, BiFunction<B, Double, B> write) {
            super(name, read, write);
        }
    }
    public class StringKey extends Key<String> {
        public StringKey(String name, Function<B, String> read, BiFunction<B, String, B> write) {
            super(name, read, write);
        }
    }

    public class ObjKey<F> extends Key<F> {
        public ObjKey(String name, Function<B, F> read, BiFunction<B, F, B> write) {
            super(name, read, write);
        }
    }
}
