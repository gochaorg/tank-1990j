package xyz.cofe.game.tank;

import xyz.cofe.game.tank.geom.Rect;

public interface GameUnit<SELF extends Moveable<SELF> & Animated<SELF>> extends Drawing, Rect, Moveable<SELF>, Animated<SELF>, Runnable {
}
