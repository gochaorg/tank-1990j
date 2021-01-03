package xyz.cofe.game.tank;

public interface GameUnit<SELF extends Moveable<SELF> & Animated<SELF>> extends Drawing, Rect, Moveable<SELF>, Animated<SELF>, Runnable {
}
