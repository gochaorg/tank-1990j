package xyz.cofe.game.tank.job;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import xyz.cofe.fn.Consumer2;
import xyz.cofe.fn.Consumer3;
import xyz.cofe.game.tank.GameUnit;
import xyz.cofe.game.tank.geom.Point;
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
    protected Map<Moving<?>, List<Rect>> collisions = new LinkedHashMap<>();

    /**
     * Расчет коллизий
     * @param collisions с какими объектами можно столкнуться
     */
    public void computeCollisions( Iterable<Rect> collisions ){
        if( collisions==null )throw new IllegalArgumentException( "collisions==null" );

        Map<Moving<?>, List<Rect>> collisionMap = this.collisions;
        moving.forEach( (move, est) -> {
            var gu = move.getGameUnit();
            collisions.forEach( crect -> {
                if( gu==crect )return;
                var ointr = est.getBounds().intersection(crect);
                if( ointr.isEmpty() )return;

                var collLst = collisionMap.computeIfAbsent(move, k -> new ArrayList<>());
                collLst.add(ointr.get());
            });
        } );

        this.collisions.forEach( (mv, colls) -> {
            var p0 = mv.getGameUnit().centralPoint();
            colls.sort((a, b) -> {
                var da = p0.distance(a.centralPoint());
                var db = p0.distance(b.centralPoint());
                return Double.compare(da, db);
            });
        });
    }

    /**
     * Коллизии
     * @return Объект и с кем столкнулся
     */
    public Map<Moving<?>, List<Rect>> getCollisions(){
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
    public void apply(int limitCollPerUnit, Consumer3<Moving<?>,GameUnit<?>,Rect> collision){
        if( collision==null )throw new IllegalArgumentException( "collision==null" );
        getUncollisiedMovings().forEach( mv -> {
            var est = getMoving().get(mv);
            if( est!=null ){
                est.apply();
            }
        });
        getCollisions().forEach( (mv,coll)->{
            int idx = 0;
            for( Rect rect : coll ){
                idx++;
                if( (idx<limitCollPerUnit && limitCollPerUnit>0) || limitCollPerUnit<0 ){
                    collision.accept(mv, mv.getGameUnit(), rect);
                }
            }
        });
    }
    //endregion
}
