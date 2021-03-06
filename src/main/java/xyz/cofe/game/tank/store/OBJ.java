package xyz.cofe.game.tank.store;

import xyz.cofe.collection.ClassMap;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * mapping/отображение объекта на простые структуры для сериализации/десериализации
 * @param <B>
 */
public abstract class OBJ<B> {
    /**
     * Тип объекта
     */
    public final Class<B> clazz;

    /**
     * Создание нового экземпляра класса
     */
    public final Supplier<B> newInstance;

    /**
     * Список свойств класса
     */
    public final List<Key<?>> keys = new ArrayList<>();

    /**
     * Конструктор
     * @param cls тип объекта
     * @param newInst Создание нового экземпляра класса
     */
    public OBJ(Class<B> cls, Supplier<B> newInst) {
        if (cls == null) throw new IllegalArgumentException("cls==null");
        if (newInst == null) throw new IllegalArgumentException("newInst==null");
        clazz = cls;
        newInstance = newInst;
    }

    /**
     * Описывает сохраняемое свойства класса
     * @param <V> Тип свойства
     */
    public abstract class Key<V> {
        /**
         * Имя свойства
         */
        public final String name;

        /**
         * Чтение значения свойства
         */
        public final Function<B, V> read;

        /**
         * Запись значения свойства
         */
        public final BiFunction<B, V, B> write;

        /**
         * Конструктор
         * @param name Имя свойства
         * @param read Чтение значения свойства
         * @param write Запись значения свойства
         */
        public Key(String name, Function<B, V> read, BiFunction<B, V, B> write) {
            if (name == null) throw new IllegalArgumentException("name==null");
            if (read == null) throw new IllegalArgumentException("read==null");
            if (write == null) throw new IllegalArgumentException("write==null");
            this.name = name;
            this.read = read;
            this.write = write;
            keys.add(this);
        }

        /**
         * Запись значения в объект
         * @param b объект
         * @param v значение
         * @return объект или новый объект
         */
        public B set(B b, V v) {
            if (b == null) throw new IllegalArgumentException("b==null");
            return write.apply(b, v);
        }

        /**
         * Чтение значения свойства
         * @param b объект
         * @return значение
         */
        public V get(B b) {
            if (b == null) throw new IllegalArgumentException("b==null");
            return read.apply(b);
        }
    }

    public class BooleanKey extends Key<Boolean> {
        public BooleanKey(String name, Function<B, Boolean> read, BiFunction<B, Boolean, B> write) {
            super(name, read, write);
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
    public class ListKey<F> extends Key<List<F>> {
        public ListKey(String name, Function<B,List<F>> read, BiFunction<B,List<F>,B> write){
            super(name,read,write);
        }
    }
}
