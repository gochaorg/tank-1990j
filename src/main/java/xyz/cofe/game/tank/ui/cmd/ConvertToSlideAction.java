package xyz.cofe.game.tank.ui.cmd;

import xyz.cofe.game.tank.sprite.SpritesData;
import xyz.cofe.game.tank.unt.Slide;

public class ConvertToSlideAction extends ConvertToAction<Slide> {
    public ConvertToSlideAction(){
        super(SpritesData.lvl_slide.images().get(0), Slide::new);
    }
}
