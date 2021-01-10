package xyz.cofe.game.tank.job;

import xyz.cofe.game.tank.geom.Rect;

/**
 * Расчет перемещений объектов с учетом коллизций
 */
public class MoveCollector {
    //region movings : Iterable<Moving<?>>
    private Iterable<Moving<?>> movings;

    public Iterable<Moving<?>> getMovings(){
        return movings;
    }

    public void setMovings(Iterable<Moving<?>> movings){
        this.movings = movings;
    }
    //endregion
    //region collisions : Iterable<Rect>
    private Iterable<Rect> collisions;

    public Iterable<Rect> getCollisions(){
        return collisions;
    }

    public void setCollisions(Iterable<Rect> collisions){
        this.collisions = collisions;
    }
    //endregion

    /**
     * Расчет перемещений
     * @return Расчет перемещений
     */
    public MoveEstimation estimate(){
        MoveEstimation estimation = new MoveEstimation();
        if( movings!=null ){
            movings.forEach( m -> {
                var oest = m.estimate();
                if( oest.isPresent() ){
                    estimation.getMoving().put(m, oest.get());
                }
            });
            if( collisions!=null ){
                estimation.computeCollisions(collisions);
            }
        }
        return estimation;
    }
}
