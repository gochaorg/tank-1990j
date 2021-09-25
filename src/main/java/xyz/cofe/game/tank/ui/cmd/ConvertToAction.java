package xyz.cofe.game.tank.ui.cmd;

import xyz.cofe.game.tank.ui.SelectAction;
import xyz.cofe.game.tank.ui.SelectToolProperty;
import xyz.cofe.game.tank.ui.tool.SelectTool;
import xyz.cofe.game.tank.unt.Brick;
import xyz.cofe.game.tank.unt.Figura;
import xyz.cofe.game.tank.unt.Scene;

import java.awt.image.BufferedImage;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public abstract class ConvertToAction<F extends Figura<?>> implements SelectAction, SelectToolProperty {
    private final Function<Figura<?>,F> convert;
    private BufferedImage image;

    public ConvertToAction(Function<Figura<?>,F> convert){
        if( convert==null )throw new IllegalArgumentException( "convert==null" );
        this.convert = convert;
    }

    public ConvertToAction(BufferedImage image,Function<Figura<?>,F> convert){
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
    public void execute(Scene scene, Set<Figura<?>> selection) {
        if( selection==null || selection.isEmpty() || scene==null )return;

        Set<Figura<?>> set = new LinkedHashSet<>(selection);
        Set<Figura<?>> clones = new LinkedHashSet<>();
        for( Figura<?> f : set ){
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
