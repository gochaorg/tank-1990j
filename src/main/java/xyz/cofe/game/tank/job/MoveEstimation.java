package xyz.cofe.game.tank.job;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import xyz.cofe.fn.Consumer3;
import xyz.cofe.game.tank.GameUnit;
import xyz.cofe.game.tank.geom.Rect;

/**
 * Расчет коллизий
 */
public class MoveEstimation {
    //region moving - кто и куда перемещается
    protected Map<Moving<?>, UnitMoveEstimation<?>> moving = new LinkedHashMap<>();

    /**
     * Информация о перемещении
     * @return кто и куда перемещается
     */
    public Map<Moving<?>, UnitMoveEstimation<?>> getMoving(){
        return moving;
    }
    //endregion
    //region collisions, computeCollisions() - Расчет коллизий
    protected Map<Moving<?>, List<Collision>> collisions = new LinkedHashMap<>();

    public static class Collision {
        public Moving<?> moving;
        public UnitMoveEstimation<?> estimation;
        public Rect intersection;
        public Rect collisionObject;

        public Collision(Moving<?> moving, UnitMoveEstimation<?> estimation, Rect intersection, Rect collisionObject) {
            this.moving = moving;
            this.estimation = estimation;
            this.intersection = intersection;
            this.collisionObject = collisionObject;
        }
    }

    /**
     * Расчет коллизий
     * @param collisions с какими объектами можно столкнуться
     */
    public void computeCollisions( Iterable<? extends Rect> collisions ){
        if( collisions==null )throw new IllegalArgumentException( "collisions==null" );

        Map<Moving<?>, List<Collision>> collisionMap = this.collisions;
        moving.forEach( (move, est) -> {
            var gu = move.getGameUnit();
            collisions.forEach( crect -> {
                if( gu==crect )return;
                var ointr = est.getBounds().intersection(crect);
                if( ointr.isEmpty() )return;

                var collLst = collisionMap.computeIfAbsent(move, k -> new ArrayList<>());
                collLst.add(
                    new Collision(move, est, ointr.get(), crect));
            });
        } );

        this.collisions.forEach( (mv, colls) -> {
            var p0 = mv.getGameUnit().getCentralPoint();
            colls.sort((a, b) -> {
                var da = p0.distance(a.collisionObject.getCentralPoint());
                var db = p0.distance(b.collisionObject.getCentralPoint());
                return Double.compare(da, db);
            });
        });
    }

    /**
     * Коллизии
     * @return Объект и с кем столкнулся
     */
    public Map<Moving<?>, List<Collision>> getCollisions(){
        return collisions;
    }
    //endregion
    //region uncollisiedMovings - Объекты не имеющие коллизий
    @SuppressWarnings("SpellCheckingInspection")
    protected Set<Moving<?>> uncollisiedMovings;

    /**
     * Объекты которые не столкнулись/не имеют коллизий
     * @return Объекты не имеющие коллизий
     */
    public Set<Moving<?>> getUncollisiedMovings(){
        if( uncollisiedMovings!=null )return uncollisiedMovings;
        uncollisiedMovings = new LinkedHashSet<>();
        moving.keySet().stream().filter( mv -> !getCollisions().containsKey(mv) ).forEach( uncollisiedMovings::add );
        return uncollisiedMovings;
    }
    //endregion
    //region apply() - Применение расчетов передвижения
    /**
     * Применение расчетов передвижения
     * @param limitCollPerUnit Лимит пересечений на unit; или -1 - без ограничения
     * @param collision коллизии объекта
     */
    public void apply(int limitCollPerUnit, Consumer3<Moving<?>,GameUnit<?>,Collision> collision){
        if( collision==null )throw new IllegalArgumentException( "collision==null" );
        getUncollisiedMovings().forEach( mv -> {
            var est = getMoving().get(mv);
            if( est!=null ){
                est.apply();
            }
        });
        getCollisions().forEach( (mv,coll)->{
            int idx = 0;
            for( Collision cl : coll ){
                idx++;
                if( (idx<limitCollPerUnit && limitCollPerUnit>0) || limitCollPerUnit<0 ){
                    collision.accept(mv, mv.getGameUnit(), cl);
                }
            }
        });
    }
    //endregion
}
