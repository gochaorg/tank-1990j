package xyz.cofe.game.tank.ui.cmd;

import xyz.cofe.game.tank.unt.Bullet;

public class ConvertToBulletAction extends ConvertToAction<Bullet> {
    public ConvertToBulletAction(){
        super(Bullet::new);
    }
}
