package xyz.cofe.game.tank;

import xyz.cofe.fn.Consumer1;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.function.UnaryOperator;

/**
 * Шаблон "обсервер" / подписчик-издатель
 * @param <A> тип события
 */
public class Observers<A> extends ArrayList<Consumer1<Observers.Event<A>>> {
    /**
     * Событие
     * @param <A> тип события
     */
    public static class Event<A> {
        /**
         * Издатель
         */
        public final Observers<A> observers;

        /**
         * Событие
         */
        public final A event;

        /**
         * подписчик
         */
        public final Consumer1<Observers.Event<A>> listener;

        /**
         * Коллекция подписчиков которые будет отсоединены после этого сообщения
         */
        public final Collection<Consumer1<Observers.Event<A>>> removeListeners;

        public Event(Observers<A> observers, A event, Consumer1<Observers.Event<A>> listener, Collection<Consumer1<Observers.Event<A>>> removeListeners) {
            this.observers = observers;
            this.event = event;
            this.listener = listener;
            this.removeListeners = removeListeners;
        }

        public void unsubscribe(){
            if( listener!=null && removeListeners!=null ){
                removeListeners.add(listener);
            }
        }
    }
    
    public void fire( A a ){
        var removeSet = new LinkedHashSet<Consumer1<Observers.Event<A>>>();
        //var ls = new LinkedHashSet<Consumer1<Observers.Event<A>>>();
        for( var el : this.toArray(new Consumer1[]{}) ){
            if( el!=null ){
                //noinspection unchecked,rawtypes,rawtypes
                el.accept(new Event(this,a,el,removeSet));
            }
        }
        removeAll(removeSet);
    }

    public Runnable listen( Consumer1<Observers.Event<A>> listener ){
        if( listener==null )throw new IllegalArgumentException( "listener==null" );
        add( listener );
        var ref = new WeakReference<>(listener);
        return ()->{
            var r = ref.get();
            if( removeIf( c -> c==r ) ){
                //System.out.println("removed listener");
            }
        };
    }

    @Override
    public void replaceAll(UnaryOperator<Consumer1<Observers.Event<A>>> operator) {
        UnaryOperator<Consumer1<Observers.Event<A>>> op = new UnaryOperator<Consumer1<Observers.Event<A>>>(){
            @Override
            public Consumer1<Observers.Event<A>> apply(Consumer1<Observers.Event<A>> aConsumer1) {
                var res = operator.apply(aConsumer1);
                if( res==null )throw new IllegalArgumentException( "res==null" );
                return res;
            }
        };
        super.replaceAll(operator);
    }

    @Override
    public Consumer1<Observers.Event<A>> set(int index, Consumer1<Observers.Event<A>> observer) {
        if( observer==null )throw new IllegalArgumentException( "observer==null" );
        return super.set(index, observer);
    }

    @Override
    public boolean add(Consumer1<Observers.Event<A>> observer) {
        return super.add(observer);
    }

    @Override
    public void add(int index, Consumer1<Observers.Event<A>> observer) {
        super.add(index, observer);
    }

    @Override
    public boolean addAll(Collection<? extends Consumer1<Observers.Event<A>>> c) {
        if( c==null )throw new IllegalArgumentException( "c==null" );
        if(c.stream().anyMatch(Objects::nonNull)){
            throw new IllegalArgumentException( "collection contains null item" );
        }
        return super.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends Consumer1<Observers.Event<A>>> c) {
        if( c==null )throw new IllegalArgumentException( "c==null" );
        if(c.stream().anyMatch(Objects::nonNull)){
            throw new IllegalArgumentException( "collection contains null item" );
        }
        return super.addAll(index, c);
    }
}
