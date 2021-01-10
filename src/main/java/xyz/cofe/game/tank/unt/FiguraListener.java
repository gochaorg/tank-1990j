package xyz.cofe.game.tank.unt;

public interface FiguraListener<SELF extends Figura<SELF>> {
    void figuraEvent(FiguraEvent<SELF> event);
}
