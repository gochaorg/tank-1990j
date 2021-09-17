package xyz.cofe.game.tank.ui;

import xyz.cofe.game.tank.unt.Figura;
import xyz.cofe.gui.swing.properties.Property;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class PropertySheet extends xyz.cofe.gui.swing.properties.PropertySheet {
    @Override
    public void edit(Object bean) {
        super.edit(bean, extra(bean));
    }

    public static class ExtraProperties {
        public final Map<Class<?>, Set<Function<Object,List<Property>>>> map = new LinkedHashMap<>();
    }

    public static final ExtraProperties extraProperties = new ExtraProperties();

    protected List<Property> extra(Object ob){
        ArrayList<Property> props = new ArrayList<>();
        if( ob!=null ) {
            var c = ob.getClass();
            extraProperties.map.forEach((cls, fns) -> {
                if( cls.isAssignableFrom(c) ){
                    for( var fn : fns ) {
                        var pl = fn.apply(ob);
                        if (pl != null) {
                            props.addAll(pl);
                        }
                    }
                }
            });
        }
        return props;
    }

    public static class Extra<T> {
        public final Class<T> beanType;
        public Extra( Class<T> beanType ){
            if( beanType==null )throw new IllegalArgumentException( "beanType==null" );
            this.beanType = beanType;
        }

        public <P> void property(String name, Class<P> type, Function<T,P> read ){
            if( name==null )throw new IllegalArgumentException( "name==null" );
            if( type==null )throw new IllegalArgumentException( "type==null" );
            if( read==null )throw new IllegalArgumentException( "read==null" );
            extraProperties.map.computeIfAbsent(beanType, x -> new LinkedHashSet<>()).add(bean -> {
                List<Property> lst = new ArrayList<>();
                Property prop = new Property(name,type, ()->read.apply((T)bean), v -> v);
                lst.add(prop);
                return lst;
            });
        }
        public <P> void property(String name, Class<P> type, Function<T,P> read, BiFunction<T,P,P> write ){
            if( name==null )throw new IllegalArgumentException( "name==null" );
            if( type==null )throw new IllegalArgumentException( "type==null" );
            if( read==null )throw new IllegalArgumentException( "read==null" );
            if( write==null )throw new IllegalArgumentException( "write==null" );
            extraProperties.map.computeIfAbsent(beanType, x -> new LinkedHashSet<>()).add(bean -> {
                List<Property> lst = new ArrayList<>();
                Property prop = new Property(name,type, ()->read.apply((T)bean), v -> write.apply((T)bean, (P)v));
                lst.add(prop);
                return lst;
            });
        }
    }

    public static <T> void extend(Class<T> cls, Consumer<Extra<T>> extend ){
        if( cls==null )throw new IllegalArgumentException( "cls==null" );
        if( extend==null )throw new IllegalArgumentException( "extend==null" );
        extend.accept(new Extra<>(cls));
    }
}
