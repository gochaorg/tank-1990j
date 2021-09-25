package xyz.cofe.game.tank.ui.cmd;

import xyz.cofe.game.tank.sprite.SpritesData;
import xyz.cofe.game.tank.unt.Bullet;

public class ConvertToBulletAction extends ConvertToAction<Bullet> {
    public ConvertToBulletAction(){
        super(SpritesData.bullet.images().get(0), Bullet::new);
    }
}
