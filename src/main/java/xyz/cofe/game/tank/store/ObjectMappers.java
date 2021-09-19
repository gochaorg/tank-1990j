package xyz.cofe.game.tank.store;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ServiceLoader;

public final class ObjectMappers {
    private ObjectMappers(){ }
    public static final Map<Class<?>,OBJ<?>> mappers = new LinkedHashMap<>();

    public final static ObjectMappers instance;
    static {
        //noinspection InstantiationOfUtilityClass
        instance = new ObjectMappers();
        for( ObjectMapper om : ServiceLoader.load(ObjectMapper.class) ){
            om.registry(instance);
        }
    }
}
