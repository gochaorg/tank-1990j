package xyz.cofe.game.tank.store;

import xyz.cofe.game.tank.geom.Point;
import xyz.cofe.simpletypes.SimpleTypes;
import static xyz.cofe.game.tank.store.ObjectMappers.mappers;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

public class MapStore {
    //region views
    @SuppressWarnings("rawtypes")
    private static final Map<Class,OBJ> views = new LinkedHashMap<>();
    @SuppressWarnings("rawtypes")
    private static OBJ viewOf(Class c){
        return views.computeIfAbsent(c, MapStore::viewOf0);
    }
    @SuppressWarnings("rawtypes")
    private static OBJ viewOf0(Class c){
        OBJ view1 = mappers.get(c);
        if( view1!=null )return view1;

        Set<Class<?>> prefer = new LinkedHashSet<>();
        for( var cls : mappers.keySet() ){
            if(cls.isAssignableFrom(c) ){
                prefer.add(cls);
            }
        }

        boolean repeat1 = true;
        while (true) {
            repeat1 = false;
            if( prefer.size()<2 )break;

            var itr = prefer.iterator();
            var c1 = itr.next();

            while (itr.hasNext()) {
                var c2 = itr.next();

                var prt = c1;
                var chl = c2;
                if (prt.isAssignableFrom(chl) && !chl.isAssignableFrom(prt)) {
                    prefer.remove(prt);
                    repeat1 = true;
                    break;
                }

                prt = c2;
                chl = c1;
                if (prt.isAssignableFrom(chl) && !chl.isAssignableFrom(prt)) {
                    prefer.remove(prt);
                    repeat1 = true;
                    break;
                }
            }

            if( !repeat1 )break;
        }

        if( prefer.size()==1 ){
            var c1 = prefer.iterator().next();
            return mappers.get(c1);
        }

        return null;
    }
    //endregion
    //region store()
    public <B> Map<String, Object> store( B obj, OBJ<B> objView ) {
        if (obj == null) return null;

        LinkedHashMap<String, Object> m = new LinkedHashMap<>();
        m.put("@type", objView.clazz.getName());
        objView.keys.forEach(key -> {
            var value = key.get(obj);
            if( value==null )return;
            if( SimpleTypes.isSimple(value.getClass()) ) {
                m.put(key.name, key.get(obj));
            }else{
                //noinspection rawtypes
                OBJ view = viewOf(value.getClass()); //OBJ.objectMappers.fetch( value.getClass() );
                if( view!=null ) {
                    //noinspection unchecked
                    var mo = store(value, view);
                    m.put(key.name, mo);
                }
            }
        });
        return m;
    }
    public Map<String, Object> store(Point p) {
        return store(p, PointV.instance);
    }
    public Map<String, Object> store(Object obj){
        if( obj==null )return null;

        //noinspection SuspiciousMethodCalls
        var view = mappers.get( obj.getClass().getName() );
        if( view==null )throw new IllegalStateException( "view for "+obj.getClass().getName() );

        //noinspection unchecked,rawtypes,rawtypes
        return store(obj, (OBJ) view);
    }
    //endregion
    //region restore()
    private static final Map<String,Class<?>> classByName = new ConcurrentHashMap<>();
    private Class<?> loadClass(String cname) {
        Class<?> cl = null;
        try {
            cl = Class.forName(cname, false, this.getClass().getClassLoader());
        } catch (ClassNotFoundException e) {
            throw new Error(e);
        }
        return cl;
    }
    public <B> B restore( Map<String,Object> map ){
        if( map==null )return null;

        var typeName = map.get("@type");
        if( typeName==null )throw new IllegalArgumentException("undefined type");

        var objMap = mappers.get(
            classByName.computeIfAbsent(typeName.toString(), this::loadClass)
        );
        if( objMap==null )throw new IllegalStateException("objectMapper with name="+typeName+" not found");

        var inst = objMap.newInstance.get();
        if( inst==null )throw new IllegalStateException("instance of "+typeName+" create fail");

        if( !objMap.clazz.isAssignableFrom(inst.getClass()) ){
            throw new IllegalStateException("create "+inst+" not instance of "+objMap.clazz);
        }

        for( var key : objMap.keys ){
            if( map.containsKey(key.name) ){
                var v = map.get(key.name);
                if( v==null )continue;

                if( SimpleTypes.isSimple(v.getClass()) ) {
                    //noinspection unchecked,rawtypes,rawtypes
                    var ninst = ((BiFunction) key.write).apply(inst, v);
                    if (ninst != null) {
                        inst = ninst;
                    }
                }else if( v instanceof Map ){
                    //noinspection unchecked,rawtypes,rawtypes
                    var nested = restore((Map)v);
                    //noinspection unchecked,rawtypes,rawtypes
                    var ninst = ((BiFunction) key.write).apply(inst, nested);
                    if (ninst != null) {
                        inst = ninst;
                    }
                }
            }
        }

        //noinspection unchecked
        return (B)inst;

//        return null;
    }
    //endregion
}
