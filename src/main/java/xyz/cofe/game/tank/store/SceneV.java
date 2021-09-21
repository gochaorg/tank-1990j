package xyz.cofe.game.tank.store;

import xyz.cofe.game.tank.unt.Figura;
import xyz.cofe.game.tank.unt.Scene;
import xyz.cofe.gui.swing.typeconv.impl.RGB;

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

    final DoubleKey width = new DoubleKey("width", Scene::getWidth, (s,v)->{s.setWidth(v);return s;} );
    final DoubleKey height = new DoubleKey("height", Scene::getHeight, (s,v)->{s.setHeight(v);return s;});
    final DoubleKey borderWidth = new DoubleKey("borderWidth", Scene::getBorderWidth, (s,v)->{s.setBorderWidth(v);return s;});
    final StringKey borderColor =
        new StringKey("borderColor",
            s -> s.getBorderColor()!=null ? RGB.rgb(s.getBorderColor()) : null,
            (s,v)->{s.setBorderColor(RGB.rgb(v));return s;});
}
