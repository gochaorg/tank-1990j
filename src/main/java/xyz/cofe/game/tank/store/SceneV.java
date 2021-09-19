package xyz.cofe.game.tank.store;

import xyz.cofe.game.tank.unt.Figura;
import xyz.cofe.game.tank.unt.Scene;

import java.util.List;
import java.util.function.Supplier;

public class SceneV extends OBJ<Scene> implements ObjectMapper {
    public static final SceneV instance = new SceneV();
    public SceneV() {
        super(Scene.class, Scene::new);
    }
    @Override
    public void registry(ObjectMappers om) {
        ObjectMappers.mappers.put(instance.clazz, instance);
    }

    final ListKey<Figura<?>> figures = new ListKey<Figura<?>>(
        "figures",
        Scene::getFigures,
        ((scene, figuras) -> {
            scene.getFigures().clear();
            scene.getFigures().addAll(figuras);
            return scene;
        })
    );
}
