package xyz.cofe.game.tank.store;

import xyz.cofe.game.tank.geom.Point;
import xyz.cofe.simpletypes.SimpleTypes;
import static xyz.cofe.game.tank.store.ObjectMappers.mappers;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
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

    protected final AtomicInteger storeIdGen = new AtomicInteger(0);
    protected final Map<Object,Integer> storeIdRefs = new HashMap<>();

    //region store()
    public <B> Map<String, Object> store( B obj, OBJ<B> objView ) {
        if (obj == null) return null;

        LinkedHashMap<String, Object> m = new LinkedHashMap<>();
        m.put("@type", objView.clazz.getName());
        var ref = storeIdRefs.get(obj);
        if( ref!=null ){
            m.put("@ref", ref);
            return m;
        }

        var oid = storeIdGen.incrementAndGet();
        m.put("@oid", oid);
        storeIdRefs.put(obj, oid);

        objView.keys.forEach(key -> {
            var value = key.get(obj);
            if( value==null )return;
            if( SimpleTypes.isSimple(value.getClass())
                || key instanceof OBJ.StringKey
                || key instanceof OBJ.DoubleKey
                || key instanceof OBJ.IntKey
                || key instanceof OBJ.BooleanKey
            ) {
                m.put(key.name, key.get(obj));
            }else{
                if( key instanceof OBJ.ListKey ){
                    //noinspection rawtypes
                    OBJ.ListKey lkey = (OBJ.ListKey)key;
                    //noinspection unchecked
                    var listObj = lkey.get(obj);
                    List<Object> resultLst = new ArrayList<>();
                    if( listObj instanceof List ){
                        ((List<?>) listObj).forEach( item -> {
                            if( item!=null ) {
                                var itemMap = store(item);
                                resultLst.add(itemMap);
                            }
                        });
                    }
                    m.put(key.name, resultLst);
                }else {
                    //noinspection rawtypes
                    OBJ view = viewOf(value.getClass()); //OBJ.objectMappers.fetch( value.getClass() );
                    if (view != null) {
                        //noinspection unchecked
                        var mo = store(value, view);
                        m.put(key.name, mo);
                    }
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

        var view = viewOf(obj.getClass());
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

    protected final Map<Integer,Object> restoreRefs = new HashMap<>();
    public <B> B restore( Map<String,Object> map ){
        if( map==null )return null;

        var typeName = map.get("@type");
        if( typeName==null )throw new IllegalArgumentException("undefined type");

        var refAtrr = map.get("@ref");
        if( refAtrr instanceof Number ){
            var refId = ((Number) refAtrr).intValue();
            var ref = restoreRefs.get(refId);
            if( ref!=null ){
                //noinspection unchecked
                return (B)ref;
            }
        }

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

                if( SimpleTypes.isSimple(v.getClass())
                    || key instanceof OBJ.StringKey
                    || key instanceof OBJ.DoubleKey
                    || key instanceof OBJ.IntKey
                    || key instanceof OBJ.BooleanKey
                ) {
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
                }else if( v instanceof List && key instanceof OBJ.ListKey ){
                    ArrayList<Object> resultList = restoreList(typeName, key.name, (List) v);
                    //noinspection unchecked,rawtypes,rawtypes
                    var ninst = ((BiFunction) key.write).apply(inst, resultList);
                    if (ninst != null) {
                        inst = ninst;
                    }
                }else {
                    throw new IllegalStateException("can't restore "+key.name+" for "+typeName+", accept "+v);
                }
            }
        }

        var oidAttr = map.get("@oid");
        if( oidAttr instanceof Number ){
            restoreRefs.put(((Number) oidAttr).intValue(), inst);
        }

        //noinspection unchecked
        return (B)inst;
    }

    private ArrayList<Object> restoreList(Object typeName, String keyName, List v) {
        var resultList = new ArrayList<>();
        //noinspection unchecked
        v.forEach(itemView -> {
            if( itemView==null )return;
            if( SimpleTypes.isSimple(itemView.getClass()) ){
                resultList.add(itemView);
            }else if( itemView instanceof Map ){
                //noinspection unchecked
                var item = restore((Map<String, Object>) itemView);
                if( item!=null ){
                    resultList.add(item);
                }
            }else if( itemView instanceof List ){
                //noinspectionrawtypes,rawtypes
                var nested = restoreList(typeName, keyName, (List)itemView);
                resultList.add(nested);
            }else {
                throw new IllegalStateException("can't restore item "+itemView+" for "+ keyName+" of "+ typeName);
            }
        });
        return resultList;
    }
    //endregion
}
