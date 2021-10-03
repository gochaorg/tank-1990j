package xyz.cofe.game.tank.ui.cmd;

import xyz.cofe.game.tank.sprite.SpritesData;
import xyz.cofe.game.tank.unt.Enemy;
import xyz.cofe.game.tank.unt.Water;

public class ConvertToEnemyAction extends ConvertToAction<Enemy> {
    public ConvertToEnemyAction(){
        super(SpritesData.enemy_slow_right.images().get(0), Enemy::new);
    }
}
