package xyz.cofe.game.tank.job;

import xyz.cofe.game.tank.geom.Rect;

/**
 * Расчет перемещений объектов с учетом коллизций
 */
public class MoveCollector {
    //region movings : Iterable<Moving<?>> - задания перемещения
    private Iterable<? extends Moving<?>> movings;

    /**
     * Указывает перемещаемые игровые объекты/задачи
     * @return задания перемещения
     */
    public Iterable<? extends Moving<?>> getMovings(){
        return movings;
    }

    /**
     * Указывает перемещаемые игровые объекты/задачи
     * @param movings задания перемещения
     */
    public void setMovings(Iterable<? extends Moving<?>> movings){
        this.movings = movings;
    }
    //endregion
    //region collisions : Iterable<Rect> - объекты с которыми возможно столкновения
    private Iterable<? extends Rect> collisions;

    /**
     * Указывает объекты с которыми возможно столкновения
     * @return объекты с которыми возможно столкновения
     */
    public Iterable<? extends Rect> getCollisions(){
        return collisions;
    }

    /**
     * Указывает объекты с которыми возможно столкновения
     * @param collisions объекты с которыми возможно столкновения
     */
    public void setCollisions(Iterable<? extends Rect> collisions){
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
