package xyz.cofe.game.tank.store;

import xyz.cofe.game.tank.geom.Point;
import xyz.cofe.game.tank.unt.Figura;
import xyz.cofe.game.tank.unt.Note;
import xyz.cofe.game.tank.unt.Scene;
import xyz.cofe.gui.swing.typeconv.impl.RGB;

import java.util.function.Supplier;

public class NoteV extends OBJ<Note> implements ObjectMapper {
    public static final NoteV instance = new NoteV();
    @Override public void registry(ObjectMappers om) {
        ObjectMappers.mappers.put(instance.clazz, instance);
    }

    public NoteV() {
        super(Note.class, Note::new);
    }

    @SuppressWarnings("Convert2MethodRef")
    public final StringKey title = new StringKey("title", ob -> ob.getTitle(), (ob, v)->{
        ob.setTitle(v);
        return ob;
    });

    @SuppressWarnings("Convert2MethodRef")
    public final StringKey content = new StringKey("content", ob -> ob.getContent(), (ob, v)->{
        ob.setContent(v);
        return ob;
    });

    public final ObjKey<Point> location = new ObjKey<Point>("location",
        Figura::getLocation,
        (ob,v) -> {
            ob.setLocation(v);
            return ob;
        }
    );

    public final StringKey titleColor =
        new StringKey("titleColor",
            s -> s.getTitleColor()!=null ? RGB.rgb(s.getTitleColor()) : null,
            (s,v)->{s.setTitleColor(RGB.rgb(v));return s;});

    public final StringKey fillColor =
        new StringKey("fillColor",
            s -> s.getFillColor()!=null ? RGB.rgb(s.getFillColor()) : null,
            (s,v)->{s.setFillColor(RGB.rgb(v));return s;});

    public final StringKey outlineColor =
        new StringKey("outlineColor",
            s -> s.getOutlineColor()!=null ? RGB.rgb(s.getOutlineColor()) : null,
            (s,v)->{s.setOutlineColor(RGB.rgb(v));return s;});

    @SuppressWarnings("Convert2MethodRef")
    final DoubleKey width = new DoubleKey("width", s -> s.getWidth(), (s, v)->{s.setWidth(v);return s;} );

    @SuppressWarnings("Convert2MethodRef")
    final DoubleKey height = new DoubleKey("height", s -> s.getHeight(), (s, v)->{s.setHeight(v);return s;});

    @SuppressWarnings("Convert2MethodRef")
    final DoubleKey contentTopMargin = new DoubleKey("contentTopMargin",
        s -> s.getContentTopMargin(), (s, v)->{s.setContentTopMargin(v);return s;});

    @SuppressWarnings("Convert2MethodRef")
    final BooleanKey contentVisible = new BooleanKey("contentVisible",
        s -> s.isContentVisible(), (s, v)->{s.setContentVisible(v);return s;});
}
