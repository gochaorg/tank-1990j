package xyz.cofe.game.tank.store;

import java.awt.Font;
import java.util.function.Supplier;

public class FontV extends OBJ<Font> implements ObjectMapper {
    public static final FontV instance = new FontV();
    @Override public void registry(ObjectMappers om) {
        ObjectMappers.mappers.put(instance.clazz, instance);
    }

    public FontV() {
        super(Font.class, () -> new Font(Font.SERIF,Font.PLAIN,12) );
    }

    public final StringKey name = new StringKey("name", f -> f.getFamily(), (f,v) -> {
        return new Font(v, f.getStyle(), f.getSize() );
    });

    public final IntKey style = new IntKey("style", f -> f.getStyle(), (f,v) -> {
        return new Font(f.getFontName(), v, f.getSize() );
    });

    public final IntKey size = new IntKey("size", f -> f.getSize(), (f,v) -> {
        return new Font(f.getFontName(), f.getStyle(), v );
    });
}
