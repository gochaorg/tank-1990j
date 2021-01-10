package xyz.cofe.game.tank.unt;

public class FiguraEvent<SELF extends Figura<SELF>> {
    public FiguraEvent(SELF self){
        this.figura = self;
    }

    protected SELF figura;
    public SELF getFigura(){ return figura; }
}
