package xyz.cofe.game.tank.ui.cmd;

import xyz.cofe.game.tank.ui.SelectAction;
import xyz.cofe.game.tank.ui.SelectToolProperty;
import xyz.cofe.game.tank.ui.tool.SelectTool;
import xyz.cofe.game.tank.unt.Figure;
import xyz.cofe.game.tank.unt.Scene;

import java.awt.image.BufferedImage;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * Конвертация выделенных объектов в указанный
 * @param <F> Тип в который будет конвертация
 */
public abstract class ConvertToAction<F extends Figure<?>> implements SelectAction, SelectToolProperty {
    private final Function<Figure<?>,F> convert;
    private BufferedImage image;

    public ConvertToAction(Function<Figure<?>,F> convert){
        if( convert==null )throw new IllegalArgumentException( "convert==null" );
        this.convert = convert;
    }

    public ConvertToAction(BufferedImage image,Function<Figure<?>,F> convert){
        if( convert==null )throw new IllegalArgumentException( "convert==null" );
        this.convert = convert;
        this.image = image;
    }

    @Override
    public Optional<BufferedImage> image() {
        return image!=null ? Optional.of(image) : Optional.empty();
    }

    private SelectTool selectTool;
    public SelectTool getSelectTool() { return selectTool; }
    public void setSelectTool(SelectTool selectTool) { this.selectTool = selectTool; }

    @Override
    public void execute(Scene scene, Set<Figure<?>> selection) {
        if( selection==null || selection.isEmpty() || scene==null )return;

        Set<Figure<?>> set = new LinkedHashSet<>(selection);
        Set<Figure<?>> clones = new LinkedHashSet<>();
        for( Figure<?> f : set ){
            var c = convert.apply(f);
            clones.add(c);
            scene.getFigures().add(c);
        }
        scene.getFigures().removeAll(set);

        if( selectTool!=null ){
            selectTool.getSelection().clear();
            selectTool.getSelection().addAll(clones);
        }
    }
}
